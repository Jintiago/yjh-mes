package cn.sourceplan.masterdata.controller;

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
import cn.sourceplan.masterdata.domain.UnitChange;
import cn.sourceplan.masterdata.service.IUnitChangeService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 计量单位转换Controller
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@RestController
@RequestMapping("/masterdata/unitChange")
public class UnitChangeController extends BaseController
{
    @Autowired
    private IUnitChangeService unitChangeService;

    /**
     * 查询计量单位转换列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UnitChange unitChange)
    {
        startPage();
        List<UnitChange> list = unitChangeService.selectUnitChangeList(unitChange);
        return getDataTable(list);
    }

    /**
     * 导出计量单位转换列表
     */
    @Log(title = "计量单位转换", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UnitChange unitChange)
    {
        List<UnitChange> list = unitChangeService.selectUnitChangeList(unitChange);
        ExcelUtil<UnitChange> util = new ExcelUtil<UnitChange>(UnitChange.class);
        util.exportExcel(response, list, "计量单位转换数据");
    }

    /**
     * 获取计量单位转换详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(unitChangeService.selectUnitChangeById(id));
    }

    /**
     * 新增计量单位转换
     */
    @Log(title = "计量单位转换", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UnitChange unitChange)
    {
        return toAjax(unitChangeService.insertUnitChange(unitChange));
    }

    /**
     * 修改计量单位转换
     */
    @PreAuthorize("@ss.hasPermi('masterdata:unitChange:edit')")
    @Log(title = "计量单位转换", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UnitChange unitChange)
    {
        return toAjax(unitChangeService.updateUnitChange(unitChange));
    }

    /**
     * 删除计量单位转换
     */
    @PreAuthorize("@ss.hasPermi('masterdata:unitChange:remove')")
    @Log(title = "计量单位转换", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(unitChangeService.deleteUnitChangeByIds(ids));
    }
}
