package cn.sourceplan.finance.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.core.domain.entity.SysUser;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.finance.domain.PieceWagePlan;
import cn.sourceplan.finance.mapper.PieceWagePlanMapper;
import cn.sourceplan.production.domain.Report;
import cn.sourceplan.production.mapper.ReportMapper;
import cn.sourceplan.quality.domain.ReportQuality;
import cn.sourceplan.quality.mapper.ReportQualityMapper;
import cn.sourceplan.system.mapper.SysUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.finance.mapper.PayrollMapper;
import cn.sourceplan.finance.domain.Payroll;
import cn.sourceplan.finance.service.IPayrollService;

import javax.servlet.http.HttpServletRequest;

/**
 * 工资单Service业务层处理
 *
 * @author jinzhong
 * @date 2023-03-11
 */
@Service
public class PayrollServiceImpl implements IPayrollService
{
    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    @Autowired
    private ReportQualityMapper reportQualityMapper;

    @Autowired
    private PieceWagePlanMapper pieceWagePlanMapper;

    @Autowired
    private SysUserMapper userMapper;
    /**
     * 查询工资单
     *
     * @param id 工资单主键
     * @return 工资单
     */
    @Override
    public Payroll selectPayrollById(Long id)
    {
        return payrollMapper.selectById(id);
    }

    /**
     * 查询工资单列表
     *
     * @param payroll 工资单
     * @return 工资单
     */
    @Override
    public List<Payroll> selectPayrollList(Payroll payroll)
    {
        String reportIdsSql = null;
        String reportIds = payroll.getReportIds();
        if(StringUtils.isNotBlank(reportIds)){
            reportIdsSql=
                    "  (report_ids = "+reportIds+" or "+
                    "report_ids LIKE '%,"+reportIds+"' or "+
                    "report_ids LIKE '"+reportIds+",%' or "+
                    "report_ids LIKE '%,"+reportIds+",%' ) ";
            payroll.setReportIds(null);
        }

        QueryWrapper<Payroll> qw = new QueryWrapper<>(payroll);
        qw.apply(" 1=1 ");
        if(payroll.getParams()!=null){
            Object beginPayrollDate = payroll.getParams().get("beginPayrollDate");
            if(beginPayrollDate!=null){
                qw.ge("payroll_date",beginPayrollDate);
            }
            Object endPayrollDate = payroll.getParams().get("endPayrollDate");
            if(endPayrollDate!=null){
                qw.le("payroll_date",endPayrollDate);
            }
        }
        if(reportIdsSql!=null){
            qw.apply(reportIdsSql);
        }

        return payrollMapper.selectList(qw);
    }

    /**
     * 新增工资单
     *
     * @param payroll 工资单
     * @return 结果
     */
    @Override
    public int insertPayroll(Payroll payroll)
    {
        return payrollMapper.insert(payroll);
    }

    /**
     * 修改工资单
     *
     * @param payroll 工资单
     * @return 结果
     */
    @Override
    public int updatePayroll(Payroll payroll)
    {
        return payrollMapper.updateById(payroll);
    }

    /**
     * 批量删除工资单
     *
     * @param ids 需要删除的工资单主键
     * @return 结果
     */
    @Override
    public int deletePayrollByIds(Long[] ids)
    {
        return payrollMapper.deletePayrollByIds(ids);
    }

    /**
     * 删除工资单信息
     *
     * @param id 工资单主键
     * @return 结果
     */
    @Override
    public int deletePayrollById(Long id)
    {
        return payrollMapper.deleteById(id);
    }

