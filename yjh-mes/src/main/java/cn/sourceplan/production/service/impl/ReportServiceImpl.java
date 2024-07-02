package cn.sourceplan.production.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.hutool.core.date.DateUtil;
import cn.sourceplan.common.core.page.PageDomain;
import cn.sourceplan.common.core.page.TableSupport;
import cn.sourceplan.common.domain.SysUserStation;
import cn.sourceplan.common.mapper.SysUserStationMapper;
import cn.sourceplan.common.utils.SecurityUtils;
import cn.sourceplan.common.utils.SqlCheckUtil;
import cn.sourceplan.production.domain.RouteProcess;
import cn.sourceplan.production.domain.WorkOrder;
import cn.sourceplan.production.domain.WorkOrderEntry;
import cn.sourceplan.production.mapper.WorkOrderEntryMapper;
import cn.sourceplan.production.mapper.WorkOrderMapper;
import cn.sourceplan.production.service.IRouteService;
import cn.sourceplan.production.service.IWorkOrderService;
import cn.sourceplan.quality.domain.ReportQuality;
import cn.sourceplan.system.service.ISysConfigService;
import cn.sourceplan.warehouse.domain.ProPick;
import cn.sourceplan.warehouse.domain.WarehouseStation;
import cn.sourceplan.warehouse.mapper.ProPickMapper;
import cn.sourceplan.warehouse.service.IWarehouseStationService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.barcode.mapper.MaterialSnMapper;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.production.mapper.ReportMapper;
import cn.sourceplan.production.domain.Report;
import cn.sourceplan.production.service.IReportService;

/**
 * 报工单Service业务层处理
 *
 * @author jinzhong
 * @date 2023-02-15
 */
@Service
public class ReportServiceImpl implements IReportService {
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    @Autowired
    private MaterialSnMapper materialSnMapper;

    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private WorkOrderEntryMapper workOrderEntryMapper;

    @Autowired
    private ProPickMapper proPickMapper;

    @Autowired
    private IWorkOrderService workOrderService;

    @Autowired
    private IRouteService routeService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IWarehouseStationService warehouseStationService;
    @Autowired
    private SysUserStationMapper sysUserStationMapper;
    /**
     * 查询报工单
     *
     * @param id 报工单主键
     * @return 报工单
     */
    @Override
    public Report selectReportById(Long id) {
        return reportMapper.selectById(id);
    }

    /**
     * 查询报工单列表
     *
     * @param report 报工单
     * @return 报工单
     */
    @Override
    public List<Report> selectReportList(Report report) {

        return reportMapper.selectReportList(report);
    }

