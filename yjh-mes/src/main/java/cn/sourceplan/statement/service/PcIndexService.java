package cn.sourceplan.statement.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface PcIndexService {
    JSONObject outputTrend(HttpServletRequest httpServletRequest);

    JSONObject defermentFactor(HttpServletRequest httpServletRequest);

    JSONObject qualificationRate(HttpServletRequest httpServletRequest);

    JSONArray unqualifiedReason(HttpServletRequest httpServletRequest);


}
