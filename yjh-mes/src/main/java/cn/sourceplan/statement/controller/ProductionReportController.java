package cn.sourceplan.statement.controller;

import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.statement.mapper.ProductionReportMapper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产相关报表Controller
 *
 * @author jinzhong
 * @date 2022-12-12
 */
@RestController
@RequestMapping("/reports/production")
public class ProductionReportController extends BaseController
{
    @Autowired
    private ProductionReportMapper productionReportMapper;

    /**
     * 根据生产订单id查询各道工序生产清空
     */
    @GetMapping("/queryProcessBySaleOrderEntryId")
    public List<JSONObject> queryProcessBySaleOrderEntryId(HttpServletRequest httpServletRequest)
    {
        String id = httpServletRequest.getParameter("id");
        List<JSONObject> list = productionReportMapper.queryProcessBySaleOrderEntryId(Long.parseLong(id) );

        return list;
    }

    /**
     * 根据工序查询生产情况
     */
    @GetMapping("/queryProductionExecutionByProcess")
    public List<JSONObject> queryProductionExecutionByProcess(HttpServletRequest httpServletRequest)
    {
        String processName = httpServletRequest.getParameter("processName");
        List<JSONObject> list = productionReportMapper.queryProductionExecutionByProcess(processName);


        //过滤掉已完成的
        List<JSONObject> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = list.get(i);
            String schedule = jsonObject.getString("schedule");
            String extendField = jsonObject.getString("extendField");
            jsonObject.put("extendField",JSONObject.parseObject(extendField));
            schedule = schedule.replaceAll("%", "");
            if(Double.valueOf(schedule)<=100){
                newList.add(jsonObject);
            }
        }
        //
        return newList;
    }

}
