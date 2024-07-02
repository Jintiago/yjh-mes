package cn.sourceplan.statement.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.utils.MesDateUtil;
import cn.sourceplan.production.domain.Report;
import cn.sourceplan.production.mapper.ReportMapper;
import cn.sourceplan.production.mapper.WorkOrderMapper;
import cn.sourceplan.quality.domain.ReportQuality;
import cn.sourceplan.quality.domain.ReportQualityEntry;
import cn.sourceplan.quality.domain.ReportQualityReason;
import cn.sourceplan.quality.service.IReportQualityService;
import cn.sourceplan.statement.mapper.PcIndexMapper;
import cn.sourceplan.statement.service.PcIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PcIndexServiceImpl implements PcIndexService {

    @Autowired
    private IReportQualityService reportQualityService;

    @Autowired
    private PcIndexMapper pcIndexMapper;

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public JSONObject outputTrend(HttpServletRequest httpServletRequest) {
        Map<String, Object> parameterMap = new HashMap<>();
        //当前日前7天，前14天，前21天......的数据
        //for循环8遍
        List<Integer> integers = new ArrayList<>();
        Date nowDate = DateUtil.date();
        List<String> textList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            //七天前
            Date  date = DateUtil.offsetDay(nowDate, -6);
            parameterMap.put("beginDate",DateUtil.format(date, "yyyy-MM-dd"));
            parameterMap.put("endDate",DateUtil.format(nowDate, "yyyy-MM-dd"));
            Integer integer = pcIndexMapper.outputTrend(parameterMap);
            integers.add(integer==null?0:integer);
            //获取走势图下方显示的文字
            Date  textDate = DateUtil.offsetDay(date, 4);
            int day = DateUtil.dayOfMonth(textDate);
            int month = DateUtil.month(textDate)+1;
            String textStr = month+"月"+day;
            textList.add(textStr);
            nowDate=date;
        }
        JSONObject  jsonObject = new JSONObject();
        Collections.reverse(textList);
        Collections.reverse(integers);
        jsonObject.put("text",textList);
        jsonObject.put("value",integers);

        //返回两个数组，一个日期如，['3月31','3月26'],还有一个数据[ 3,5,7,8]
        return jsonObject;
    }

    @Override
    public JSONObject defermentFactor(HttpServletRequest httpServletRequest) {

        String beginDate = httpServletRequest.getParameter("dateQuery[0]");
        String endDate = httpServletRequest.getParameter("dateQuery[1]");
        Date date = DateUtil.parse(endDate);
        date = DateUtil.offsetDay(date, 1);
        endDate = DateUtil.format(date, "yyyy-MM-dd");
        JSONArray jsonArray = new JSONArray();
        int chaoqi = workOrderMapper.selectWorkOrderCount(" is_postpone = 'Y' " +
                "and update_time >= '" + beginDate + "' " +
                "and  update_time<='" + endDate + "'");
        if(chaoqi>0){
            JSONObject chaoqiJ = new JSONObject();
            chaoqiJ.put("name","超期");
            chaoqiJ.put("value",chaoqi);
            jsonArray.add(chaoqiJ);
        }
        int shengchan = workOrderMapper.selectWorkOrderCount(" pro_status ='B' and is_postpone !='Y' " +
                "and update_time >= '" + beginDate + "' " +
                "and  update_time<='" + endDate + "'");
        if(shengchan>0){
            JSONObject shengchanJ = new JSONObject();
            shengchanJ.put("name","生产中");
            shengchanJ.put("value",shengchan);
            jsonArray.add(shengchanJ);
        }

        int wancheng = workOrderMapper.selectWorkOrderCount(" pro_status ='D' and is_postpone !='Y' " +
                "and update_time >= '" + beginDate + "' " +
                "and  update_time<='" + endDate + "'");
        if(wancheng>0){
            JSONObject wanchengJ = new JSONObject();
            wanchengJ.put("name","已完成");
            wanchengJ.put("value",wancheng);
            jsonArray.add(wanchengJ);
        }

        int zanting = workOrderMapper.selectWorkOrderCount(" pro_status ='C' and is_postpone !='Y' " +
                "and update_time >= '" + beginDate + "' " +
                "and  update_time<='" + endDate + "'");
        if(zanting>0){
            JSONObject zantingJ = new JSONObject();
            zantingJ.put("name","暂停");
            zantingJ.put("value",zanting);
            jsonArray.add(zantingJ);
        }

        int guanbi = workOrderMapper.selectWorkOrderCount(" pro_status ='E' and is_postpone !='Y' " +
                "and update_time >= '" + beginDate + "' " +
                "and  update_time<='" + endDate + "'");
        if(guanbi>0){
            JSONObject guanbiJ = new JSONObject();
            guanbiJ.put("name","关闭");
            guanbiJ.put("value",guanbi);
            jsonArray.add(guanbiJ);
        }

        int zongshu = workOrderMapper.selectWorkOrderCount(" `status` ='A' and pro_status not in ('A') " +
                "and update_time >= '" + beginDate + "' " +
                "and  update_time<='" + endDate + "'");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("zongshu",zongshu);
            jsonObject.put("array",jsonArray);

        return jsonObject;
    }

    @Override
    public JSONObject qualificationRate(HttpServletRequest httpServletRequest) {
        Map<String, Object> map = MesDateUtil.queryDateAddOneDay(httpServletRequest);
        JSONObject jsonObject = pcIndexMapper.qualificationRate(map);
        return jsonObject;
    }

    @Override
    public JSONArray unqualifiedReason(HttpServletRequest httpServletRequest) {

        //过滤要查的报工单

        Report reportQW = new Report();
        reportQW.setStatus("A");
        Map<String, Object> params = reportQW.getParams();
        params =MesDateUtil.queryDateAddOneDay(httpServletRequest);
        List<Report> reports = reportMapper.selectReportList(reportQW);


        JSONArray jsonArray = new JSONArray();
        //遍历报工单
        for (int i = 0; i < reports.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            Report report = reports.get(i);
            ReportQuality rq = new ReportQuality();
            rq.setReportIds(report.getId().toString());
            rq.setStatus("0");
            List<ReportQuality> reportQualities = reportQualityService.selectReportQualityList(rq);
            jsonObject.put("reportTime",DateUtil.format(report.getReportTime(), "yyyy-MM-dd HH:mm:ss")   );
            jsonObject.put("reportUserName",report.getReportUserName());
            //只查询不合格数量大于0的数据
            if(!(report.getUnqualifiedQuantity()!=null&&  report.getUnqualifiedQuantity().compareTo(BigDecimal.ZERO)>0)){
                continue;
            }
            if(reportQualities==null||reportQualities.size()==0){

               jsonObject.put("text",report.getUnqualifiedQuantity());

            }else{
                //如果存在质检等级，则继续查询
                ReportQuality reportQuality = reportQualities.get(0);
                jsonObject.put("checkTime",DateUtil.format(reportQuality.getCheckTime(), "yyyy-MM-dd HH:mm:ss"));
                List<ReportQualityEntry> qualityLevelList = reportQuality.getQualityLevelList();

                if(qualityLevelList==null||qualityLevelList.size()==0){
                    jsonObject.put("text",reportQuality.getUnqualifiedQuantity());
                }else{
                    //如果存在质检原因，则继续查询
                    String text = "";
                    for (int j = 0; j < qualityLevelList.size(); j++) {
                        ReportQualityEntry rqe = qualityLevelList.get(j);
                        List<ReportQualityReason> reasonList = rqe.getReasonList();
                        if(reasonList==null||reasonList.size()==0){
                            text+= rqe.getLevelName()+": "+rqe.getQuantity()+",";
                        }else{
                            text+= rqe.getLevelName()+"(";
                            for (int k = 0; k < reasonList.size(); k++) {
                                ReportQualityReason rqr = reasonList.get(k);
                                text+=rqr.getReasonName()+": "+rqr.getQuantity()+", ";
                            }
                            text+="),";
                        }
                    }
                    jsonObject.put("text",text);
                }
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


}
