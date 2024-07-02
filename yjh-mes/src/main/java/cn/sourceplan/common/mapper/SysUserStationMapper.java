package cn.sourceplan.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.common.domain.SysUserStation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户工位绑定Mapper接口
 *
 * @author jinzhong
 * @date 2023-02-07
 */
@Repository
public interface SysUserStationMapper extends BaseMapper<SysUserStation>
{

    /**
     * 批量删除用户工位绑定
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSysUserStationByIds(Long[] ids);


    /**
     * 根据当前登录用户ID查询工位绑定信息
     *
     * @param userId userId
     * @return 结果
     */
    SysUserStation getInfoByUserId(@Param("userId") Long userId);
}
