package cn.sourceplan.common.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author jinzhong
 */
@Repository
public interface AppVersionMapper {

    /**
     *
     * 查询当前app版本号
     * @return
     */
    @Select("select version from sys_app_version")
    Double query();
}
