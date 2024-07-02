package cn.sourceplan.production.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.common.constant.HttpStatus;
import cn.sourceplan.common.core.page.PageDomain;
import cn.sourceplan.common.core.page.TableSupport;
import cn.sourceplan.production.mapper.ReportMapper;
import com.github.pagehelper.PageInfo;
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
import cn.sourceplan.production.domain.Report;
import cn.sourceplan.production.service.IReportService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 报工单Controller
 *
 * @author jinzhong
 * @date 2023-02-15
 */
@RestController
@RequestMapping("/production/report")
public class ReportController extends BaseController
{
    @Autowired
    private IReportService reportService;

    @Autowired
    private ReportMapper reportMapper;

    /**
     * 查询报工单列表
     */

    @GetMapping("/list")
    public TableDataInfo list(Report report)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        Map<String, Object> params = report.getParams();
        params.put("pageNum",(pageNum-1)*pageSize);
        params.put("pageSize",pageSize);
        String[] reportTimeQuery = report.getReportTimeQuery();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (reportTimeQuery!=null&&reportTimeQuery.length>0){
            params.put("reportTimeStart",reportTimeQuery[0]);
            if(reportTimeQuery.length>1){
                params.put("reportTimeEnd",reportTimeQuery[1]+" 23:59:59");
            }else{
                String format = sdf.format(new Date());
                params.put("reportTimeEnd",format+" 23:59:59");
            }
        }

        List<Report> list = reportService.selectReportList(report);
        int i = reportMapper.selectReportListCount(report);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(i);
        return rspData;
    }

    /**
     * 导出报工单列表
     */
    @Log(title = "报工单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Report report)
    {
        List<Report> list = reportService.selectReportList(report);
        ExcelUtil<Report> util = new ExcelUtil<Report>(Report.class);
        util.exportExcel(response, list, "报工单数据");
    }

    /**
     * 获取报工单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(reportService.selectReportById(id));
    }

    /**
     * 批量新增/保存报工单
     */
    @Log(title = "报工单", businessType = BusinessType.INSERT)
    @PostMapping("/batchSave")
    public AjaxResult batchSave(@RequestBody List<Report> reportList)
    {
        int count =0;
        for (int i = 0; i < reportList.size(); i++) {
            count+=reportService.insertReport(reportList.get(i));
        }

        return toAjax(count);
    }

    /**
     * 新增报工单
     */
    @Log(title = "报工单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody List<Report> reportList)
    {
        Report report = reportList.get(0);
        int i = reportService.insertReport(report);
        if(i==0){
            return AjaxResult.error("请检查是否领料");
        }
        if(i==-1){
            return AjaxResult.error("该工位未绑定报工仓库");
        }
        return toAjax(i);
    }


    /**
     * 修改报工单
     */
    @Log(title = "报工单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Report report)
    {

        return reportService.updateReport(report);
    }

    /**
     * 删除报工单
     */
    @Log(title = "报工单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return reportService.deleteReportByIds(ids);
    }
}
