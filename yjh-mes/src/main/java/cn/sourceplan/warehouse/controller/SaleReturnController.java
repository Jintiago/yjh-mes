package cn.sourceplan.warehouse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.warehouse.service.ISaleReturnService;
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
import cn.sourceplan.warehouse.domain.SaleReturn;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 销售退货单Controller
 *
 * @author jinzhong
 * @date 2023-08-04
 */
@RestController
@RequestMapping("/warehouse/saleReturn")
public class SaleReturnController extends BaseController
{
    @Autowired
    private ISaleReturnService saleReturnService;

    /**
     * 查询销售退货单列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:saleReturn:list')")
    @GetMapping("/list")
    public TableDataInfo list(SaleReturn saleReturn)
    {
        startPage();
        List<SaleReturn> list = saleReturnService.selectSaleReturnList(saleReturn);
        return getDataTable(list);
    }

    /**
     * 导出销售退货单列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:saleReturn:export')")
    @Log(title = "销售退货单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SaleReturn saleReturn)
    {
        List<SaleReturn> list = saleReturnService.selectSaleReturnList(saleReturn);
        ExcelUtil<SaleReturn> util = new ExcelUtil<SaleReturn>(SaleReturn.class);
        util.exportExcel(response, list, "销售退货单数据");
    }

    /**
     * 获取销售退货单详细信息
     */
    @PreAuthorize("@ss.hasPermi('warehouse:saleReturn:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(saleReturnService.selectSaleReturnById(id));
    }

    /**
     * 新增销售退货单
     */
    @PreAuthorize("@ss.hasPermi('warehouse:saleReturn:add')")
    @Log(title = "销售退货单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaleReturn saleReturn)
    {
        return toAjax(saleReturnService.insertSaleReturn(saleReturn));
    }

    /**
     * 修改销售退货单
     */
    @PreAuthorize("@ss.hasPermi('warehouse:saleReturn:edit')")
    @Log(title = "销售退货单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaleReturn saleReturn)
    {
        return toAjax(saleReturnService.updateSaleReturn(saleReturn));
    }

    /**
     * 删除销售退货单
     */
    @PreAuthorize("@ss.hasPermi('warehouse:saleReturn:remove')")
    @Log(title = "销售退货单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(saleReturnService.deleteSaleReturnByIds(ids));
    }
}
