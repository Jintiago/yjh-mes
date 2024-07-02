package cn.sourceplan.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.masterdata.domain.Material;
import org.springframework.stereotype.Repository;

/**
 * 物料Mapper接口
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Repository
public interface MaterialMapper extends BaseMapper<Material>
{

    /**
     * 删除物料
     *
     * @param id 物料主键
     * @return 结果
     */
    int deleteMaterialById(Long id);

    /**
     * 批量删除物料
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMaterialByIds(Long[] ids);
}
