package cn.sourceplan.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.masterdata.domain.Unit;
import org.springframework.stereotype.Repository;

/**
 * 计量单位Mapper接口
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Repository
public interface UnitMapper extends BaseMapper<Unit>
{

    /**
     * 新增计量单位
     *
     * @param unit 计量单位
     * @return 结果
     */
    int insertUnit(Unit unit);

    /**
     * 修改计量单位
     *
     * @param unit 计量单位
     * @return 结果
     */
    int updateUnit(Unit unit);

    /**
     * 删除计量单位
     *
     * @param id 计量单位主键
     * @return 结果
     */
    int deleteUnitById(Long id);

    /**
     * 批量删除计量单位
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteUnitByIds(Long[] ids);
}
