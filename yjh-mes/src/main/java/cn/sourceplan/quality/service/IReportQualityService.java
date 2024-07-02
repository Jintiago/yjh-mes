package cn.sourceplan.quality.service;

import java.util.List;
import cn.sourceplan.quality.domain.ReportQuality;

/**
 * 报工质检单Service接口
 * 
 * @author jinzhong
 * @date 2023-02-21
 */
public interface IReportQualityService 
{
    /**
     * 查询报工质检单
     * 
     * @param id 报工质检单主键
     * @return 报工质检单
     */
    public ReportQuality selectReportQualityById(Long id);

    /**
     * 查询报工质检单列表
     * 
     * @param reportQuality 报工质检单
     * @return 报工质检单集合
     */
    public List<ReportQuality> selectReportQualityList(ReportQuality reportQuality);

    /**
     * 新增报工质检单
     * 
     * @param reportQuality 报工质检单
     * @return 结果
     */
    public int insertReportQuality(ReportQuality reportQuality);

    /**
     * 修改报工质检单
     * 
     * @param reportQuality 报工质检单
     * @return 结果
     */
    public int updateReportQuality(ReportQuality reportQuality);

    /**
     * 批量删除报工质检单
     * 
     * @param ids 需要删除的报工质检单主键集合
     * @return 结果
     */
    public int deleteReportQualityByIds(Long[] ids);

    /**
     * 删除报工质检单信息
     * 
     * @param id 报工质检单主键
     * @return 结果
     */
    public int deleteReportQualityById(Long id);
}
