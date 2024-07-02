package cn.sourceplan.statement.service.impl;

import com.alibaba.fastjson2.JSONObject;
import cn.sourceplan.common.utils.SecurityUtils;
import cn.sourceplan.common.utils.SqlCheckUtil;
import cn.sourceplan.finance.service.IPayrollService;
import cn.sourceplan.statement.mapper.MobileWorkMapper;
import cn.sourceplan.statement.service.MobileWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Service
public class MobileWorkServiceImpl implements MobileWorkService {

    @Autowired
    MobileWorkMapper mobileWorkMapper;

    @Autowired
    private IPayrollService payrollService;

    @Override
    public JSONObject executeBillCount(HttpServletRequest httpServletRequest) {
        String beginDate = httpServletRequest.getParameter("beginDate");
        String endDate = httpServletRequest.getParameter("endDate");
        Long userId = SecurityUtils.getUserId();
        //根据当前人id查询
        JSONObject jsonObject =new JSONObject();
        //单据数量
            //生产工单
        int gongdanI = SqlCheckUtil.sqlCheck("( select pwo.id from  pro_report prp " +
                        " left join pro_workorder_entry pwe on pwe.id = prp.work_order_entry_id " +
                        " left join pro_workorder pwo on pwo.id = pwe.workorder_id ",
                " where prp.report_time >='"+beginDate+"' " +
                        "  and prp.report_time<='"+endDate+"' " +
                        " and prp.report_user_id = "+userId+" " +
                        " GROUP BY pwo.id ) a");
        jsonObject.put("gongdan",gongdanI);
            //报工单
        int baogongI = SqlCheckUtil.sqlCheck("pro_report",
                "where report_time >='"+beginDate+"' " +
                        " and report_time<='"+endDate+"' " +
                        " and report_user_id ="+userId+" ");
        jsonObject.put("baogong",baogongI);
            //质检单
        int zhijianI = SqlCheckUtil.sqlCheck("qc_report_quality",
                "where check_time >='"+beginDate+"' " +
                        " and check_time<='"+endDate+"' " +
                        " and check_user_id ="+userId+" ");
        jsonObject.put("zhijian",zhijianI);
            //领料单
        int lingliaoI = SqlCheckUtil.sqlCheck("wm_production_pick",
                " where pick_time >='"+beginDate+"' " +
                        " and pick_time<='"+endDate+"' " +
                        " and pick_user_id ="+userId+"");
        jsonObject.put("lingliao",lingliaoI);
            //入库单
        int rukuI = SqlCheckUtil.sqlCheck("wm_manufacture_into",
                " where into_date >='"+beginDate+"' " +
                        " and into_date<='"+endDate+"' " +
                        " and deliverer_id ="+userId+" ");
        jsonObject.put("ruku",rukuI);
        //生产质量
        JSONObject pqResult = mobileWorkMapper.productionQuality(beginDate,endDate,userId);
        jsonObject.put("pqResult",pqResult);
        //工资统计
            //调用工资单接口
            BigDecimal bigDecimal = payrollService.sumByUserId(userId, httpServletRequest);
            jsonObject.put("paySum",bigDecimal);
        return jsonObject;
    }


}
