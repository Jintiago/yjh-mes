package cn.sourceplan.warehouse.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.warehouse.domain.ProfitLossRecord;

/**
 * 盘点盈亏记录Service接口
 *
 * @author jinzhong
 * @date 2023-07-20
 */
public interface IProfitLossRecordService
{
    /**
     * 查询盘点盈亏记录
     *
     * @param id 盘点盈亏记录主键
     * @return 盘点盈亏记录
     */
    public ProfitLossRecord selectProfitLossRecordById(Long id);

    /**
     * 查询盘点盈亏记录列表
     *
     * @param profitLossRecord 盘点盈亏记录
     * @return 盘点盈亏记录集合
     */
    public List<ProfitLossRecord> selectProfitLossRecordList(ProfitLossRecord profitLossRecord);

    /**
     * 新增盘点盈亏记录
     *
     * @param profitLossRecord 盘点盈亏记录
     * @return 结果
     */
    public int insertProfitLossRecord(ProfitLossRecord profitLossRecord);

    /**
     * 修改盘点盈亏记录
     *
     * @param profitLossRecord 盘点盈亏记录
     * @return 结果
     */
    public int updateProfitLossRecord(ProfitLossRecord profitLossRecord);

    /**
     * 批量删除盘点盈亏记录
     *
     * @param ids 需要删除的盘点盈亏记录主键集合
     * @return 结果
     */
    public int deleteProfitLossRecordByIds(Long[] ids);

    /**
     * 删除盘点盈亏记录信息
     *
     * @param id 盘点盈亏记录主键
     * @return 结果
     */
    public int deleteProfitLossRecordById(Long id);

    AjaxResult addByTakeInventory(Long id);
}
