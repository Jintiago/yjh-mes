package cn.sourceplan.warehouse.service;

import java.util.List;

import cn.sourceplan.warehouse.domain.TakeInventory;

/**
 * 库存盘点Service接口
 *
 * @author jinzhong
 * @date 2023-07-20
 */
public interface ITakeInventoryService
{
    /**
     * 查询库存盘点
     *
     * @param id 库存盘点主键
     * @return 库存盘点
     */
    public TakeInventory selectTakeInventoryById(Long id);

    /**
     * 查询库存盘点列表
     *
     * @param takeInventory 库存盘点
     * @return 库存盘点集合
     */
    public List<TakeInventory> selectTakeInventoryList(TakeInventory takeInventory);

    /**
     * 新增库存盘点
     *
     * @param takeInventory 库存盘点
     * @return 结果
     */
    public int insertTakeInventory(TakeInventory takeInventory);

    /**
     * 修改库存盘点
     *
     * @param takeInventory 库存盘点
     * @return 结果
     */
    public int updateTakeInventory(TakeInventory takeInventory);

    /**
     * 批量删除库存盘点
     *
     * @param ids 需要删除的库存盘点主键集合
     * @return 结果
     */
    public int deleteTakeInventoryByIds(Long[] ids);

    /**
     * 删除库存盘点信息
     *
     * @param id 库存盘点主键
     * @return 结果
     */
    public int deleteTakeInventoryById(Long id);
}
