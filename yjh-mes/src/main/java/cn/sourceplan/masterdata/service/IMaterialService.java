package cn.sourceplan.masterdata.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.masterdata.domain.Material;

/**
 * 物料Service接口
 *
 * @author jinzhong
 * @date 2022-09-13
 */
public interface IMaterialService
{
    /**
     * 查询物料
     *
     * @param id 物料主键
     * @return 物料
     */
    Material selectMaterialById(Long id);

    /**
     * 查询物料列表
     *
     * @param material 物料
     * @return 物料集合
     */
    List<Material> selectMaterialList(Material material);

    /**
     * 新增物料
     *
     * @param material 物料
     * @return 结果
     */
    AjaxResult insertMaterial(Material material);

    /**
     * 修改物料
     *
     * @param material 物料
     * @return 结果
     */
    AjaxResult updateMaterial(Material material);

    /**
     * 批量删除物料
     *
     * @param ids 需要删除的物料主键集合
     * @return 结果
     */
    AjaxResult deleteMaterialByIds(Long[] ids);

}
