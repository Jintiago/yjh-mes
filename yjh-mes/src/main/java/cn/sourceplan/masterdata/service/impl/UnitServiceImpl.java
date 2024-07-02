package cn.sourceplan.masterdata.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.SqlCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.masterdata.mapper.UnitMapper;
import cn.sourceplan.masterdata.domain.Unit;
import cn.sourceplan.masterdata.service.IUnitService;

/**
 * 计量单位Service业务层处理
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Service
public class UnitServiceImpl implements IUnitService
{
    @Autowired
    private UnitMapper unitMapper;

    /**
     * 查询计量单位
     *
     * @param id 计量单位主键
     * @return 计量单位
     */
    @Override
    public Unit selectUnitById(Long id)
    {
        return unitMapper.selectById(id);
    }

    /**
     * 查询计量单位列表
     *
     * @param unit 计量单位
     * @return 计量单位
     */
    @Override
    public List<Unit> selectUnitList(Unit unit)
    {
        Wrapper<Unit> w = new QueryWrapper<>(unit);
        return unitMapper.selectList(w);
    }

    /**
     * 新增计量单位
     *
     * @param unit 计量单位
     * @return 结果
     */
    @Override
    public int insertUnit(Unit unit)
    {
        return unitMapper.insertUnit(unit);
    }

    /**
     * 修改计量单位
     *
     * @param unit 计量单位
     * @return 结果
     */
    @Override
    public AjaxResult updateUnit(Unit unit)
    {
        String result = SqlCheckUtil.sqlCheck("number","md_material",
                "where unit_id = "+unit.getId()+" and status ='0'");
        if(result!=null){
            return AjaxResult.error("该数据在物料["+ result +"]中已被引用，无法修改");
        }
        int i = unitMapper.updateUnit(unit);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 批量删除计量单位
     *
     * @param ids 需要删除的计量单位主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteUnitByIds(Long[] ids)
    {
        String result  = SqlCheckUtil.sqlCheck("number", "md_material",
                "where unit_id in ("+ StringUtils.join(ids,",")+") and status ='0'");
        if(result!=null){
            return AjaxResult.error("所选数据在物料["+ result +"]中已被引用，无法删除");
        }
        int i = unitMapper.deleteUnitByIds(ids);
        return  i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

}
