package cn.sourceplan.warehouse.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.warehouse.domain.Warehouse;

/**
 * 仓库信息Service接口
 *
 * @author jinzhong
 * @date 2022-11-03
 */
public interface IWarehouseService
{
    /**
     * 查询仓库信息
     *
     * @param id 仓库信息主键
     * @return 仓库信息
     */
    public Warehouse selectWarehouseById(Long id);

    /**
     * 查询仓库信息列表
     *
     * @param warehouse 仓库信息
     * @return 仓库信息集合
     */
    public List<Warehouse> selectWarehouseList(Warehouse warehouse);

    /**
     * 新增仓库信息
     *
     * @param warehouse 仓库信息
     * @return 结果
     */
    public int insertWarehouse(Warehouse warehouse);

    /**
     * 修改仓库信息
     *
     * @param warehouse 仓库信息
     * @return 结果
     */
    public AjaxResult updateWarehouse(Warehouse warehouse);

    /**
     * 批量删除仓库信息
     *
     * @param ids 需要删除的仓库信息主键集合
     * @return 结果
     */
    public AjaxResult deleteWarehouseByIds(Long[] ids);

}
