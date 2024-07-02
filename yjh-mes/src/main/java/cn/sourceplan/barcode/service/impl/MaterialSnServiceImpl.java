package cn.sourceplan.barcode.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.barcode.mapper.MaterialSnMapper;
import cn.sourceplan.barcode.domain.MaterialSn;
import cn.sourceplan.barcode.service.IMaterialSnService;

/**
 * 物料SN码Service业务层处理
 *
 * @author jinzhong
 * @date 2023-04-15
 */
@Service
public class MaterialSnServiceImpl implements IMaterialSnService
{
    @Autowired
    private MaterialSnMapper materialSnMapper;

    /**
     * 查询物料SN码
     *
     * @param id 物料SN码主键
     * @return 物料SN码
     */
    @Override
    public MaterialSn selectMaterialSnById(Long id)
    {
        return materialSnMapper.selectById(id);
    }

    /**
     * 查询物料SN码列表
     *
     * @param materialSn 物料SN码
     * @return 物料SN码
     */
    @Override
    public List<MaterialSn> selectMaterialSnList(MaterialSn materialSn)
    {
        QueryWrapper<MaterialSn> qw = new QueryWrapper<>(materialSn);
        return materialSnMapper.selectList(qw);
    }

    /**
     * 新增物料SN码
     *
     * @param materialSn 物料SN码
     * @return 结果
     */
    @Override
    public int insertMaterialSn(MaterialSn materialSn)
    {
        return materialSnMapper.insert(materialSn);
    }

    /**
     * 修改物料SN码
     *
     * @param materialSn 物料SN码
     * @return 结果
     */
    @Override
    public int updateMaterialSn(MaterialSn materialSn)
    {
        return materialSnMapper.updateById(materialSn);
    }

    /**
     * 批量删除物料SN码
     *
     * @param ids 需要删除的物料SN码主键
     * @return 结果
     */
    @Override
    public int deleteMaterialSnByIds(Long[] ids)
    {
        return materialSnMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除物料SN码信息
     *
     * @param id 物料SN码主键
     * @return 结果
     */
    @Override
    public int deleteMaterialSnById(Long id)
    {
        return materialSnMapper.deleteById(id);
    }
}
