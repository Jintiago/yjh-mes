package cn.sourceplan.statement.mapper;

import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author jinzhong
 */
@Repository
public interface PcIndexMapper {


    Integer  outputTrend (@Param("map") Map<String, Object> map);

    JSONObject qualificationRate(@Param("map")Map<String, Object> map);


}
