package cn.sourceplan.equipment.controller;

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
import cn.sourceplan.equipment.domain.InspectionItem;
import cn.sourceplan.equipment.service.IInspectionItemService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 设备项目Controller
 *
 * @author jinzhong
 * @date 2023-04-08
 */
@RestController
@RequestMapping("/equipment/inspectionItem")
public class InspectionItemController extends BaseController
{
    @Autowired
    private IInspectionItemService inspectionItemService;

    /**
     * 查询设备项目列表
     */
    @GetMapping("/list")
    public TableDataInfo list(InspectionItem inspectionItem)
    {
        startPage();
        List<InspectionItem> list = inspectionItemService.selectInspectionItemList(inspectionItem);
        return getDataTable(list);
    }

    /**
     * 导出设备项目列表
     */
    @Log(title = "设备项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InspectionItem inspectionItem)
    {
        List<InspectionItem> list = inspectionItemService.selectInspectionItemList(inspectionItem);
        ExcelUtil<InspectionItem> util = new ExcelUtil<InspectionItem>(InspectionItem.class);
        util.exportExcel(response, list, "设备项目数据");
    }

    /**
     * 获取设备项目详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(inspectionItemService.selectInspectionItemById(id));
    }

    /**
     * 新增设备项目
     */
    @Log(title = "设备项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionItem inspectionItem)
    {
        return toAjax(inspectionItemService.insertInspectionItem(inspectionItem));
    }

    /**
     * 修改设备项目
     */
    @Log(title = "设备项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionItem inspectionItem)
    {
        return toAjax(inspectionItemService.updateInspectionItem(inspectionItem));
    }

    /**
     * 删除设备项目
     */
    @Log(title = "设备项目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(inspectionItemService.deleteInspectionItemByIds(ids));
    }
}
