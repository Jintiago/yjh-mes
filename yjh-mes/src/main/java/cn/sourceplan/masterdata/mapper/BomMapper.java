package cn.sourceplan.masterdata.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.masterdata.domain.Bom;
import org.springframework.stereotype.Repository;

/**
 * 物料BOMMapper接口
 *
 * @author jinzhong
 * @date 2022-09-16
 */
@Repository
public interface BomMapper extends BaseMapper<Bom>
{

    /**
     * 查询物料BOM列表
     *
     * @param bom 物料BOM
     * @return 物料BOM集合
     */
    List<Bom> selectBomList(Bom bom);


    /**
     * 删除物料BOM
     *
     * @param id 物料BOM主键
     * @return 结果
     */
    int deleteBomById(Long id);

    /**
     * 批量删除物料BOM
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBomByIds(Long[] ids);


}
