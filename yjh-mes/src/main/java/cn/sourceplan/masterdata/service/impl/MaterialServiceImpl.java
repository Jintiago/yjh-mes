package cn.sourceplan.masterdata.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.SqlCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.masterdata.mapper.MaterialMapper;
import cn.sourceplan.masterdata.domain.Material;
import cn.sourceplan.masterdata.service.IMaterialService;

/**
 * 物料Service业务层处理
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Service
public class MaterialServiceImpl implements IMaterialService
{
    @Autowired
    private MaterialMapper materialMapper;

    /**
     * 查询物料
     *
     * @param id 物料主键
     * @return 物料
     */
    @Override
    public Material selectMaterialById(Long id)
    {
        return materialMapper.selectById(id);
    }

    /**
     * 查询物料列表
     *
     * @param material 物料
     * @return 物料
     */
    @Override
    public List<Material> selectMaterialList(Material material)
    {
        QueryWrapper<Material> w = new QueryWrapper<>(material);
        w.orderByAsc("number");
        return materialMapper.selectList(w);
    }

    /**
     * 新增物料
     *
     * @param material 物料
     * @return 结果
     */
    @Override
    public AjaxResult insertMaterial(Material material)
    {
        //编号不允许重复
        QueryWrapper<Material> qw = new QueryWrapper<>();
        qw.eq("number",material.getNumber());
        Integer count = materialMapper.selectCount(qw);
        if(count>0){
            return AjaxResult.error(500,"物料编号已存在，请核对");
        }
        return AjaxResult.success("操作成功"+materialMapper.insert(material)+"条");
    }

    /**
     * 修改物料
     *
     * @param material 物料
     * @return 结果
     */
    @Override
    public AjaxResult updateMaterial(Material material)
    {
        //清空这两个属性
        material.setUpdateBy(null);
        material.setUpdateTime(null);

        //校验数据引用关系
        //BOM
        String bomCheck = SqlCheckUtil.sqlCheck("number","md_bom",
                "where material_id = "+material.getId()+" and status ='0'");
        if(bomCheck!=null){
            return AjaxResult.error("该数据在物料BOM["+ bomCheck +"]中已被引用，无法修改");
        }
        //销售订单
        String salOrderCheck = SqlCheckUtil.sqlCheck("number",
                "sal_order_entry b left join sal_order a on a.id = b.main_id ",
                "where b.material_id = "+material.getId()+" and a.status ='A'" +
                        "group by a.number");
        if(salOrderCheck!=null){
            return AjaxResult.error("该数据在销售订单["+ salOrderCheck +"]中已被引用，无法修改");
        }
        //生产工单
        String workorderCheck = SqlCheckUtil.sqlCheck("number","pro_workorder",
                "where material_id = "+material.getId()+" and status ='A'");
        if(workorderCheck!=null){
            return AjaxResult.error("该数据在生产工单["+ workorderCheck +"]中已被引用，无法修改");
        }
        //计件工资方案
        String pieceWagePlanCheck = SqlCheckUtil.sqlCheck("process_name","fm_piece_wage_plan ",
                "where material_id = "+material.getId()+" and status ='A'");
        if(pieceWagePlanCheck!=null){
            return AjaxResult.error("该数据在计件工资方案,工序["+ pieceWagePlanCheck +"]中已被引用，无法修改");
        }

        int i = materialMapper.updateById(material);

        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 批量删除物料
     *
     * @param ids 需要删除的物料主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteMaterialByIds(Long[] ids)
    {
        String str = StringUtils.join(ids, ",");
        //校验数据引用关系
        //BOM
        String bomCheck = SqlCheckUtil.sqlCheck("number","md_bom",
                "where material_id in ("+ str+" )and status ='0'");
        if(bomCheck!=null){
            return AjaxResult.error("所选数据在物料BOM["+ bomCheck +"]中已被引用，无法删除");
        }
        //计件工资方案
        String pieceWagePlanCheck = SqlCheckUtil.sqlCheck("process_name","fm_piece_wage_plan",
                "where material_id in ( "+str+" ) and status ='A'");
        if(pieceWagePlanCheck!=null){
            return AjaxResult.error("所选数据在计件工资方案,工序["+ pieceWagePlanCheck +"]中已被引用，无法删除");
        }
        //销售订单
        String salOrderCheck = SqlCheckUtil.sqlCheck("number",
                "sal_order_entry b left join sal_order a on a.id = b.main_id ",
                "where b.material_id in ( "+str+" )and a.status ='A'" +
                        "group by a.number");
        if(salOrderCheck!=null){
            return AjaxResult.error("所选数据在销售订单["+ salOrderCheck +"]中已被引用，无法删除");
        }
        //生产工单
        String workorderCheck = SqlCheckUtil.sqlCheck("number","pro_workorder",
                "where material_id in ( "+str+") and status ='A'");
        if(workorderCheck!=null){
            return AjaxResult.error("所选数据在生产工单["+ workorderCheck +"]中已被引用，无法删除");
        }

        int i = materialMapper.deleteMaterialByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

}
