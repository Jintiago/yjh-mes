package cn.sourceplan.system.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.page.TableDataInfo;
import cn.sourceplan.common.utils.SecurityUtils;
import cn.sourceplan.equipment.domain.RepairOrder;
import cn.sourceplan.equipment.service.IRepairOrderService;
import cn.sourceplan.finance.domain.Payroll;
import cn.sourceplan.finance.service.IPayrollService;
import cn.sourceplan.production.domain.Report;
import cn.sourceplan.production.domain.WorkOrder;
import cn.sourceplan.production.service.IReportService;
import cn.sourceplan.production.service.IWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动端接口Controller
 *
 * @author jinzhong
 * @date 2023-04-19
 */
@RestController
@RequestMapping("/system/mobile")
public class MobileApiController extends BaseController
{
    @Autowired
    private IWorkOrderService workOrderService;

    @Autowired
    private IReportService reportService;

    @Autowired
    private IRepairOrderService repairOrderService;

    @Autowired
    private IPayrollService payrollService;
    /**
     * 查询待办事项
     */
    @GetMapping("/toDoList")
    public JSONArray toDoList()
    {
        JSONArray jsonArray = new JSONArray();
        //查询未完成的工单
        JSONObject jA = new JSONObject();
        WorkOrder workOrder = new WorkOrder();
        workOrder.setMobileStatus("B");
        TableDataInfo tableDataInfo = workOrderService.listForMobile(workOrder);
        long total = tableDataInfo.getTotal();
            jA.put("text","未完成的生产工单");
            jA.put("count",total+"");
            jA.put("url","/pages/production/workOrder/index");
            jsonArray.add(jA);
        //查询未完成报修单
        JSONObject jB = new JSONObject();
            //如果有设备维修角色，进行查询
        RepairOrder repairOrder = new RepairOrder();
        repairOrder.setStatus("'B'");
        List<RepairOrder> repairOrders = repairOrderService.selectRepairOrderList(repairOrder);
            jB.put("text","待处理的设备报修单");
            jB.put("count",repairOrders.size()+"");
            jB.put("url","/pages/equipment/repairOrder/index");
            jsonArray.add(jB);
        //查询未完成的质检单
        JSONObject jC = new JSONObject();
            //具有质检员或报工质检员权限的，进行查询
        Report report = new Report();
        report.setQualityStatus("B");
        report.setStatus("A");
        List<Report> reports = reportService.selectReportList(report);
            jC.put("text","未完成的质检单");
            jC.put("count",reports.size()+"");
            jC.put("url","/pages/production/report/index");
            jsonArray.add(jC);
        //查询本月的工资单
        JSONObject jD = new JSONObject();
        Payroll payroll = new Payroll();
        payroll.setUserId(SecurityUtils.getUserId());
        DateTime beginDate = DateUtil.beginOfMonth(new Date());
        DateTime nextMonth = DateUtil.nextMonth();
        DateTime endDate = DateUtil.beginOfMonth(nextMonth);
        Map<String, Object> params = new HashMap<>();
        params.put("beginPayrollDate",beginDate);
        params.put("endPayrollDate",endDate);
        payroll.setParams(params);
        List<Payroll> payrolls = payrollService.selectPayrollList(payroll);
            jD.put("url","/pages/finance/payroll/index");
            jD.put("text","查看本月工资单");
            jD.put("count",payrolls.size()+"");
            jsonArray.add(jD);
        return jsonArray;
    }

}