    @Override
    public AjaxResult payRollPreview(Long[] ids) {

        //这里默认取计件工资方案，将来扩展计时或其他可以通过wageType属性来区分


        //查询所有报工单
        Report reportQ = new Report();
        Map<String, Object> params = reportQ.getParams();
        params.put("ids",StringUtils.join(ids,","));
        List<Report> reports = reportMapper.selectReportList(reportQ);

        //按照人员分组
        Map<Long, List<Report>> map = reports.stream().collect(Collectors.groupingBy(t -> t.getReportUserId()));
        List <Payroll> resultList = new ArrayList<>();
        for(Long key:map.keySet()){
            String reportIds = "";
            String reportNumbers ="";
            String planNumbers = "";
            BigDecimal shouldPaySum = new BigDecimal(0.0F);
            SysUser sysUser = userMapper.selectUserById(key);
            for (Report report:map.get(key)) {
                //查询当前用户所有报工单中工序ID+物料ID+当前用户岗位ID查询计件工资方案
                QueryWrapper<PieceWagePlan> qw= new QueryWrapper<>();
                qw.eq("process_id",report.getProcessId());
                qw.eq("material_id",report.getMaterialId());
                qw.eq("status","A");
                qw.orderByDesc("create_time","update_time");
                List<PieceWagePlan> pieceWagePlans = pieceWagePlanMapper.selectList(qw);

                if(pieceWagePlans==null||pieceWagePlans.size()==0){
                    return AjaxResult.error("没有设置计件方案，请前去设置" +
                            "工序："+report.getProcessName()+"----物料："+report.getMaterialName()+"的计件方案");
                }
                PieceWagePlan pieceWagePlan = pieceWagePlans.get(0);
                //获取方案中的单价
                BigDecimal planPrice = pieceWagePlan.getPrice();

                if((!"A".equals(report.getQualityStatus()))&&(!"C".equals(report.getQualityStatus()))){
                    return AjaxResult.error("只有免检和质检通过的报工允许生成工资单");
                }

                if("A".equals(report.getQualityStatus())){
                    //如果当前报工为免检，该报工单工资为 报工数量X方案中单价
                    BigDecimal q = new BigDecimal(String.valueOf(report.getReportQuantity()));
                    BigDecimal multiply = planPrice.multiply(q);
                    shouldPaySum =shouldPaySum.add(multiply);
                }
                if("C".equals(report.getQualityStatus())){
                    //质检通过状态
                    // 该报工单工资为 方案中单价X当前报工单所关联质检单的合格数量
                    QueryWrapper<ReportQuality> qwReportQuality = new QueryWrapper<>();
                    qwReportQuality.eq("report_id",report.getId());
                    List<ReportQuality> reportQualities = reportQualityMapper.selectList(qwReportQuality);
                    if(reportQualities==null||reportQualities.size()==0){
                        return AjaxResult.error("报工单:"+report.getNumber()+"，的质检单异常，请联系管理员处理");
                    }
                    BigDecimal q = reportQualities.get(0).getQualifiedQuantity();
                    BigDecimal multiply = planPrice.multiply(q);
                    shouldPaySum = shouldPaySum.add(multiply);
                }
                reportIds+=report.getId()+",";
                planNumbers+=pieceWagePlan.getNumber()+",";
                reportNumbers+=report.getNumber()+",";

            }
            shouldPaySum = shouldPaySum.setScale(3,BigDecimal.ROUND_HALF_UP);
            Payroll payroll = new Payroll();
            payroll.setShouldPay(shouldPaySum);
            payroll.setRealPay(shouldPaySum);
            payroll.setPayrollDate(new Date());
            payroll.setReportIds(reportIds);
            payroll.setUserId(sysUser.getUserId());
            payroll.setUserName(sysUser.getNickName());
            payroll.setReportNumbers(reportNumbers);
            payroll.setPlanInfo("");
            resultList.add(payroll);
        }

        return AjaxResult.success(resultList);
    }

    @Override
    public BigDecimal sumByUserId(Long id, HttpServletRequest httpServletRequest) {
        String beginDate = httpServletRequest.getParameter("beginDate");
        String endDate = httpServletRequest.getParameter("endDate");

        BigDecimal result = payrollMapper.sumByUserId(beginDate,endDate,id);

        return result;
    }
}
