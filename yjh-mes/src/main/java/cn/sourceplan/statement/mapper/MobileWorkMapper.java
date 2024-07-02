package cn.sourceplan.statement.mapper;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author jinzhong
 */
@Repository
public interface MobileWorkMapper {


    JSONObject productionQuality(@Param("beginDate")String beginDate,
                                 @Param("endDate")String endDate,
                                 @Param("userId")Long userId);
}
