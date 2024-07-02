package cn.sourceplan.production.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.production.domain.Route;
import cn.sourceplan.production.domain.RouteProcess;
import org.springframework.stereotype.Repository;

/**
 * 工序路线Mapper接口
 *
 * @author jinzhong
 * @date 2022-09-22
 */
@Repository
public interface RouteMapper extends BaseMapper<Route>
{
    /**
     * 查询工序路线
     *
     * @param id 工序路线主键
     * @return 工序路线
     */
    Route selectRouteById(Long id);

    /**
     * 修改工序路线
     *
     * @param route 工序路线
     * @return 结果
     */
    int updateRoute(Route route);

    /**
     * 批量删除工序路线
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteRouteByIds(Long[] ids);

    /**
     * 批量删除工序路线明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteRouteProcessByRouteIds(Long[] ids);

    /**
     * 批量新增工序路线明细
     *
     * @param proRouteProcessList 工序路线明细列表
     * @return 结果
     */
    int batchRouteProcess(List<RouteProcess> proRouteProcessList);


    /**
     * 通过工序路线主键删除工序路线明细信息
     *
     * @param id 工序路线ID
     * @return 结果
     */
    int deleteRouteProcessByRouteId(Long id);
}