    /**
     * 新增报工单
     *
     * @param report 报工单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertReport(Report report) {
        Long workOrderEntryId = report.getWorkOrderEntryId();
        WorkOrderEntry workOrderEntry = workOrderEntryMapper.selectById(workOrderEntryId);

        //本道工序有领料任务，但未进行领料操作不允许报工
        Integer processSort = workOrderEntry.getProcessSort();
        QueryWrapper<WorkOrderEntry> woeQw = new QueryWrapper<>();
        woeQw.eq("workorder_id", workOrderEntry.getWorkorderId())
                .eq("process_sort", processSort)
                .isNotNull("pick_material_id");

        //定制化功能begin
            //并且领料单的规格型号的长*宽与当前报工规格型号相同
            //截取当前领料的长宽
            String reportSpecification = workOrderEntry.getReportSpecification();
            String[] parts = reportSpecification.split("\\*"); // 使用*作为分隔符，需要使用\\*转义*
            for (String part : parts) {
                if (part.endsWith("mm") ) {
                    woeQw.like("pick_specification",part);
                }
                if(part.endsWith("m")){
                    woeQw.like("pick_specification",part);
                }
            }
        //定制化功能end

        //查对应工单，
        List<WorkOrderEntry> woeList = workOrderEntryMapper.selectList(woeQw);
        Boolean isPcik = false;
        if (woeList != null && woeList.size() > 0) {
            //查对应领料单
            for (int i = 0; i < woeList.size(); i++) {
                WorkOrderEntry woe = woeList.get(i);
                QueryWrapper<ProPick> ppQw = new QueryWrapper<>();
                ppQw.eq("work_order_entry_id", woe.getId());
                List<ProPick> proPicks = proPickMapper.selectList(ppQw);
                if (proPicks == null || proPicks.size() == 0) {
                    isPcik = true;
                }
            }
        }
        //没有领料
        if (isPcik) {
            return 0;
        }

        //如果编号为null,则自动为其生成编号
        if (StringUtils.isBlank(report.getNumber())) {
            SysCodeRule queryDomain = new SysCodeRule();
            queryDomain.setBasicDomain("Report");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(), true);
            report.setNumber(number);
        }
        int rows = reportMapper.insert(report);
        //更新SN码追溯表中的内容
        String snArray = report.getSnArray();
        if (StringUtils.isNotBlank(snArray)) {
            int i = snArray.lastIndexOf(",");
            snArray = snArray.substring(0, i);
            String SnContent = DateUtil.now() + "： " + report.getReportUserName() + "-进行报工操作(" + report.getNumber() + ")<br/> ";
            Map<String, Object> map = new HashMap<>();
            map.put("content", SnContent);
            map.put("whereSql", " and sn_code in(" + snArray + ")");
            materialSnMapper.updateBatchSomeColumn(map);
        }

        //根据工单明细ID，查询所有报工单，数量之和大于等于工单明细中的数量，则将状态改为已完成
        BigDecimal planQ = workOrderEntry.getReportQuantity();
        Report reportQuery = new Report();
        reportQuery.setWorkOrderEntryId(workOrderEntryId);
        List<Report> reports = reportMapper.selectReportList(reportQuery);
        for (int i = 0; i < reports.size(); i++) {
            Report rep = reports.get(i);
            BigDecimal reportQ = rep.getReportQuantity();
            planQ = planQ.subtract(reportQ);
        }
        if (planQ.compareTo(new BigDecimal(0)) == 0 || planQ.compareTo(new BigDecimal(0)) == -1) {
            workOrderEntry.setStatus("B");
            workOrderEntryMapper.updateById(workOrderEntry);
        }

        //将报工记录生成为下到工序的领料
        WorkOrderEntry newWoe = new WorkOrderEntry();
        newWoe.setPickMaterialId(report.getMaterialId());
        newWoe.setPickMaterialName(report.getMaterialName());
        newWoe.setPickQuantity(report.getReportQuantity());
        newWoe.setPickSpecification(report.getSpecification());
        newWoe.setId(null);
        //获取主表销售订单id相同；并且分录为下道工序的workorder ，作为主键
        WorkOrder workOrder = workOrderService.selectWorkOrderById(workOrderEntry.getWorkorderId());
        String sourceInfo = workOrder.getSourceInfo();
        JSONObject sourceInfoJson = JSONObject.parseObject(sourceInfo);
        // 获取JSONArray
        JSONArray saleOrderEntryArr = sourceInfoJson.getJSONArray("saleOrderEntryId");
        // 将JSONArray转换为List<Long>
        List<Long> lList = saleOrderEntryArr.toJavaList(Long.class);
        // 将List<Long>转换为Long数组
        Long[] lArr = lList.toArray(new Long[0]);
        String sql = SqlCheckUtil.querySourceInfo("saleOrderEntryId", lArr);
        QueryWrapper<WorkOrder> WOqw = new QueryWrapper<>();
        WOqw.apply(" 1=1 "+sql);
        WOqw.orderByAsc("id");
        List<WorkOrder> workOrders = workOrderMapper.selectList(WOqw);
        RouteProcess process = null ;
        outerLoop:
        for (WorkOrder workOrder1 : workOrders) {
            QueryWrapper<WorkOrderEntry> woeQW= new QueryWrapper<>();
            woeQW.eq("workorder_id",workOrder1.getId());
            List<WorkOrderEntry> workOrderEntries = workOrderEntryMapper.selectList(woeQW);
            if(workOrderEntries==null|| workOrderEntries.isEmpty())continue;
            for (WorkOrderEntry workOrderEntry1 : workOrderEntries) {
                process =routeService.getProcessBySort(workOrder.getRouteId(), processSort + 1);
                if(process!=null){
                    if (Objects.equals(workOrderEntry1.getProcessId(), process.getProcessId())) {
                        newWoe.setWorkorderId(workOrder1.getId());
                        newWoe.setProcessId(process.getId());
                        newWoe.setProcessName(process.getProcessName());
                        break outerLoop;
                    }
                }
                //天润项目定制化功能begin
                //向下多找一级
                process = routeService.getProcessBySort(workOrder.getRouteId(), processSort + 2);
                if(process!=null){
                    if (Objects.equals(workOrderEntry1.getProcessId(), process.getProcessId())) {
                        newWoe.setWorkorderId(workOrder1.getId());
                        newWoe.setProcessId(process.getId());
                        newWoe.setProcessName(process.getProcessName());
                        break outerLoop;
                    }
                }
                //天润项目定制化功能end
            }
        }

        if(process==null){
            return rows;
        }
        newWoe.setStationId(null);
        newWoe.setStationName(null);
        newWoe.setMachineId(null);
        newWoe.setMachineName(null);
        newWoe.setType("pick");
        newWoe.setProcessSort(processSort+1);
        if(newWoe.getWorkorderId()!=null){
            //保存领料单
            workOrderEntryMapper.insert(newWoe);
        }
        return rows;
    }

    /**
     * 修改报工单
     *
     * @param report 报工单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult updateReport(Report report) {
        int i = reportMapper.updateById(report);
        return i > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 批量删除报工单
     *
     * @param ids 需要删除的报工单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult deleteReportByIds(Long[] ids) {
        int i = reportMapper.deleteReportByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }


}
