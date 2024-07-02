package cn.sourceplan.quality.controller;

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
import cn.sourceplan.quality.domain.Reason;
import cn.sourceplan.quality.service.IReasonService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 质检原因Controller
 *
 * @author jinzhong
 * @date 2023-02-21
 */
@RestController
@RequestMapping("/quality/reason")
public class ReasonController extends BaseController
{
    @Autowired
    private IReasonService reasonService;

    /**
     * 查询质检原因列表
     */
    @PreAuthorize("@ss.hasPermi('quality:reason:list')")
    @GetMapping("/list")
    public TableDataInfo list(Reason reason)
    {
        startPage();
        List<Reason> list = reasonService.selectReasonList(reason);
        return getDataTable(list);
    }

    /**
     * 导出质检原因列表
     */
    @PreAuthorize("@ss.hasPermi('quality:reason:export')")
    @Log(title = "质检原因", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Reason reason)
    {
        List<Reason> list = reasonService.selectReasonList(reason);
        ExcelUtil<Reason> util = new ExcelUtil<Reason>(Reason.class);
        util.exportExcel(response, list, "质检原因数据");
    }

    /**
     * 获取质检原因详细信息
     */
    @PreAuthorize("@ss.hasPermi('quality:reason:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(reasonService.selectReasonById(id));
    }

    /**
     * 新增质检原因
     */
    @PreAuthorize("@ss.hasPermi('quality:reason:add')")
    @Log(title = "质检原因", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Reason reason)
    {
        return toAjax(reasonService.insertReason(reason));
    }

    /**
     * 修改质检原因
     */
    @PreAuthorize("@ss.hasPermi('quality:reason:edit')")
    @Log(title = "质检原因", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Reason reason)
    {
        return reasonService.updateReason(reason);
    }

    /**
     * 删除质检原因
     */
    @PreAuthorize("@ss.hasPermi('quality:reason:remove')")
    @Log(title = "质检原因", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return reasonService.deleteReasonByIds(ids);
    }
}
