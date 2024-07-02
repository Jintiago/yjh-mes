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
import cn.sourceplan.warehouse.domain.WarehouseStation;
import cn.sourceplan.warehouse.service.IWarehouseStationService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 仓库工位绑定Controller
 * 
 * @author jinzhong
 * @date 2023-11-04
 */
@RestController
@RequestMapping("/warehouse/warehouseStation")
public class WarehouseStationController extends BaseController
{
    @Autowired
    private IWarehouseStationService warehouseStationService;

    /**
     * 查询仓库工位绑定列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:warehouseStation:list')")
    @GetMapping("/list")
    public TableDataInfo list(WarehouseStation warehouseStation)
    {
        startPage();
        List<WarehouseStation> list = warehouseStationService.selectWarehouseStationList(warehouseStation);
        return getDataTable(list);
    }

    /**
     * 导出仓库工位绑定列表
     */
    @PreAuthorize("@ss.hasPermi('warehouse:warehouseStation:export')")
    @Log(title = "仓库工位绑定", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WarehouseStation warehouseStation)
    {
        List<WarehouseStation> list = warehouseStationService.selectWarehouseStationList(warehouseStation);
        ExcelUtil<WarehouseStation> util = new ExcelUtil<WarehouseStation>(WarehouseStation.class);
        util.exportExcel(response, list, "仓库工位绑定数据");
    }

    /**
     * 获取仓库工位绑定详细信息
     */
    @PreAuthorize("@ss.hasPermi('warehouse:warehouseStation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(warehouseStationService.selectWarehouseStationById(id));
    }

    /**
     * 新增仓库工位绑定
     */
    @PreAuthorize("@ss.hasPermi('warehouse:warehouseStation:add')")
    @Log(title = "仓库工位绑定", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WarehouseStation warehouseStation)
    {
        return toAjax(warehouseStationService.insertWarehouseStation(warehouseStation));
    }

    /**
     * 修改仓库工位绑定
     */
    @PreAuthorize("@ss.hasPermi('warehouse:warehouseStation:edit')")
    @Log(title = "仓库工位绑定", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WarehouseStation warehouseStation)
    {
        return toAjax(warehouseStationService.updateWarehouseStation(warehouseStation));
    }

    /**
     * 删除仓库工位绑定
     */
    @PreAuthorize("@ss.hasPermi('warehouse:warehouseStation:remove')")
    @Log(title = "仓库工位绑定", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(warehouseStationService.deleteWarehouseStationByIds(ids));
    }
}
