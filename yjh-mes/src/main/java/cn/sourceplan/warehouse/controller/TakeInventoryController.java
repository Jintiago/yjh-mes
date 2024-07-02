package cn.sourceplan.warehouse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.warehouse.domain.TakeInventory;
import cn.sourceplan.warehouse.service.ITakeInventoryService;
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
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 库存盘点Controller
 *
 * @author jinzhong
 * @date 2023-07-20
 */
@RestController
@RequestMapping("/warehouse/takeInventory")
public class TakeInventoryController extends BaseController
{
    @Autowired
    private ITakeInventoryService takeInventoryService;

    /**
     * 查询库存盘点列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:takeInventory:list')")
    @GetMapping("/list")
    public TableDataInfo list(TakeInventory takeInventory)
    {
        startPage();
        List<TakeInventory> list = takeInventoryService.selectTakeInventoryList(takeInventory);
        return getDataTable(list);
    }

    /**
     * 导出库存盘点列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:takeInventory:export')")
    @Log(title = "库存盘点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TakeInventory takeInventory)
    {
        List<TakeInventory> list = takeInventoryService.selectTakeInventoryList(takeInventory);
        ExcelUtil<TakeInventory> util = new ExcelUtil<TakeInventory>(TakeInventory.class);
        util.exportExcel(response, list, "库存盘点数据");
    }

    /**
     * 获取库存盘点详细信息
     */
    @PreAuthorize("@ss.hasPermi('warehouse:takeInventory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(takeInventoryService.selectTakeInventoryById(id));
    }

    /**
     * 新增库存盘点
     */
    @PreAuthorize("@ss.hasPermi('warehouse:takeInventory:add')")
    @Log(title = "库存盘点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TakeInventory takeInventory)
    {
        return toAjax(takeInventoryService.insertTakeInventory(takeInventory));
    }

    /**
     * 修改库存盘点
     */
    @PreAuthorize("@ss.hasPermi('warehouse:takeInventory:edit')")
    @Log(title = "库存盘点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TakeInventory takeInventory)
    {
        return toAjax(takeInventoryService.updateTakeInventory(takeInventory));
    }

    /**
     * 删除库存盘点
     */
    @PreAuthorize("@ss.hasPermi('warehouse:takeInventory:remove')")
    @Log(title = "库存盘点", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(takeInventoryService.deleteTakeInventoryByIds(ids));
    }
}
