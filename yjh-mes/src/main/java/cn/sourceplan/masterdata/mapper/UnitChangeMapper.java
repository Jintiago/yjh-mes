package cn.sourceplan.masterdata.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.masterdata.domain.UnitChange;
import org.springframework.stereotype.Repository;

/**
 * 计量单位转换Mapper接口
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@Repository
public interface UnitChangeMapper extends BaseMapper<UnitChange>
{
    /**
     * 查询计量单位转换
     *
     * @param id 计量单位转换主键
     * @return 计量单位转换
     */
    public UnitChange selectUnitChangeById(Long id);

    /**
     * 查询计量单位转换列表
     *
     * @param unitChange 计量单位转换
     * @return 计量单位转换集合
     */
    public List<UnitChange> selectUnitChangeList(UnitChange unitChange);

    /**
     * 新增计量单位转换
     *
     * @param unitChange 计量单位转换
     * @return 结果
     */
    public int insertUnitChange(UnitChange unitChange);

    /**
     * 修改计量单位转换
     *
     * @param unitChange 计量单位转换
     * @return 结果
     */
    public int updateUnitChange(UnitChange unitChange);

    /**
     * 删除计量单位转换
     *
     * @param id 计量单位转换主键
     * @return 结果
     */
    public int deleteUnitChangeById(Long id);

    /**
     * 批量删除计量单位转换
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUnitChangeByIds(Long[] ids);
}
