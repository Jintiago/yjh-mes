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
import cn.sourceplan.masterdata.domain.Workshop;
import cn.sourceplan.masterdata.service.IWorkshopService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 车间Controller
 *
 * @author jinzhong
 * @date 2022-09-21
 */
@RestController
@RequestMapping("/masterdata/workshop")
public class WorkshopController extends BaseController
{
    @Autowired
    private IWorkshopService workshopService;

    /**
     * 查询车间列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Workshop workshop)
    {
        startPage();
        List<Workshop> list = workshopService.selectWorkshopList(workshop);
        return getDataTable(list);
    }

    /**
     * 导出车间列表
     */
    @Log(title = "车间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Workshop workshop)
    {
        List<Workshop> list = workshopService.selectWorkshopList(workshop);
        ExcelUtil<Workshop> util = new ExcelUtil<Workshop>(Workshop.class);
        util.exportExcel(response, list, "车间数据");
    }

    /**
     * 获取车间详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(workshopService.selectWorkshopById(id));
    }

    /**
     * 新增车间
     */
    @Log(title = "车间", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Workshop workshop)
    {
        return workshopService.insertWorkshop(workshop);
    }

    /**
     * 修改车间
     */
    @Log(title = "车间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Workshop workshop)
    {

        return workshopService.updateWorkshop(workshop);
    }

    /**
     * 删除车间
     */
    @Log(title = "车间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return workshopService.deleteWorkshopByIds(ids);
    }
}
