package cn.sourceplan.masterdata.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.masterdata.domain.MaterialClass;
import org.springframework.stereotype.Repository;

/**
 * 物料分类Mapper接口
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Repository
public interface MaterialClassMapper extends BaseMapper<MaterialClass>
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
    int updateMaterialClass(MaterialClass materialClass);

    /**
     * 删除物料分类
     *
     * @param id 物料分类主键
     * @return 结果
     */
    int deleteMaterialClassById(Long id);

    /**
     * 批量删除物料分类
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMaterialClassByIds(Long[] ids);
}
