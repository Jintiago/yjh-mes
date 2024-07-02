package cn.sourceplan.equipment.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.equipment.mapper.InspectionItemMapper;
import cn.sourceplan.equipment.domain.InspectionItem;
import cn.sourceplan.equipment.service.IInspectionItemService;

/**
 * 设备项目Service业务层处理
 *
 * @author jinzhong
 * @date 2023-04-08
 */
@Service
public class InspectionItemServiceImpl implements IInspectionItemService
{
    @Autowired
    private InspectionItemMapper inspectionItemMapper;

    /**
     * 查询设备项目
     *
     * @param id 设备项目主键
     * @return 设备项目
     */
    @Override
    public InspectionItem selectInspectionItemById(Long id)
    {
        return inspectionItemMapper.selectById(id);
    }

    /**
     * 查询设备项目列表
     *
     * @param inspectionItem 设备项目
     * @return 设备项目
     */
    @Override
    public List<InspectionItem> selectInspectionItemList(InspectionItem inspectionItem)
    {
        QueryWrapper<InspectionItem> qw = new QueryWrapper<>(inspectionItem);
        return inspectionItemMapper.selectList(qw);
    }

    /**
     * 新增设备项目
     *
     * @param inspectionItem 设备项目
     * @return 结果
     */
    @Override
    public int insertInspectionItem(InspectionItem inspectionItem)
    {
        return inspectionItemMapper.insert(inspectionItem);
    }

    /**
     * 修改设备项目
     *
     * @param inspectionItem 设备项目
     * @return 结果
     */
    @Override
    public int updateInspectionItem(InspectionItem inspectionItem)
    {
        return inspectionItemMapper.updateById(inspectionItem);
    }

    /**
     * 批量删除设备项目
     *
     * @param ids 需要删除的设备项目主键
     * @return 结果
     */
    @Override
    public int deleteInspectionItemByIds(Long[] ids)
    {
        return inspectionItemMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除设备项目信息
     *
     * @param id 设备项目主键
     * @return 结果
     */
    @Override
    public int deleteInspectionItemById(Long id)
    {
        return inspectionItemMapper.deleteById(id);
    }
}
