package cn.sourceplan.warehouse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.warehouse.domain.ProPick;
import cn.sourceplan.warehouse.domain.ProPickEntry;
import cn.sourceplan.warehouse.service.IProPickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.sourceplan.common.annotation.Log;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.enums.BusinessType;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 生产领料单Controller
 *
 * @author jinzhong
 * @date 2023-02-23
 */
@RestController
@RequestMapping("/warehouse/proPick")
public class ProPickController extends BaseController
{
    @Autowired
    private IProPickService proPickService;

    /**
     * 查询生产领料单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ProPick proPick)
    {
        startPage();
        List<ProPickEntry> list = proPickService.selectProPickList(proPick);
        return getDataTable(list);
    }

    /**
     * 导出生产领料单列表
     */
    @Log(title = "生产领料单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProPick proPick)
    {
        List<ProPickEntry> list = proPickService.selectProPickList(proPick);
        ExcelUtil<ProPickEntry> util = new ExcelUtil<ProPickEntry>(ProPickEntry.class);
        util.exportExcel(response, list, "生产领料单数据");
    }

    /**
     * 获取生产领料单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(proPickService.selectProPickById(id));
    }

    /**
     * 新增生产领料单
     */
    @Log(title = "生产领料单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProPick proPick)
    {
        return toAjax(proPickService.insertProPick(proPick));
    }

    /**
     * 修改生产领料单
     */
    @Log(title = "生产领料单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProPick proPick)
    {
        return toAjax(proPickService.updateProPick(proPick));
    }

    /**
     * 删除生产领料单
     */
    @Log(title = "生产领料单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(proPickService.deleteProPickByIds(ids));
    }

    /**
     * 批量审核
     */
    @Log(title = "批量审核生产领料单", businessType = BusinessType.UPDATE)
    @PutMapping("/batchCheck")
    public AjaxResult batchCheck(Long[] ids, String checkStatus)
    {
        int i = proPickService.batchCheck(ids,checkStatus);
        return AjaxResult.success(i);
    }


}
