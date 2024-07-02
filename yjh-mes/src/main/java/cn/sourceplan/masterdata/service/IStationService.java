package cn.sourceplan.masterdata.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.masterdata.domain.Station;

/**
 * 工位Service接口
 *
 * @author jinzhong
 * @date 2022-09-27
 */
public interface IStationService
{
    /**
     * 查询工位
     *
     * @param id 工位主键
     * @return 工位
     */
    Station selectStationById(Long id);

    /**
     * 查询工位列表
     *
     * @param station 工位
     * @return 工位集合
     */
    List<Station> selectStationList(Station station);

    /**
     * 新增工位
     *
     * @param station 工位
     * @return 结果
     */
    int insertStation(Station station);

    /**
     * 修改工位
     *
     * @param station 工位
     * @return 结果
     */
    AjaxResult updateStation(Station station);

    /**
     * 批量删除工位
     *
     * @param ids 需要删除的工位主键集合
     * @return 结果
     */
    AjaxResult deleteStationByIds(Long[] ids);

}
