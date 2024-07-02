package cn.sourceplan.warehouse.service.impl;

import java.util.Arrays;
import java.util.List;

import cn.sourceplan.warehouse.domain.Warehouse;
import cn.sourceplan.warehouse.mapper.WarehouseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.warehouse.mapper.WarehouseStationMapper;
import cn.sourceplan.warehouse.domain.WarehouseStation;
import cn.sourceplan.warehouse.service.IWarehouseStationService;

/**
 * 仓库工位绑定Service业务层处理
 *
 * @author jinzhong
 * @date 2023-11-04
 */
@Service
public class WarehouseStationServiceImpl implements IWarehouseStationService
{
    @Autowired
    private WarehouseStationMapper warehouseStationMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;
    /**
     * 查询仓库工位绑定
     *
     * @param id 仓库工位绑定主键
     * @return 仓库工位绑定
     */
    @Override
    public WarehouseStation selectWarehouseStationById(Long id)
    {
        return warehouseStationMapper.selectById(id);
    }

    /**
     * 查询仓库工位绑定列表
     *
     * @param warehouseStation 仓库工位绑定
     * @return 仓库工位绑定
     */
    @Override
    public List<WarehouseStation> selectWarehouseStationList(WarehouseStation warehouseStation)
    {
        QueryWrapper<WarehouseStation> qw = new QueryWrapper<>(warehouseStation);
        List<WarehouseStation> warehouseStations = warehouseStationMapper.selectList(qw);
        for (int i = 0; i < warehouseStations.size(); i++) {
            WarehouseStation ws = warehouseStations.get(i);
            Warehouse warehouse = warehouseMapper.selectById(ws.getWarehouseId());
            ws.setWarehouse(warehouse);
        }
        return warehouseStations;
    }

    /**
     * 新增仓库工位绑定
     *
     * @param warehouseStation 仓库工位绑定
     * @return 结果
     */
    @Override
    public int insertWarehouseStation(WarehouseStation warehouseStation)
    {
        return warehouseStationMapper.insert(warehouseStation);
    }

    /**
     * 修改仓库工位绑定
     *
     * @param warehouseStation 仓库工位绑定
     * @return 结果
     */
    @Override
    public int updateWarehouseStation(WarehouseStation warehouseStation)
    {
        return warehouseStationMapper.updateById(warehouseStation);
    }

    /**
     * 批量删除仓库工位绑定
     *
     * @param ids 需要删除的仓库工位绑定主键
     * @return 结果
     */
    @Override
    public int deleteWarehouseStationByIds(Long[] ids)
    {
        return warehouseStationMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除仓库工位绑定信息
     *
     * @param id 仓库工位绑定主键
     * @return 结果
     */
    @Override
    public int deleteWarehouseStationById(Long id)
    {
        return warehouseStationMapper.deleteById(id);
    }
}
