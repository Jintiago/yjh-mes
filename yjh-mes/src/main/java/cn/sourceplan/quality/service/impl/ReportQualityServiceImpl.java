package cn.sourceplan.quality.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.DateUtil;
import cn.sourceplan.production.domain.Report;
import cn.sourceplan.production.mapper.ReportMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.barcode.mapper.MaterialSnMapper;
import cn.sourceplan.common.utils.StringUtils;
import cn.sourceplan.quality.domain.ReportQualityEntry;
import cn.sourceplan.quality.domain.ReportQualityReason;
import cn.sourceplan.quality.mapper.ReportQualityEntryMapper;
import cn.sourceplan.quality.mapper.ReportQualityReasonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.quality.mapper.ReportQualityMapper;
import cn.sourceplan.quality.domain.ReportQuality;
import cn.sourceplan.quality.service.IReportQualityService;

/**
 * 报工质检单Service业务层处理
 *
 * @author jinzhong
 * @date 2023-02-21
 */
@Service
public class ReportQualityServiceImpl implements IReportQualityService
{
    @Autowired
    private ReportQualityMapper reportQualityMapper;
    @Autowired
    private ReportQualityEntryMapper reportQualityEntryMapper;

    @Autowired
    private ReportQualityReasonMapper reportQualityReasonMapper;

    @Autowired
    private MaterialSnMapper materialSnMapper;

    @Autowired
    private ReportMapper reportMapper;

    /**
     * 查询报工质检单
     *
     * @param id 报工质检单主键
     * @return 报工质检单
     */
    @Override
    public ReportQuality selectReportQualityById(Long id)
    {
            ReportQuality reportQuality = reportQualityMapper.selectById(id);
            QueryWrapper<ReportQualityEntry> qw = new QueryWrapper<>();
            qw.eq("main_id",reportQuality.getId());
            List<ReportQualityEntry> reportQualityEntries = reportQualityEntryMapper.selectList(qw);
            for (int j = 0; j < reportQualityEntries.size(); j++) {
                ReportQualityEntry reportQualityEntry = reportQualityEntries.get(j);
                QueryWrapper<ReportQualityReason> qwB = new QueryWrapper<>();
                qwB.eq("main_id",reportQualityEntry.getId());
                List<ReportQualityReason> reportQualityReasons = reportQualityReasonMapper.selectList(qwB);
                reportQualityEntry.setReasonList(reportQualityReasons);
            }
            reportQuality.setQualityLevelList(reportQualityEntries);
        return reportQuality;
    }

    /**
     * 查询报工质检单列表
     *
     * @param rqQuery 报工质检单
     * @return 报工质检单
     */
    @Override
    public List<ReportQuality> selectReportQualityList(ReportQuality rqQuery)
    {
        QueryWrapper<ReportQuality> qwA = new QueryWrapper<>(rqQuery);

        String reportIdsSql = null;
        String reportIds = rqQuery.getReportIds();
        if(org.apache.commons.lang3.StringUtils.isNotBlank(reportIds)) {
            reportIdsSql =
                    "  (report_ids = " + reportIds + " or " +
                            "report_ids LIKE '%," + reportIds + "' or " +
                            "report_ids LIKE '" + reportIds + ",%' or " +
                            "report_ids LIKE '%," + reportIds + ",%' ) ";
            rqQuery.setReportIds(null);
            qwA.apply(reportIdsSql);
        }
        qwA.orderByDesc("check_time");
        List<ReportQuality> reportQualities = reportQualityMapper.selectList(qwA);

        for (int i = 0; i < reportQualities.size(); i++) {
            ReportQuality reportQuality = reportQualities.get(i);
            QueryWrapper<ReportQualityEntry> qw = new QueryWrapper<>();
            qw.eq("main_id",reportQuality.getId());
            List<ReportQualityEntry> reportQualityEntries = reportQualityEntryMapper.selectList(qw);
            for (int j = 0; j < reportQualityEntries.size(); j++) {
                ReportQualityEntry reportQualityEntry = reportQualityEntries.get(j);
                QueryWrapper<ReportQualityReason> qwB = new QueryWrapper<>();
                qwB.eq("main_id",reportQualityEntry.getId());
                List<ReportQualityReason> reportQualityReasons = reportQualityReasonMapper.selectList(qwB);
                reportQualityEntry.setReasonList(reportQualityReasons);
            }
            reportQuality.setQualityLevelList(reportQualityEntries);
        }

        return reportQualities;
    }

    /**
     * 新增报工质检单
     *
     * @param reportQuality 报工质检单
     * @return 结果
     */
    @Override
    public int insertReportQuality(ReportQuality reportQuality)
    {

        int i = reportQualityMapper.insert(reportQuality);
        //更新SN码追溯表中的内容
        String snArray = reportQuality.getSnArray();
        if(StringUtils.isNotBlank(snArray)){
            int l = snArray.lastIndexOf(",");
            snArray = snArray.substring(0,l);
            String SnContent = DateUtil.now()+"： "+reportQuality.getCheckUserName()+"-进行质检操作("+reportQuality.getNumber()+")<br/> ";
            Map<String,Object> map = new HashMap<>();
            map.put("content",SnContent);
            map.put("whereSql", " and sn_code in(" +snArray+")");
            materialSnMapper.updateBatchSomeColumn(map);
        }

        //插入等级表
        List<ReportQualityEntry> qualityLevelList = reportQuality.getQualityLevelList();
        for (int j = 0; j < qualityLevelList.size(); j++) {
            ReportQualityEntry qualityLevel = qualityLevelList.get(j);
            qualityLevel.setMainId(reportQuality.getId());
            //调用Maperr层插入质检等级表
            reportQualityEntryMapper.insert(qualityLevel);

            List<ReportQualityReason> reasonList = qualityLevel.getReasonList();
            if(reasonList!=null&&reasonList.size()>0){
                for (int k = 0; k < reasonList.size(); k++) {
                    ReportQualityReason reason = reasonList.get(k);
                    reason.setMainId(qualityLevel.getId());
                }
                reportQualityReasonMapper.insertBatchSomeColumn(reasonList);
            }

        }

        BigDecimal unqualifiedQuantity = reportQuality.getUnqualifiedQuantity();
        //回写报工单状态为已质检 C检验通过、D检验异常
        String reportStatus ="";
        String reportIds = reportQuality.getReportIds();
        if(unqualifiedQuantity.compareTo(new BigDecimal(0))==1){
            //检验异常
            reportStatus="D";
        }else{
            //检验通过
            reportStatus="C";
        }
        //回写报工单状态
        String[] split = reportIds.split(",");
        for (int j = 0; j < split.length; j++) {
            Long id = Long.parseLong(split[j]) ;
            Report report = reportMapper.selectById(id);
            report.setQualityStatus(reportStatus);
            reportMapper.updateById(report);
        }

        return i;
    }

    /**
     * 修改报工质检单
     *
     * @param reportQuality 报工质检单
     * @return 结果
     */
    @Override
    public int updateReportQuality(ReportQuality reportQuality)
    {
        return reportQualityMapper.updateById(reportQuality);
    }

    /**
     * 批量删除报工质检单
     *
     * @param ids 需要删除的报工质检单主键
     * @return 结果
     */
    @Override
    public int deleteReportQualityByIds(Long[] ids)
    {
        return reportQualityMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除报工质检单信息
     *
     * @param id 报工质检单主键
     * @return 结果
     */
    @Override
    public int deleteReportQualityById(Long id)
    {
        return reportQualityMapper.deleteById(id);
    }
}
