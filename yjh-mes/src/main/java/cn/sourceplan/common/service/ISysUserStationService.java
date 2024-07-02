package cn.sourceplan.common.service;

import java.util.List;
import cn.sourceplan.common.domain.SysUserStation;

/**
 * 用户工位绑定Service接口
 *
 * @author jinzhong
 * @date 2023-02-07
 */
public interface ISysUserStationService
{
    /**
     * 查询用户工位绑定
     *
     * @param id 主键
     * @return 用户工位绑定
     */
    SysUserStation selectSysUserStationById(Long id);

    /**
     * 查询用户工位绑定列表
     *
     * @param sysUserStation 用户工位绑定
     * @return 用户工位绑定集合
     */
    List<SysUserStation> selectSysUserStationList(SysUserStation sysUserStation);

    /**
     * 新增用户工位绑定
     *
     * @param sysUserStation 用户工位绑定
     * @return 结果
     */
    int insertSysUserStation(SysUserStation sysUserStation);

    /**
     * 修改用户工位绑定
     *
     * @param sysUserStation 用户工位绑定
     * @return 结果
     */
    int updateSysUserStation(SysUserStation sysUserStation);

    /**
     * 批量删除用户工位绑定
     *
     * @param userIds 需要删除的用户工位绑定主键集合
     * @return 结果
     */
    int deleteSysUserStationByIds(Long[] userIds);


    /**
     * 移动端扫码绑定
     *
     * @param sysUserStation 用户工位绑定
     * @return 结果
     */
    int mobileBindUserStation(SysUserStation sysUserStation);


    /**
     * 根据当前登录用户ID查询工位绑定信息
     *
     * @param userId userId
     * @return 结果
     */
    SysUserStation getInfoByUserId(Long userId);
}
