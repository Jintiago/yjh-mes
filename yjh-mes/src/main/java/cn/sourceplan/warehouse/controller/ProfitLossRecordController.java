package cn.sourceplan.warehouse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.warehouse.domain.ProfitLossRecord;
import cn.sourceplan.warehouse.service.IProfitLossRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.sourceplan.common.annotation.Log;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.enums.BusinessType;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 盘点盈亏记录Controller
 *
 * @author jinzhong
 * @date 2023-07-20
 */
@RestController
@RequestMapping("/warehouse/profitLossRecord")
public class ProfitLossRecordController extends BaseController
{
    @Autowired
    private IProfitLossRecordService profitLossRecordService;

    /**
     * 查询盘点盈亏记录列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:profitLossRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProfitLossRecord profitLossRecord)
    {
        startPage();
        List<ProfitLossRecord> list = profitLossRecordService.selectProfitLossRecordList(profitLossRecord);
        return getDataTable(list);
    }

    /**
     * 导出盘点盈亏记录列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:profitLossRecord:export')")
    @Log(title = "盘点盈亏记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProfitLossRecord profitLossRecord)
    {
        List<ProfitLossRecord> list = profitLossRecordService.selectProfitLossRecordList(profitLossRecord);
        ExcelUtil<ProfitLossRecord> util = new ExcelUtil<ProfitLossRecord>(ProfitLossRecord.class);
        util.exportExcel(response, list, "盘点盈亏记录数据");
    }

    /**
     * 获取盘点盈亏记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('warehouse:profitLossRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(profitLossRecordService.selectProfitLossRecordById(id));
    }


    /**
     * 根据盘点单下推生成盘盈记录单
     */
    @GetMapping(value = "/addByTakeInventory/{id}")
    public AjaxResult addByTakeInventory(@PathVariable Long id)
    {
        AjaxResult a = profitLossRecordService.addByTakeInventory(id);
        return a;
    }

    /**
     * 新增盘点盈亏记录
     */
    @PreAuthorize("@ss.hasPermi('warehouse:profitLossRecord:add')")
    @Log(title = "盘点盈亏记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProfitLossRecord profitLossRecord)
    {
        return toAjax(profitLossRecordService.insertProfitLossRecord(profitLossRecord));
    }

    /**
     * 修改盘点盈亏记录
     */
    @PreAuthorize("@ss.hasPermi('warehouse:profitLossRecord:edit')")
    @Log(title = "盘点盈亏记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProfitLossRecord profitLossRecord)
    {
        return toAjax(profitLossRecordService.updateProfitLossRecord(profitLossRecord));
    }

    /**
     * 删除盘点盈亏记录
     */
    @PreAuthorize("@ss.hasPermi('warehouse:profitLossRecord:remove')")
    @Log(title = "盘点盈亏记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(profitLossRecordService.deleteProfitLossRecordByIds(ids));
    }
}
