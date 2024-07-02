package cn.sourceplan.warehouse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import cn.sourceplan.warehouse.domain.OtherOut;
import cn.sourceplan.warehouse.service.IOtherOutService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 其他出库单主表Controller
 * 
 * @author jinzhong
 * @date 2023-09-28
 */
@RestController
@RequestMapping("/warehouse/otherOut")
public class OtherOutController extends BaseController
{
    @Autowired
    private IOtherOutService otherOutService;

    /**
     * 查询其他出库单主表列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:otherOut:list')")
    @GetMapping("/list")
    public TableDataInfo list(OtherOut otherOut)
    {
        startPage();
        List<OtherOut> list = otherOutService.selectOtherOutList(otherOut);
        return getDataTable(list);
    }

    /**
     * 导出其他出库单主表列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:otherOut:export')")
    @Log(title = "其他出库单主表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OtherOut otherOut)
    {
        List<OtherOut> list = otherOutService.selectOtherOutList(otherOut);
        ExcelUtil<OtherOut> util = new ExcelUtil<OtherOut>(OtherOut.class);
        util.exportExcel(response, list, "其他出库单主表数据");
    }

    /**
     * 获取其他出库单主表详细信息
     */
    @PreAuthorize("@ss.hasPermi('warehouse:otherOut:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(otherOutService.selectOtherOutById(id));
    }

    /**
     * 新增其他出库单主表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:otherOut:add')")
    @Log(title = "其他出库单主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OtherOut otherOut)
    {
        return toAjax(otherOutService.insertOtherOut(otherOut));
    }

    /**
     * 修改其他出库单主表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:otherOut:edit')")
    @Log(title = "其他出库单主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OtherOut otherOut)
    {
        return toAjax(otherOutService.updateOtherOut(otherOut));
    }

    /**
     * 删除其他出库单主表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:otherOut:remove')")
    @Log(title = "其他出库单主表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(otherOutService.deleteOtherOutByIds(ids));
    }
}
