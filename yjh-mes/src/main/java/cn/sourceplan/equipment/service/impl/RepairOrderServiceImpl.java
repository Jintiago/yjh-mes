package cn.sourceplan.equipment.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.equipment.mapper.RepairOrderEntryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.equipment.domain.RepairOrderEntry;
import cn.sourceplan.equipment.mapper.RepairOrderMapper;
import cn.sourceplan.equipment.domain.RepairOrder;
import cn.sourceplan.equipment.service.IRepairOrderService;

/**
 * 设备维修单Service业务层处理
 *
 * @author jinzhong
 * @date 2023-04-08
 */
@Service
public class RepairOrderServiceImpl implements IRepairOrderService
{
    @Autowired
    private RepairOrderMapper repairOrderMapper;

    @Autowired
    private RepairOrderEntryMapper repairOrderEntryMapper;

    /**
     * 查询设备维修单
     *
     * @param id 设备维修单主键
     * @return 设备维修单
     */
    @Override
    public RepairOrder selectRepairOrderById(Long id)
    {
        return repairOrderMapper.selectRepairOrderById(id);
    }

    /**
     * 查询设备维修单列表
     *
     * @param repairOrder 设备维修单
     * @return 设备维修单
     */
    @Override
    public List<RepairOrder> selectRepairOrderList(RepairOrder repairOrder)
    {
        QueryWrapper<RepairOrder> qw = new QueryWrapper<>(repairOrder);
        return repairOrderMapper.selectList(qw);
    }

    /**
     * 新增设备维修单
     *
     * @param repairOrder 设备维修单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertRepairOrder(RepairOrder repairOrder)
    {
        int rows = repairOrderMapper.insert(repairOrder);
        insertRepairOrderEntry(repairOrder);
        return rows;
    }

    /**
     * 修改设备维修单
     *
     * @param repairOrder 设备维修单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateRepairOrder(RepairOrder repairOrder)
    {
        repairOrderMapper.deleteRepairOrderEntryByMainId(repairOrder.getId());
        insertRepairOrderEntry(repairOrder);
        return repairOrderMapper.updateById(repairOrder);
    }

    /**
     * 批量删除设备维修单
     *
     * @param ids 需要删除的设备维修单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteRepairOrderByIds(Long[] ids)
    {
        repairOrderMapper.deleteRepairOrderEntryByMainIds(ids);
        return repairOrderMapper.deleteRepairOrderByIds(ids);
    }

    /**
     * 删除设备维修单信息
     *
     * @param id 设备维修单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteRepairOrderById(Long id)
    {
        repairOrderMapper.deleteRepairOrderEntryByMainId(id);
        return repairOrderMapper.deleteRepairOrderById(id);
    }

    /**
     * 新增设备维修单明细信息
     *
     * @param repairOrder 设备维修单对象
     */
    public void insertRepairOrderEntry(RepairOrder repairOrder)
    {
        List<RepairOrderEntry> repairOrderEntryList = repairOrder.getRepairOrderEntryList();
        Long id = repairOrder.getId();
        if (StringUtils.isNotNull(repairOrderEntryList))
        {
            List<RepairOrderEntry> list = new ArrayList<RepairOrderEntry>();
            for (RepairOrderEntry repairOrderEntry : repairOrderEntryList)
            {
                repairOrderEntry.setMainId(id);
                list.add(repairOrderEntry);
            }
            if (list.size() > 0)
            {
                repairOrderEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }
}
