package cn.sourceplan.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.equipment.domain.RepairOrder;
import org.springframework.stereotype.Repository;

/**
 * 设备维修单Mapper接口
 *
 * @author jinzhong
 * @date 2023-04-08
 */
@Repository
public interface RepairOrderMapper extends BaseMapper<RepairOrder>
{
    /**
     * 查询设备维修单
     *
     * @param id 设备维修单主键
     * @return 设备维修单
     */
    public RepairOrder selectRepairOrderById(Long id);

    /**
     * 删除设备维修单
     *
     * @param id 设备维修单主键
     * @return 结果
     */
    public int deleteRepairOrderById(Long id);

    /**
     * 批量删除设备维修单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRepairOrderByIds(Long[] ids);

    /**
     * 批量删除设备维修单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRepairOrderEntryByMainIds(Long[] ids);

    /**
     * 通过设备维修单主键删除设备维修单明细信息
     *
     * @param id 设备维修单ID
     * @return 结果
     */
    public int deleteRepairOrderEntryByMainId(Long id);
}
