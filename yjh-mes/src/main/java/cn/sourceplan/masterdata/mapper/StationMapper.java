package cn.sourceplan.masterdata.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.masterdata.domain.Station;
import org.springframework.stereotype.Repository;

/**
 * 工位Mapper接口
 *
 * @author jinzhong
 * @date 2022-09-27
 */
@Repository
public interface StationMapper extends BaseMapper<Station>
{

    /**
     * 查询工位列表
     *
     * @param station 工位
     * @return 工位集合
     */
    List<Station> selectStationList(Station station);

    /**
     * 删除工位
     *
     * @param id 工位主键
     * @return 结果
     */
    int deleteStationById(Long id);

    /**
     * 批量删除工位
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteStationByIds(Long[] ids);
}
