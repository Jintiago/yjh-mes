package cn.sourceplan.production.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.production.domain.Report;

/**
 * 报工单Service接口
 *
 * @author jinzhong
 * @date 2023-02-15
 */
public interface IReportService
{
    /**
     * 查询报工单
     *
     * @param id 报工单主键
     * @return 报工单
     */
    public Report selectReportById(Long id);

    /**
     * 查询报工单列表
     *
     * @param report 报工单
     * @return 报工单集合
     */
    public List<Report> selectReportList(Report report);

    /**
     * 新增报工单
     *
     * @param report 报工单
     * @return 结果
     */
    public int insertReport(Report report);

    /**
     * 修改报工单
     *
     * @param report 报工单
     * @return 结果
     */
    public AjaxResult updateReport(Report report);

    /**
     * 批量删除报工单
     *
     * @param ids 需要删除的报工单主键集合
     * @return 结果
     */
    public AjaxResult deleteReportByIds(Long[] ids);

}
