package cn.sourceplan.masterdata.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.masterdata.domain.MaterialClass;

/**
 * 物料分类Service接口
 *
 * @author jinzhong
 * @date 2022-09-13
 */
public interface IMaterialClassService
{
    /**
     * 查询物料分类
     *
     * @param id 物料分类主键
     * @return 物料分类
     */
    MaterialClass selectMaterialClassById(Long id);

    /**
     * 查询物料分类列表
     *
     * @param materialClass 物料分类
     * @return 物料分类集合
     */
    List<MaterialClass> selectMaterialClassList(MaterialClass materialClass);

    /**
     * 新增物料分类
     *
     * @param materialClass 物料分类
     * @return 结果
     */
    int insertMaterialClass(MaterialClass materialClass);

    /**
     * 修改物料分类
     *
     * @param materialClass 物料分类
     * @return 结果
     */
    AjaxResult updateMaterialClass(MaterialClass materialClass);

    /**
     * 批量删除物料分类
     *
     * @param ids 需要删除的物料分类主键集合
     * @return 结果
     */
    AjaxResult deleteMaterialClassByIds(Long[] ids);

}
