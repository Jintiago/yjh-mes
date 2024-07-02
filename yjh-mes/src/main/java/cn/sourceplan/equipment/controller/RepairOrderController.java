package cn.sourceplan.equipment.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import cn.sourceplan.equipment.domain.RepairOrder;
import cn.sourceplan.equipment.service.IRepairOrderService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 设备维修单Controller
 *
 * @author jinzhong
 * @date 2023-04-08
 */
@RestController
@RequestMapping("/equipment/repairOrder")
public class RepairOrderController extends BaseController
{
    @Autowired
    private IRepairOrderService repairOrderService;

    /**
     * 查询设备维修单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(RepairOrder repairOrder)
    {
        String status = "";
        if(StringUtils.isNotBlank(repairOrder.getStatus())){
            String[] split = repairOrder.getStatus().split(",");
            for (int i = 0; i < split.length; i++) {
                status+="'"+split[i]+"'";
                if(i!=split.length-1){
                    status+=",";
                }
            }
        }
        repairOrder.setStatus(status);
        startPage();
        List<RepairOrder> list = repairOrderService.selectRepairOrderList(repairOrder);
        return getDataTable(list);
    }

    /**
     * 导出设备维修单列表
     */
    @Log(title = "设备维修单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RepairOrder repairOrder)
    {
        List<RepairOrder> list = repairOrderService.selectRepairOrderList(repairOrder);
        ExcelUtil<RepairOrder> util = new ExcelUtil<RepairOrder>(RepairOrder.class);
        util.exportExcel(response, list, "设备维修单数据");
    }

    /**
     * 获取设备维修单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(repairOrderService.selectRepairOrderById(id));
    }

    /**
     * 新增设备维修单
     */
    @Log(title = "设备维修单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RepairOrder repairOrder)
    {
        return toAjax(repairOrderService.insertRepairOrder(repairOrder));
    }

    /**
     * 修改设备维修单
     */
    @Log(title = "设备维修单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RepairOrder repairOrder)
    {
        return toAjax(repairOrderService.updateRepairOrder(repairOrder));
    }

    /**
     * 删除设备维修单
     */
    @Log(title = "设备维修单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(repairOrderService.deleteRepairOrderByIds(ids));
    }
}
