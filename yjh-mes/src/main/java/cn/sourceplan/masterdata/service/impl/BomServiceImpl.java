package cn.sourceplan.masterdata.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.masterdata.service.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.masterdata.mapper.BomMapper;
import cn.sourceplan.masterdata.domain.Bom;
import cn.sourceplan.masterdata.service.IBomService;

/**
 * 物料BOMService业务层处理
 *
 * @author jinzhong
 * @date 2022-09-16
 */
@Service
public class BomServiceImpl implements IBomService
{
    @Autowired
    private IMaterialService materialService;

    @Autowired
    private BomMapper bomMapper;

    /**
     * 查询物料BOM
     *
     * @param id 物料BOM主键
     * @return 物料BOM
     */
    @Override
    public Bom selectBomById(Long id)
    {
        return bomMapper.selectById(id);
    }

    /**
     * 查询物料BOM列表
     *
     * @param bom 物料BOM
     * @return 物料BOM
     */
    @Override
    public List<Bom> selectBomList(Bom bom)
    {
        return bomMapper.selectBomList(bom);
    }

    /**
     * 新增物料BOM
     *
     * @param bom 物料BOM
     * @return 结果
     */
    @Override
    public AjaxResult insertBom(Bom bom)
    {
        //编号不允许重复
        QueryWrapper<Bom> qw = new QueryWrapper<>();
        qw.eq("number",bom.getNumber());
        Integer count = bomMapper.selectCount(qw);
        if(count>0){
            return AjaxResult.error(500,"该BOM版本号已存在，请核对");
        }
        return AjaxResult.success("操作成功"+bomMapper.insert(bom)+"条");
    }

    /**
     * 修改物料BOM
     *
     * @param bom 物料BOM
     * @return 结果
     */
    @Override
    public int updateBom(Bom bom)
    {
        bom.setUpdateTime(null);
        bom.setUpdateBy(null);
        return bomMapper.updateById(bom);
    }

    /**
     * 批量删除物料BOM
     *
     * @param ids 需要删除的物料BOM主键
     * @return 结果
     */
    @Override
    public int deleteBomByIds(Long[] ids)
    {
        return bomMapper.deleteBomByIds(ids);
    }

    /**
     * 删除物料BOM信息
     *
     * @param id 物料BOM主键
     * @return 结果
     */
    @Override
    public int deleteBomById(Long id)
    {
        return bomMapper.deleteBomById(id);
    }
}
