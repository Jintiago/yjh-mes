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
import cn.sourceplan.quality.domain.ReportQuality;
import cn.sourceplan.quality.service.IReportQualityService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 报工质检单Controller
 *
 * @author jinzhong
 * @date 2023-02-21
 */
@RestController
@RequestMapping("/quality/reportQuality")
public class ReportQualityController extends BaseController
{
    @Autowired
    private IReportQualityService reportQualityService;

    /**
     * 查询报工质检单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ReportQuality reportQuality)
    {
        startPage();
        List<ReportQuality> list = reportQualityService.selectReportQualityList(reportQuality);
        return getDataTable(list);
    }

    /**
     * 导出报工质检单列表
     */
    @Log(title = "报工质检单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ReportQuality reportQuality)
    {
        List<ReportQuality> list = reportQualityService.selectReportQualityList(reportQuality);
        ExcelUtil<ReportQuality> util = new ExcelUtil<ReportQuality>(ReportQuality.class);
        util.exportExcel(response, list, "报工质检单数据");
    }

    /**
     * 获取报工质检单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(reportQualityService.selectReportQualityById(id));
    }

    /**
     * 新增报工质检单
     */
    @Log(title = "报工质检单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReportQuality reportQuality)
    {
        return toAjax(reportQualityService.insertReportQuality(reportQuality));
    }

    /**
     * 修改报工质检单
     */
    @Log(title = "报工质检单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReportQuality reportQuality)
    {
        return toAjax(reportQualityService.updateReportQuality(reportQuality));
    }

    /**
     * 删除报工质检单
     */
    @Log(title = "报工质检单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(reportQualityService.deleteReportQualityByIds(ids));
    }
}
