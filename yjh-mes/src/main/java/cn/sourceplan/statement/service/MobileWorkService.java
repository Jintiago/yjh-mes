package cn.sourceplan.statement.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface MobileWorkService {
    JSONObject executeBillCount(HttpServletRequest httpServletRequest);
}
