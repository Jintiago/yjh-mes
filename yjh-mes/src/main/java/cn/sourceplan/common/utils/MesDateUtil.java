package cn.sourceplan.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MesDateUtil {

    /**
     * 查询条件中的结束日期自动加1天
     * 因为查询2023-04-1时，数据库中2023-04-01 01:01:01 的就查不出来了
     */
    public static Map<String,Object> queryDateAddOneDay(HttpServletRequest httpServletRequest){
        String beginDate = httpServletRequest.getParameter("dateQuery[0]");
        String endDate = httpServletRequest.getParameter("dateQuery[1]");
        Date tmpDate = cn.hutool.core.date.DateUtil.parse(endDate);
        tmpDate = cn.hutool.core.date.DateUtil.offsetDay(tmpDate, 1);
        endDate = cn.hutool.core.date.DateUtil.format(tmpDate, "yyyy-MM-dd");
        Map<String,Object> map = new HashMap<>();
        map.put("beginDate",beginDate);
        map.put("endDate",endDate);
        return  map;
    }
}
