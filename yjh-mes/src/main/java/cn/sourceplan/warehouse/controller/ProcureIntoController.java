package cn.sourceplan.warehouse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.warehouse.domain.ProcureIntoEntry;
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
import cn.sourceplan.warehouse.domain.ProcureInto;
import cn.sourceplan.warehouse.service.IProcureIntoService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 采购入库单Controller
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@RestController
@RequestMapping("/warehouse/procureInto")
public class ProcureIntoController extends BaseController
{
    @Autowired
    private IProcureIntoService procureIntoService;

    /**
     * 查询采购入库单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ProcureInto procureInto)
    {
        startPage();
        List<ProcureIntoEntry> list = procureIntoService.selectProcureIntoList(procureInto);
        return getDataTable(list);
    }

    /**
     * 导出采购入库单列表
     */
    @Log(title = "采购入库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProcureInto procureInto)
    {
        List<ProcureIntoEntry> list = procureIntoService.selectProcureIntoList(procureInto);
        ExcelUtil<ProcureIntoEntry> util = new ExcelUtil<>(ProcureIntoEntry.class);
        util.exportExcel(response, list, "采购入库单数据");
    }

    /**
     * 获取采购入库单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(procureIntoService.selectProcureIntoById(id));
    }

    /**
     * 新增采购入库单
     */
    @Log(title = "采购入库单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProcureInto procureInto)
    {
        return toAjax(procureIntoService.insertProcureInto(procureInto));
    }

    /**
     * 修改采购入库单
     */
    @Log(title = "采购入库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProcureInto procureInto)
    {
        return toAjax(procureIntoService.updateProcureInto(procureInto));
    }

    /**
     * 删除采购入库单
     */
    @Log(title = "采购入库单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(procureIntoService.deleteProcureIntoByIds(ids));
    }
}
