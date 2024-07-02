package cn.sourceplan.warehouse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.warehouse.domain.ManufactureInto;
import cn.sourceplan.warehouse.service.IManufactureIntoService;
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
 * 完工入库单Controller
 *
 * @author jinzhong
 * @date 2022-11-10
 */
@RestController
@RequestMapping("/warehouse/manufactureInto")
public class ManufactureIntoController extends BaseController
{
    @Autowired
    private IManufactureIntoService manufactureIntoService;

    /**
     * 查询完工入库单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ManufactureInto manufactureInto)
    {
        startPage();
        List<ManufactureInto> list = manufactureIntoService.selectManufactureIntoList(manufactureInto);
        return getDataTable(list);
    }

    /**
     * 导出完工入库单列表
     */
    @Log(title = "完工入库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ManufactureInto manufactureInto)
    {
        List<ManufactureInto> list = manufactureIntoService.selectManufactureIntoList(manufactureInto);
        ExcelUtil<ManufactureInto> util = new ExcelUtil<ManufactureInto>(ManufactureInto.class);
        util.exportExcel(response, list, "完工入库单数据");
    }

    /**
     * 获取完工入库单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(manufactureIntoService.selectManufactureIntoById(id));
    }

    /**
     * 新增完工入库单
     */
    @Log(title = "完工入库单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ManufactureInto manufactureInto)
    {
        return toAjax(manufactureIntoService.insertManufactureInto(manufactureInto));
    }

    /**
     * 修改完工入库单
     */
    @Log(title = "完工入库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ManufactureInto manufactureInto)
    {
        return toAjax(manufactureIntoService.updateManufactureInto(manufactureInto));
    }

    /**
     * 删除完工入库单
     */
    @Log(title = "完工入库单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(manufactureIntoService.deleteManufactureIntoByIds(ids));
    }
}
