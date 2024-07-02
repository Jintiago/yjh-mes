package cn.sourceplan.finance.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.common.utils.FieldExtendUtil;
import cn.sourceplan.finance.domain.SaleOutAccountEntry;
import com.alibaba.fastjson2.JSONArray;
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
import cn.sourceplan.finance.domain.SaleOutAccountBill;
import cn.sourceplan.finance.service.ISaleOutAccountBillService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 销售出库对账单Controller
 *
 * @author jinzhong
 * @date 2023-08-01
 */
@RestController
@RequestMapping("/finance/saleOutAccountBill")
public class SaleOutAccountBillController extends BaseController
{
    @Autowired
    private ISaleOutAccountBillService saleOutAccountBillService;

    /**
     * 查询销售出库对账单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:saleOutAccountBill:list')")
    @GetMapping("/list")
    public TableDataInfo list(SaleOutAccountBill saleOutAccountBill)
    {

        startPage();
        List<SaleOutAccountEntry> list = saleOutAccountBillService.selectSaleOutAccountBillList(saleOutAccountBill);
        return getDataTable(list);
    }

    /**
     * 导出销售出库对账单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:saleOutAccountBill:export')")
    @Log(title = "销售出库对账单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SaleOutAccountBill saleOutAccountBill)
    {
        List<SaleOutAccountEntry> list = saleOutAccountBillService.selectSaleOutAccountBillList(saleOutAccountBill);
        JSONArray jsonArray = new JSONArray(list);
        try {
            FieldExtendUtil.exportExcelByJson("saleOutAccountEntry",jsonArray,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取销售出库对账单详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:saleOutAccountBill:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(saleOutAccountBillService.selectSaleOutAccountBillById(id));
    }

    /**
     * 新增销售出库对账单
     */
    @PreAuthorize("@ss.hasPermi('finance:saleOutAccountBill:add')")
    @Log(title = "销售出库对账单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaleOutAccountBill saleOutAccountBill)
    {
        return toAjax(saleOutAccountBillService.insertSaleOutAccountBill(saleOutAccountBill));
    }

    /**
     * 修改销售出库对账单
     */
    @PreAuthorize("@ss.hasPermi('finance:saleOutAccountBill:edit')")
    @Log(title = "销售出库对账单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaleOutAccountBill saleOutAccountBill)
    {
        return toAjax(saleOutAccountBillService.updateSaleOutAccountBill(saleOutAccountBill));
    }

    /**
     * 删除销售出库对账单
     */
    @PreAuthorize("@ss.hasPermi('finance:saleOutAccountBill:remove')")
    @Log(title = "销售出库对账单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(saleOutAccountBillService.deleteSaleOutAccountBillByIds(ids));
    }
}
