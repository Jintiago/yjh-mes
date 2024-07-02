package cn.sourceplan.statement.mapper;

import cn.sourceplan.production.domain.Report;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报工单Mapper接口
 *
 * @author jinzhong
 * @date 2023-02-15
 */
@Repository
public interface ProductionReportMapper extends BaseMapper<Report>
{


    /**
     * 根据生产订单id查询各道工序生产清空
     *
     * @param id 根据生产订单id
     * @return 集合
     */
    public List<JSONObject>  queryProcessBySaleOrderEntryId(@Param("id") Long id );



    /**
     * 根据工序查询生产情况
     *
     * @param processName 根据生产订单id
     * @return 集合
     */
    public List<JSONObject>  queryProductionExecutionByProcess(@Param("processName") String processName );

}
