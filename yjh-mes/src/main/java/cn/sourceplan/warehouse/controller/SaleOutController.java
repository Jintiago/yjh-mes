package cn.sourceplan.warehouse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.system.service.ISysConfigService;
import cn.sourceplan.warehouse.service.ISaleOutService;
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
import cn.sourceplan.warehouse.domain.SaleOut;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 销售出库单主表Controller
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@RestController
@RequestMapping("/warehouse/saleOut")
public class SaleOutController extends BaseController
{
    @Autowired
    private ISaleOutService saleOutService;

    /**
     * 查询销售出库单主表列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SaleOut saleOut)
    {
        startPage();
        List<SaleOut> list = saleOutService.selectSaleOutList(saleOut);
        return getDataTable(list);
    }

    /**
     * 导出销售出库单主表列表
     */
    @Log(title = "销售出库单主表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SaleOut saleOut)
    {
        List<SaleOut> list = saleOutService.selectSaleOutList(saleOut);
        ExcelUtil<SaleOut> util = new ExcelUtil<SaleOut>(SaleOut.class);
        util.exportExcel(response, list, "销售出库单主表数据");
    }

    /**
     * 获取销售出库单主表详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(saleOutService.selectSaleOutById(id));
    }

    /**
     * 新增销售出库单主表
     */
    @Log(title = "销售出库单主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaleOut saleOut)
    {
        return saleOutService.insertSaleOut(saleOut);
    }

    /**
     * 修改销售出库单主表
     */
    @Log(title = "销售出库单主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaleOut saleOut)
    {

        return saleOutService.updateSaleOut(saleOut);
    }

    /**
     * 删除销售出库单主表
     */
    @Log(title = "销售出库单主表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(saleOutService.deleteSaleOutByIds(ids));
    }
}
