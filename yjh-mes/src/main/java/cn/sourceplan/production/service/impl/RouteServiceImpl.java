package cn.sourceplan.production.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.DateUtils;
import cn.sourceplan.common.utils.SqlCheckUtil;
import cn.sourceplan.production.domain.RouteProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.production.mapper.RouteMapper;
import cn.sourceplan.production.domain.Route;
import cn.sourceplan.production.service.IRouteService;

/**
 * 工序路线Service业务层处理
 *
 * @author jinzhong
 * @date 2022-09-22
 */
@Service
public class RouteServiceImpl implements IRouteService
{
    @Autowired
    private RouteMapper routeMapper;

    /**
     * 查询工序路线
     *
     * @param id 工序路线主键
     * @return 工序路线
     */
    @Override
    public Route selectRouteById(Long id)
    {
        return routeMapper.selectRouteById(id);
    }

    /**
     * 查询工序路线列表
     *
     * @param route 工序路线
     * @return 工序路线
     */
    @Override
    public List<Route> selectRouteList(Route route)
    {
        QueryWrapper<Route> qw = new QueryWrapper(route);
        return routeMapper.selectList(qw);
    }

    /**
     * 新增工序路线
     *
     * @param route 工序路线
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertRoute(Route route)
    {
        int rows = routeMapper.insert(route);
        insertProRouteProcess(route);
        return rows;
    }

    /**
     * 修改工序路线
     *
     * @param route 工序路线
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult updateRoute(Route route)
    {
        //校验数据引用关系
        //物料
        String pieceWagePlanCheck = SqlCheckUtil.sqlCheck("number","md_material",
                "where route_id =  "+route.getId()+"  and status ='0'");
        if(pieceWagePlanCheck!=null){
            return AjaxResult.error("该数据在物料["+pieceWagePlanCheck+"]中已被引用，无法修改");
        }
        route.setUpdateTime(DateUtils.getNowDate());
        routeMapper.deleteRouteProcessByRouteId(route.getId());
        insertProRouteProcess(route);
        int i = routeMapper.updateRoute(route);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 批量删除工序路线
     *
     * @param ids 需要删除的工序路线主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult deleteRouteByIds(Long[] ids)
    {
        String str = StringUtils.join(ids, ",");
        //校验数据引用关系
        //物料
        String pieceWagePlanCheck = SqlCheckUtil.sqlCheck("number","md_material",
                "where route_id in(  "+str+" ) and status ='0'");
        if(pieceWagePlanCheck!=null){
            return AjaxResult.error("所选数据在物料["+ pieceWagePlanCheck +"]中已被引用，无法删除");
        }
        routeMapper.deleteRouteProcessByRouteIds(ids);
        int i = routeMapper.deleteRouteByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 新增工序路线明细信息
     *
     * @param route 工序路线对象
     */
    @Override
    public void insertProRouteProcess(Route route)
    {
        List<RouteProcess> proRouteProcessList = route.getRouteProcessList();
        Long id = route.getId();
        if (StringUtils.isNotNull(proRouteProcessList))
        {
            List<RouteProcess> list = new ArrayList<RouteProcess>();
            for (RouteProcess proRouteProcess : proRouteProcessList)
            {
                proRouteProcess.setRouteId(id);
                list.add(proRouteProcess);
            }
            if (list.size() > 0)
            {
                routeMapper.batchRouteProcess(list);
            }
        }
    }

    /**
     *
     * 根据当前工序id查询工艺路线
     * 查询指定顺序的工序
     *
     * @param routeId
     * @param sort
     */
    @Override
    public RouteProcess getProcessBySort(Long routeId,int sort)
    {

        Route route = routeMapper.selectRouteById(routeId);
        if (route ==null) return null;
        List<RouteProcess> routeProcessList = route.getRouteProcessList();
        for (RouteProcess routeProcess : routeProcessList) {
            if (routeProcess.getSort() == sort) {
                return routeProcess;
            }
        }
        return null;
    }
}
