package cn.sourceplan.sale.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.common.utils.FieldExtendUtil;
import cn.sourceplan.sale.domain.SalOrderEntry;
import com.alibaba.fastjson2.JSONArray;
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
import cn.sourceplan.sale.domain.SalOrder;
import cn.sourceplan.sale.service.ISalOrderService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 销售订单Controller
 *
 * @author jinzhong
 * @date 2022-10-08
 */
@RestController
@RequestMapping("/sale/saleOrder")
public class SalOrderController extends BaseController
{
    @Autowired
    private ISalOrderService salOrderService;

    /**
     * 查询销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(SalOrder salOrder)
    {
        String saleOrderStatus = "";
        if(org.apache.commons.lang3.StringUtils.isNotBlank(salOrder.getStatus())){
            String[] split = salOrder.getStatus().split(",");
            for (int i = 0; i < split.length; i++) {
                saleOrderStatus+="'"+split[i]+"'";
                if(i!=split.length-1){
                    saleOrderStatus+=",";
                }
            }
        }
        salOrder.setStatus(saleOrderStatus);
        startPage();
        List<SalOrderEntry> list = salOrderService.selectSalOrderList(salOrder);
        return getDataTable(list);
    }

    /**
     * 导出销售订单列表
     */
    @Log(title = "销售订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SalOrder salOrder)
    {
        String saleOrderStatus = "";
        if(org.apache.commons.lang3.StringUtils.isNotBlank(salOrder.getStatus())){
            String[] split = salOrder.getStatus().split(",");
            for (int i = 0; i < split.length; i++) {
                saleOrderStatus+="'"+split[i]+"'";
                if(i!=split.length-1){
                    saleOrderStatus+=",";
                }
            }
        }
        salOrder.setStatus(saleOrderStatus);
        List<SalOrderEntry> list = salOrderService.selectSalOrderList(salOrder);
        JSONArray jsonArray = new JSONArray(list);
        try {
            FieldExtendUtil.exportExcelByJson("saleOrderEntry",jsonArray,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取销售订单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(salOrderService.selectSalOrderById(id));
    }

    /**
     * 新增销售订单
     */
    @Log(title = "销售订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SalOrder salOrder)
    {
        return toAjax(salOrderService.insertSalOrder(salOrder));
    }

    /**
     * 修改销售订单
     */
    @Log(title = "销售订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SalOrder salOrder)
    {
        return toAjax(salOrderService.updateSalOrder(salOrder));
    }

    /**
     * 删除销售订单
     */
    @Log(title = "销售订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(salOrderService.deleteSalOrderByIds(ids));
    }


    /**
     * 根据销售订单主表ID查询所有销售订单信息
     */
    @GetMapping("/listEntryByIds")
    public AjaxResult listEntryByIds(String ids)
    {

        if(ids.split(",").length<1){
            return AjaxResult.error("所选单据ID有异常，请联系管理员处理");
        }
        return salOrderService.listByIds(ids);
    }

    @PutMapping("/batchUpdateEntryStatus")
    public AjaxResult batchUpdateEntryStatus(Long[] ids,String status){

        return AjaxResult.success(salOrderService.batchUpdateStatusByEntryIds(StringUtils.join(ids, ","),status));

    }

}
