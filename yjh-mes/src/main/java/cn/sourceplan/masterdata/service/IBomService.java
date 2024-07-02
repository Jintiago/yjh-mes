package cn.sourceplan.masterdata.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.masterdata.domain.Bom;

/**
 * 物料BOMService接口
 *
 * @author jinzhong
 * @date 2022-09-16
 */
public interface IBomService
{
    /**
     * 查询物料BOM
     *
     * @param id 物料BOM主键
     * @return 物料BOM
     */
    public Bom selectBomById(Long id);

    /**
     * 查询物料BOM列表
     *
     * @param bom 物料BOM
     * @return 物料BOM集合
     */
    public List<Bom> selectBomList(Bom bom);

    /**
     * 新增物料BOM
     *
     * @param bom 物料BOM
     * @return 结果
     */
    public AjaxResult insertBom(Bom bom);

    /**
     * 修改物料BOM
     *
     * @param bom 物料BOM
     * @return 结果
     */
    public int updateBom(Bom bom);

    /**
     * 批量删除物料BOM
     *
     * @param ids 需要删除的物料BOM主键集合
     * @return 结果
     */
    public int deleteBomByIds(Long[] ids);

    /**
     * 删除物料BOM信息
     *
     * @param id 物料BOM主键
     * @return 结果
     */
    public int deleteBomById(Long id);
}
