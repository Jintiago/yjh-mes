package cn.sourceplan.warehouse.service;

import java.util.List;
import cn.sourceplan.warehouse.domain.WarehouseStation;

/**
 * 仓库工位绑定Service接口
 * 
 * @author jinzhong
 * @date 2023-11-04
 */
public interface IWarehouseStationService 
{
    /**
     * 查询仓库工位绑定
     * 
     * @param id 仓库工位绑定主键
     * @return 仓库工位绑定
     */
    public WarehouseStation selectWarehouseStationById(Long id);

    /**
     * 查询仓库工位绑定列表
     * 
     * @param warehouseStation 仓库工位绑定
     * @return 仓库工位绑定集合
     */
    public List<WarehouseStation> selectWarehouseStationList(WarehouseStation warehouseStation);

    /**
     * 新增仓库工位绑定
     * 
     * @param warehouseStation 仓库工位绑定
     * @return 结果
     */
    public int insertWarehouseStation(WarehouseStation warehouseStation);

    /**
     * 修改仓库工位绑定
     * 
     * @param warehouseStation 仓库工位绑定
     * @return 结果
     */
    public int updateWarehouseStation(WarehouseStation warehouseStation);

    /**
     * 批量删除仓库工位绑定
     * 
     * @param ids 需要删除的仓库工位绑定主键集合
     * @return 结果
     */
    public int deleteWarehouseStationByIds(Long[] ids);

    /**
     * 删除仓库工位绑定信息
     * 
     * @param id 仓库工位绑定主键
     * @return 结果
     */
    public int deleteWarehouseStationById(Long id);
}
