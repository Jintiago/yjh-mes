package cn.sourceplan.production.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.common.utils.FieldExtendUtil;
import cn.sourceplan.common.utils.SqlCheckUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.sourceplan.common.annotation.Log;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.enums.BusinessType;
import cn.sourceplan.production.domain.WorkOrder;
import cn.sourceplan.production.service.IWorkOrderService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 工单Controller
 *
 * @author jinzhong
 * @date 2022-12-12
 */
@RestController
@RequestMapping("/production/workOrder")
public class WorkOrderController extends BaseController
{
    @Autowired
    private IWorkOrderService workOrderService;

    /**
     * 查询工单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(WorkOrder workOrder)
    {
        TableDataInfo tableDataInfo = workOrderService.selectWorkOrderList(workOrder,false);
        return tableDataInfo;
    }

    /**
     * 移动端查询工单列表
     */
    @GetMapping("/listForMobile")
    public TableDataInfo listForMobile(WorkOrder workOrder)
    {
        return  workOrderService.listForMobile(workOrder);
    }

    /**
     * 导出工单列表
     */
    @Log(title = "工单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WorkOrder workOrder)
    {

        TableDataInfo tableDataInfo = workOrderService.selectWorkOrderList(workOrder,true);
        List<WorkOrder> rows = (List<WorkOrder>)tableDataInfo.getRows();
        JSONArray jsonArray = new JSONArray(rows);
        try {
            FieldExtendUtil.exportExcelByJson("workOrder",jsonArray,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取工单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(workOrderService.selectWorkOrderById(id));
    }

    /**
     * 根据工单子表ID获取工单详细信息
     */
    @GetMapping(value = "/getInfoByEntryId/{id}")
    public AjaxResult getInfoByEntryId(@PathVariable("id") Long id)
    {
        return success(workOrderService.selectWorkOrderByEntryId(id));
    }


    /**
     * 新增工单
     */
    @Log(title = "工单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WorkOrder workOrder)
    {
        return toAjax(workOrderService.insertWorkOrder(workOrder));
    }


    /**
     * 批量新增工单
     *
     */
    @Log(title = "工单", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public AjaxResult batchAdd(@RequestBody List<WorkOrder> workOrderList)
    {
        return toAjax(workOrderService.batchInsertWorkOrder(workOrderList));
    }

    /**
     *
     * 以json形式接收
     * 新增工单
     * 传递多个工单时，将所有工单的规格型号，和数量汇总，
     * 实际仍然生成一个工单
     */
    @Log(title = "工单", businessType = BusinessType.INSERT)
    @PostMapping("/batchAddByJson")
    public AjaxResult batchAddByJson(@RequestBody JSONObject jsonObject)
    {
        return workOrderService.batchAddByJson(jsonObject);
    }

    /**
     * 修改工单
     */
    @Log(title = "工单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WorkOrder workOrder)
    {
        return workOrderService.updateWorkOrder(workOrder);
    }


    /**
     * 修改工单优先级
     */
    @Log(title = "工单", businessType = BusinessType.UPDATE)
    @PutMapping("/editPriority")
    public AjaxResult editPriority(@RequestBody WorkOrder workOrder)
    {
        Integer priority = workOrder.getPriority();
        Long id = workOrder.getId();
        Object o = SqlCheckUtil.executeCustomSql("Update pro_workorder Set priority = " + priority + " Where id =" + id);
        return AjaxResult.success();
    }

    /**
     * 删除工单
     */
    @Log(title = "工单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return workOrderService.deleteWorkOrderByIds(ids);
    }

    /**
     * 批量修改生产状态
     */
    @PutMapping("/editProStatus")
    public AjaxResult editProStatus(Long[] ids,String proStatus)
    {
        return workOrderService.updateProStatus(ids,proStatus);
    }

    /**
     * 工单排产计划预览
     */
    @GetMapping("/preview")
    public AjaxResult preview(String ids,String billType)
    {
        if(ids.split(",").length<1){
            return AjaxResult.error("所选单据ID有异常，请联系管理员处理");
        }
        return workOrderService.preview(ids,billType);
    }


    /**
     * 查询上一道工序的报工数据
     * salOrderEntryIds 销售订单分录ids
     * sort 当前工序顺序
     */
    @GetMapping("/getFrontProcessReport")
    public AjaxResult getFrontProcessReport(String salOrderEntryIds,int sort)
    {
        String[] split = salOrderEntryIds.split("," );
        Long[] lArr = new Long[split.length];
        for (int i = 0; i < split.length; i++) {
            lArr[i] = Long.valueOf(split[i]);
        }
        return AjaxResult.success(workOrderService.getFrontProcessReport(lArr,sort)) ;
    }

    /**
     * 给工单进行自定义排序
     */
    @PostMapping("/updateCustomSort")
    public AjaxResult updateCustomSort(@RequestBody List<WorkOrder> workOrderList)
    {
        int i = workOrderService.updateCustomSort(workOrderList);

        return AjaxResult.success();
    }

}
