package cn.sourceplan.common.mapper;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author jinzhong
 */
@Repository
public interface SqlCheckMapper {
    /**
     * 自定义查询语句
     *  @param field 字段
     * @param tableName 数据库表名
     * @param whereSql 过滤条件
     * @return 是否有数据  true 校验通过  false 校验失败
     */
    List<Map<String,String>> selectCheckField(@Param("field") String field, @Param("tableName") String tableName, @Param("whereSql") String whereSql);

    /**
     * 自定义查询语句
     * @param tableName 数据库表名
     * @param whereSql 过滤条件
     * @return 是否有数据  true 校验通过  false 校验失败
     */
    int selectCheckCount(@Param("tableName") String tableName, @Param("whereSql") String whereSql);

    /**
     * 查询该表所有字段信息
     * 包括字段名，字段类型，字段说明
     * @param tableName 数据库名
     * @return 是否有数据  true 校验通过  false 校验失败
     */
    List<Map<String,String>> getTableAllFiled(@Param("tableName")String tableName);

    /**
     * 执行任意自定义sql
     * @param sql sql语句
     * @return
     */
    Object executeCustomSql(@Param("sql")String sql);
}
