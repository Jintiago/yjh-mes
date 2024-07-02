package cn.sourceplan.warehouse.service.impl;

import java.util.List;

import cn.sourceplan.warehouse.domain.Warehouse;
import cn.sourceplan.warehouse.mapper.WarehouseMapper;
import cn.sourceplan.warehouse.service.IWarehouseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.SqlCheckUtil;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 仓库信息Service业务层处理
 *
 * @author jinzhong
 * @date 2022-11-03
 */
@Service
public class WarehouseServiceImpl implements IWarehouseService
{
    @Autowired
    private WarehouseMapper warehouseMapper;

    /**
     * 查询仓库信息
     *
     * @param id 仓库信息主键
     * @return 仓库信息
     */
    @Override
    public Warehouse selectWarehouseById(Long id)
    {
        return warehouseMapper.selectById(id);
    }

    /**
     * 查询仓库信息列表
     *
     * @param warehouse 仓库信息
     * @return 仓库信息
     */
    @Override
    public List<Warehouse> selectWarehouseList(Warehouse warehouse)
    {
        QueryWrapper<Warehouse> qw = new QueryWrapper<>(warehouse);
        return warehouseMapper.selectList(qw);
    }

    /**
     * 新增仓库信息
     *
     * @param warehouse 仓库信息
     * @return 结果
     */
    @Override
    public int insertWarehouse(Warehouse warehouse)
    {
        return warehouseMapper.insert(warehouse);
    }

    /**
     * 修改仓库信息
     *
     * @param warehouse 仓库信息
     * @return 结果
     */
    @Override
    public AjaxResult updateWarehouse(Warehouse warehouse)
    {
        //校验数据引用关系
        //生产领料
        String ppeCheck = SqlCheckUtil.sqlCheck("number","wm_production_pick",
                "where warehouse_id =  "+warehouse.getId()+"  and status ='A'");
        if(ppeCheck!=null){
            return AjaxResult.error("该数据在生产领料单["+ppeCheck+"]中已被引用，无法修改");
        }
        //完工入库
        String mieCheck = SqlCheckUtil.sqlCheck("number","wm_manufacture_into",
                "where warehouse_id =  "+warehouse.getId()+"  and status ='A'");
        if(mieCheck!=null){
            return AjaxResult.error("该数据在完工入库单["+mieCheck+"]中已被引用，无法修改");
        }
        int i = warehouseMapper.updateById(warehouse);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 批量删除仓库信息
     *
     * @param ids 需要删除的仓库信息主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteWarehouseByIds(Long[] ids)
    {
        String str = StringUtils.join(ids, ",");
        //校验数据引用关系
        //生产领料
        String ppeCheck = SqlCheckUtil.sqlCheck("number","wm_production_pick",
                "where warehouse_id in(  "+str+" ) and status ='A'");
        if(ppeCheck!=null){
            return AjaxResult.error("所选数据在生产领料单["+ppeCheck+"]中已被引用，无法删除");
        }
        //完工入库
        String mieCheck = SqlCheckUtil.sqlCheck("number","wm_manufacture_into",
                "where warehouse_id in(   "+str+" ) and status ='A'");
        if(mieCheck!=null){
            return AjaxResult.error("所选数据在完工入库单["+mieCheck+"]中已被引用，无法删除");
        }
        int i = warehouseMapper.deleteWarehouseByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

}
