package cn.sourceplan.masterdata.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.masterdata.domain.Unit;

/**
 * 计量单位Service接口
 *
 * @author jinzhong
 * @date 2022-09-13
 */
public interface IUnitService
{
    /**
     * 查询计量单位
     *
     * @param id 计量单位主键
     * @return 计量单位
     */
    public Unit selectUnitById(Long id);

    /**
     * 查询计量单位列表
     *
     * @param unit 计量单位
     * @return 计量单位集合
     */
    public List<Unit> selectUnitList(Unit unit);

    /**
     * 新增计量单位
     *
     * @param unit 计量单位
     * @return 结果
     */
    public int insertUnit(Unit unit);

    /**
     * 修改计量单位
     *
     * @param unit 计量单位
     * @return 结果
     */
    public AjaxResult updateUnit(Unit unit);

    /**
     * 批量删除计量单位
     *
     * @param ids 需要删除的计量单位主键集合
     * @return 结果
     */
    public AjaxResult deleteUnitByIds(Long[] ids);

}
