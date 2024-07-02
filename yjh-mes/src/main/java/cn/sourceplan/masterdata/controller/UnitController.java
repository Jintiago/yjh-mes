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
import cn.sourceplan.masterdata.domain.Unit;
import cn.sourceplan.masterdata.service.IUnitService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 计量单位Controller
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@RestController
@RequestMapping("/masterdata/unit")
public class UnitController extends BaseController
{
    @Autowired
    private IUnitService unitService;

    /**
     * 查询计量单位列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Unit unit)
    {
        startPage();
        List<Unit> list = unitService.selectUnitList(unit);
        return getDataTable(list);
    }

    /**
     * 导出计量单位列表
     */
    @Log(title = "计量单位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Unit unit)
    {
        List<Unit> list = unitService.selectUnitList(unit);
        ExcelUtil<Unit> util = new ExcelUtil<Unit>(Unit.class);
        util.exportExcel(response, list, "计量单位数据");
    }

    /**
     * 获取计量单位详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(unitService.selectUnitById(id));
    }

    /**
     * 新增计量单位
     */
    @Log(title = "计量单位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Unit unit)
    {
        return toAjax(unitService.insertUnit(unit));
    }

    /**
     * 修改计量单位
     */
    @Log(title = "计量单位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Unit unit)
    {
        return unitService.updateUnit(unit);
    }

    /**
     * 删除计量单位
     */
    @Log(title = "计量单位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return unitService.deleteUnitByIds(ids);
    }
}
