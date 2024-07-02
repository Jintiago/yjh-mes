package cn.sourceplan.common.utils;

import cn.sourceplan.common.mapper.SqlCheckMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 自定义SQL查询/校验工具类
 * @author jinzhong
 */
@Component
public class SqlCheckUtil {

    @Autowired
    private SqlCheckMapper sqlCheckMapperT;

    private  static SqlCheckMapper sqlCheckMapper;

    @PostConstruct
    public void init (){
        sqlCheckMapper=sqlCheckMapperT;
    }

    /**
     *
     * 编辑和删除A时查询其所关联的B表是否已存在A数据，存在则不让其操作，
     * 返回所关联表B的数据编号
     *
     */
    public static String sqlCheck(String field, String tableName, String whereSql) {
        List<Map<String,String>> l = sqlCheckMapper.selectCheckField(field,tableName, whereSql);
        StringBuilder sb = new StringBuilder();
        if(l!=null&&l.size()>0){
            for (int i = 0; i <l.size() ; i++) {
                Map<String, String> m = l.get(i);
                if(m==null){
                    continue;
                }
                String s = m.get(field);
                sb.append(s);
                if(i!=l.size()-1){
                    sb.append(",");
                }
            }
            return sb.toString();
        }else{
            return null;
        }
    }

    public static int sqlCheck( String tableName, String whereSql) {
        int i = sqlCheckMapper.selectCheckCount(tableName, whereSql);
        return i;
    }

    /**
     *
     * 查询该表所有字段信息
     * 包括字段名，字段类型，字段说明
     *
     */
    public static List<Map<String, String>> getTableAllFiled(String tableName){
        return sqlCheckMapper.getTableAllFiled(tableName);
    }

    /**
     *
     * 执行任意自定义sql
     *
     */
    public static Object executeCustomSql( String customSql) {
        Object i = sqlCheckMapper.executeCustomSql(customSql);
        return i;
    }

    /**
     *
     * 封装过滤条件
     * 例如  传递 12
     * 查询   "sourceInfo": "{\"saleOrderEntryId\":[12,13]}" 这种格式的数据书
     * @param fieldName 例如 saleOrderEntryId
     */
    public static String querySourceInfo(String fieldName,Long[] ids) {


        String sql="";
        sql+=" and (";

        for (int i = 0; i < ids.length; i++) {

            //处于中间
            sql+="source_info like concat('%,',"+ids[i]+",',%' ) or ";
            //处于末尾
            sql+="source_info like concat('%,',"+ids[i]+",']}')or ";
            //处于开头
            sql+="source_info like concat('{\""+fieldName+"\":[',"+ids[i]+",',%' ) or ";
            //只有一个
            sql+="source_info = concat('{\""+fieldName+"\":[',"+ids[i]+",']}')";

            //不是最后一个
            if(i!=ids.length-1){
                sql+=" or ";
            }

        }
        sql+=")";


        return sql;
    }


}
