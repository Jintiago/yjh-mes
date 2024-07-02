package cn.sourceplan.equipment.service;

import java.util.List;
import cn.sourceplan.equipment.domain.RepairOrder;

/**
 * 设备维修单Service接口
 * 
 * @author jinzhong
 * @date 2023-04-08
 */
public interface IRepairOrderService 
{
    /**
     * 查询设备维修单
     * 
     * @param id 设备维修单主键
     * @return 设备维修单
     */
    public RepairOrder selectRepairOrderById(Long id);

    /**
     * 查询设备维修单列表
     * 
     * @param repairOrder 设备维修单
     * @return 设备维修单集合
     */
    public List<RepairOrder> selectRepairOrderList(RepairOrder repairOrder);

    /**
     * 新增设备维修单
     * 
     * @param repairOrder 设备维修单
     * @return 结果
     */
    public int insertRepairOrder(RepairOrder repairOrder);

    /**
     * 修改设备维修单
     * 
     * @param repairOrder 设备维修单
     * @return 结果
     */
    public int updateRepairOrder(RepairOrder repairOrder);

    /**
     * 批量删除设备维修单
     * 
     * @param ids 需要删除的设备维修单主键集合
     * @return 结果
     */
    public int deleteRepairOrderByIds(Long[] ids);

    /**
     * 删除设备维修单信息
     * 
     * @param id 设备维修单主键
     * @return 结果
     */
    public int deleteRepairOrderById(Long id);
}
