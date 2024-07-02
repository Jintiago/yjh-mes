package cn.sourceplan.equipment.service;

import java.util.List;
import cn.sourceplan.equipment.domain.InspectionItem;

/**
 * 设备项目Service接口
 * 
 * @author jinzhong
 * @date 2023-04-08
 */
public interface IInspectionItemService 
{
    /**
     * 查询设备项目
     * 
     * @param id 设备项目主键
     * @return 设备项目
     */
    public InspectionItem selectInspectionItemById(Long id);

    /**
     * 查询设备项目列表
     * 
     * @param inspectionItem 设备项目
     * @return 设备项目集合
     */
    public List<InspectionItem> selectInspectionItemList(InspectionItem inspectionItem);

    /**
     * 新增设备项目
     * 
     * @param inspectionItem 设备项目
     * @return 结果
     */
    public int insertInspectionItem(InspectionItem inspectionItem);

    /**
     * 修改设备项目
     * 
     * @param inspectionItem 设备项目
     * @return 结果
     */
    public int updateInspectionItem(InspectionItem inspectionItem);

    /**
     * 批量删除设备项目
     * 
     * @param ids 需要删除的设备项目主键集合
     * @return 结果
     */
    public int deleteInspectionItemByIds(Long[] ids);

    /**
     * 删除设备项目信息
     * 
     * @param id 设备项目主键
     * @return 结果
     */
    public int deleteInspectionItemById(Long id);
}
