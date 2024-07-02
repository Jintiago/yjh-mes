package cn.sourceplan.common.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.utils.SecurityUtils;
import cn.sourceplan.masterdata.domain.Station;
import cn.sourceplan.masterdata.mapper.StationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.common.mapper.SysUserStationMapper;
import cn.sourceplan.common.domain.SysUserStation;
import cn.sourceplan.common.service.ISysUserStationService;

/**
 * 用户工位绑定Service业务层处理
 *
 * @author jinzhong
 * @date 2023-02-07
 */
@Service
public class SysUserStationServiceImpl implements ISysUserStationService
{
    @Autowired
    private SysUserStationMapper sysUserStationMapper;

    @Autowired
    private StationMapper stationMapper;

    /**
     * 查询用户工位绑定
     *
     * @param id 用户工位绑定主键
     * @return 用户工位绑定
     */
    @Override
    public SysUserStation selectSysUserStationById(Long id)
    {
        SysUserStation sysUserStation = sysUserStationMapper.selectById(id);
        Station station = stationMapper.selectById(sysUserStation.getStationId());
        sysUserStation.setWorkshopName(station.getWorkshopName());

        return sysUserStation;
    }

    /**
     * 查询用户工位绑定列表
     *
     * @param sysUserStation 用户工位绑定
     * @return 用户工位绑定
     */
    @Override
    public List<SysUserStation> selectSysUserStationList(SysUserStation sysUserStation)
    {
        QueryWrapper<SysUserStation> qw = new QueryWrapper<>(sysUserStation);
        qw.orderByAsc("station_Id");

        List<SysUserStation> sysUserStations = sysUserStationMapper.selectList(qw);
        for (int i = 0; i < sysUserStations.size(); i++) {
            SysUserStation temp = sysUserStations.get(i);
            Station station = stationMapper.selectById(temp.getStationId());
            temp.setWorkshopName(station.getWorkshopName());
        }

        return sysUserStations;
    }

    /**
     * 新增用户工位绑定
     *
     * @param sysUserStation 用户工位绑定
     * @return 结果
     */
    @Override
    public int insertSysUserStation(SysUserStation sysUserStation)
    {
        sysUserStation.setUpdateTime(new Date());
        sysUserStation.setUpdateBy(SecurityUtils.getUsername());
        return sysUserStationMapper.insert(sysUserStation);
    }

    /**
     * 修改用户工位绑定
     *
     * @param sysUserStation 用户工位绑定
     * @return 结果
     */
    @Override
    public int updateSysUserStation(SysUserStation sysUserStation)
    {
        sysUserStation.setUpdateBy(null);
        sysUserStation.setUpdateTime(null);
        return sysUserStationMapper.updateById(sysUserStation);
    }

    /**
     * 批量删除用户工位绑定
     *
     * @param ids 需要删除的用户工位绑定主键
     * @return 结果
     */
    @Override
    public int deleteSysUserStationByIds(Long[] ids)
    {
        return sysUserStationMapper.deleteSysUserStationByIds(ids);
    }

    @Override
    public int mobileBindUserStation(SysUserStation sysUserStation) {
        Long userId = SecurityUtils.getUserId();
        SysUserStation s =sysUserStationMapper.getInfoByUserId(userId);
        if(s==null){
            //新增
            sysUserStation.setUserId(userId);
            sysUserStation.setUserName(SecurityUtils.getUsername());
            return insertSysUserStation(sysUserStation);
        }else{
            //修改
            sysUserStation.setId(s.getId());
            return updateSysUserStation(sysUserStation);
        }

    }

    @Override
    public SysUserStation getInfoByUserId(Long userId) {


        SysUserStation s = sysUserStationMapper.getInfoByUserId(userId);

        return s;
    }

}
