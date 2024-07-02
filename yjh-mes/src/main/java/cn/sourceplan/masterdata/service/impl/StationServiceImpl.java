package cn.sourceplan.masterdata.service.impl;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.SqlCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.masterdata.mapper.StationMapper;
import cn.sourceplan.masterdata.domain.Station;
import cn.sourceplan.masterdata.service.IStationService;

/**
 * 工位Service业务层处理
 *
 * @author jinzhong
 * @date 2022-09-27
 */
@Service
public class StationServiceImpl implements IStationService
{
    @Autowired
    private StationMapper stationMapper;

    /**
     * 查询工位
     *
     * @param id 工位主键
     * @return 工位
     */
    @Override
    public Station selectStationById(Long id)
    {
        return stationMapper.selectById(id);
    }

    /**
     * 查询工位列表
     *
     * @param station 工位
     * @return 工位
     */
    @Override
    public List<Station> selectStationList(Station station)
    {
        return stationMapper.selectStationList(station);
    }

    /**
     * 新增工位
     *
     * @param station 工位
     * @return 结果
     */
    @Override
    public int insertStation(Station station)
    {
        return stationMapper.insert(station);
    }

    /**
     * 修改工位
     *
     * @param station 工位
     * @return 结果
     */
    @Override
    public AjaxResult updateStation(Station station)
    {
        String result = SqlCheckUtil.sqlCheck("number",
                "pro_workorder_entry b   left join pro_workorder a  on a.id =b.workorder_id    " ,
                " where  b.station_id = "+station.getId() +" and a.status ='A' " +
                        "group by a.number");
        if(result!=null){
            return AjaxResult.error("该数据在生产工单["+ result +"]中已被引用，无法修改");
        }
        int i = stationMapper.updateById(station);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 批量删除工位
     *
     * @param ids 需要删除的工位主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteStationByIds(Long[] ids)
    {

        String result = SqlCheckUtil.sqlCheck("number",
                "pro_workorder_entry b   left join pro_workorder a  on a.id =b.workorder_id    " ,
                " where  b.station_id in( "+StringUtils.join(ids,",") +") and a.status ='A' " +
                        "group by a.number");
        if(result!=null){
            return AjaxResult.error("所选数据在生产工单["+ result +"]中已被引用，无法删除");
        }

        int i = stationMapper.deleteStationByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();

    }

}
