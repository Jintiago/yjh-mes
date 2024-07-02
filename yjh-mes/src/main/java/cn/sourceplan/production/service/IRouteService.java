package cn.sourceplan.production.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.production.domain.Route;
import cn.sourceplan.production.domain.RouteProcess;

/**
 * 工序路线Service接口
 *
 * @author jinzhong
 * @date 2022-09-22
 */
public interface IRouteService
{
    /**
     * 查询工序路线
     *
     * @param id 工序路线主键
     * @return 工序路线
     */
    Route selectRouteById(Long id);

    /**
     * 查询工序路线列表
     *
     * @param route 工序路线
     * @return 工序路线集合
     */
    List<Route> selectRouteList(Route route);

    /**
     * 新增工序路线
     *
     * @param route 工序路线
     * @return 结果
     */
    int insertRoute(Route route);

    /**
     * 修改工序路线
     *
     * @param route 工序路线
     * @return 结果
     */
    AjaxResult updateRoute(Route route);

    /**
     * 批量删除工序路线
     *
     * @param ids 需要删除的工序路线主键集合
     * @return 结果
     */
    AjaxResult deleteRouteByIds(Long[] ids);

    void insertProRouteProcess(Route route);

    RouteProcess getProcessBySort(Long routeId, int sort);
}
