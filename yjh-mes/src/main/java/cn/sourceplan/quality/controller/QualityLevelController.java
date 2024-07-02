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
import cn.sourceplan.quality.domain.QualityLevel;
import cn.sourceplan.quality.service.IQualityLevelService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 质检等级Controller
 *
 * @author jinzhong
 * @date 2023-02-21
 */
@RestController
@RequestMapping("/quality/qualityLevel")
public class QualityLevelController extends BaseController
{
    @Autowired
    private IQualityLevelService qualityLevelService;

    /**
     * 查询质检等级列表
     */
    @GetMapping("/list")
    public TableDataInfo list(QualityLevel qualityLevel)
    {
        startPage();
        List<QualityLevel> list = qualityLevelService.selectQualityLevelList(qualityLevel);
        return getDataTable(list);
    }

    /**
     * 导出质检等级列表
     */
    @Log(title = "质检等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityLevel qualityLevel)
    {
        List<QualityLevel> list = qualityLevelService.selectQualityLevelList(qualityLevel);
        ExcelUtil<QualityLevel> util = new ExcelUtil<QualityLevel>(QualityLevel.class);
        util.exportExcel(response, list, "质检等级数据");
    }

    /**
     * 获取质检等级详细信息
     */
    @PreAuthorize("@ss.hasPermi('quality:qualityLevel:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qualityLevelService.selectQualityLevelById(id));
    }

    /**
     * 新增质检等级
     */
    @PreAuthorize("@ss.hasPermi('quality:qualityLevel:add')")
    @Log(title = "质检等级", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QualityLevel qualityLevel)
    {
        return toAjax(qualityLevelService.insertQualityLevel(qualityLevel));
    }

    /**
     * 修改质检等级
     */
    @PreAuthorize("@ss.hasPermi('quality:qualityLevel:edit')")
    @Log(title = "质检等级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QualityLevel qualityLevel)
    {
        return qualityLevelService.updateQualityLevel(qualityLevel);
    }

    /**
     * 删除质检等级
     */
    @PreAuthorize("@ss.hasPermi('quality:qualityLevel:remove')")
    @Log(title = "质检等级", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return qualityLevelService.deleteQualityLevelByIds(ids);
    }
}
