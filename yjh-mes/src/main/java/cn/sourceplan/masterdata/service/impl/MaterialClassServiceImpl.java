package cn.sourceplan.masterdata.service.impl;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.SqlCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.masterdata.mapper.MaterialClassMapper;
import cn.sourceplan.masterdata.domain.MaterialClass;
import cn.sourceplan.masterdata.service.IMaterialClassService;

/**
 * 物料分类Service业务层处理
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Service
public class MaterialClassServiceImpl implements IMaterialClassService
{
    @Autowired
    private MaterialClassMapper materialClassMapper;

    /**
     * 查询物料分类
     *
     * @param id 物料分类主键
     * @return 物料分类
     */
    @Override
    public MaterialClass selectMaterialClassById(Long id)
    {
        return materialClassMapper.selectMaterialClassById(id);
    }

    /**
     * 查询物料分类列表
     *
     * @param materialClass 物料分类
     * @return 物料分类
     */
    @Override
    public List<MaterialClass> selectMaterialClassList(MaterialClass materialClass)
    {
        return materialClassMapper.selectMaterialClassList(materialClass);
    }

    /**
     * 新增物料分类
     *
     * @param materialClass 物料分类
     * @return 结果
     */
    @Override
    public int insertMaterialClass(MaterialClass materialClass)
    {
        return materialClassMapper.insertMaterialClass(materialClass);
    }

    /**
     * 修改物料分类
     *
     * @param materialClass 物料分类
     * @return 结果
     */
    @Override
    public AjaxResult updateMaterialClass(MaterialClass materialClass)
    {
        String materialCheck = SqlCheckUtil.sqlCheck("number","md_material",
                "where class_id = "+materialClass.getId()+" and status ='0'");
        if(materialCheck!=null){
            return AjaxResult.error("该数据在物料["+ materialCheck +"]中已被引用，无法修改");
        }

        int i = materialClassMapper.updateMaterialClass(materialClass);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 批量删除物料分类
     *
     * @param ids 需要删除的物料分类主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteMaterialClassByIds(Long[] ids)
    {

        String classCheck = SqlCheckUtil.sqlCheck("name","md_material_class",
                "where parent_id in ("+ StringUtils.join(ids,",")+") ");
        if(classCheck!=null){
            return AjaxResult.error("所选数据存在下级["+ classCheck +"]，无法删除");
        }
        String materialCheck = SqlCheckUtil.sqlCheck("number","md_material",
                "where class_id in ("+ StringUtils.join(ids,",")+")  and status ='0'");
        if(materialCheck!=null){
            return AjaxResult.error("所选数据在物料["+ materialCheck +"]中已被引用，无法删除");
        }
        int i = materialClassMapper.deleteMaterialClassByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

}
