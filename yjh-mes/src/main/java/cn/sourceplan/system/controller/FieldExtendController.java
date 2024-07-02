package cn.sourceplan.system.controller;

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
import cn.sourceplan.system.domain.FieldExtend;
import cn.sourceplan.system.service.IFieldExtendService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 字段扩展Controller
 *
 * @author jinzhong
 * @date 2023-05-23
 */
@RestController
@RequestMapping("/system/fieldExtend")
public class FieldExtendController extends BaseController
{
    @Autowired
    private IFieldExtendService fieldExtendService;

    /**
     * 查询字段扩展列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FieldExtend fieldExtend)
    {
        startPage();
        List<FieldExtend> list = fieldExtendService.selectFieldExtendList(fieldExtend);
        return getDataTable(list);
    }

    /**
     * 导出字段扩展列表
     */
    @Log(title = "字段扩展", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FieldExtend fieldExtend)
    {
        List<FieldExtend> list = fieldExtendService.selectFieldExtendList(fieldExtend);
        ExcelUtil<FieldExtend> util = new ExcelUtil<FieldExtend>(FieldExtend.class);
        util.exportExcel(response, list, "字段扩展数据");
    }

    /**
     * 获取字段扩展详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fieldExtendService.selectFieldExtendById(id));
    }

    /**
     * 新增字段扩展
     */
    @Log(title = "字段扩展", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody List<FieldExtend> fieldExtend)
    {
        return toAjax(fieldExtendService.insertFieldExtend(fieldExtend));
    }

    /**
     * 修改字段扩展
     */
    @Log(title = "字段扩展", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FieldExtend fieldExtend)
    {
        return toAjax(fieldExtendService.updateFieldExtend(fieldExtend));
    }

    /**
     * 删除字段扩展
     */
    @Log(title = "字段扩展", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fieldExtendService.deleteFieldExtendByIds(ids));
    }
}
