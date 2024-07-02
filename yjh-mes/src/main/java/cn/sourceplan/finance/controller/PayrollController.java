package cn.sourceplan.finance.controller;

import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.sourceplan.common.annotation.Log;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.enums.BusinessType;
import cn.sourceplan.finance.domain.Payroll;
import cn.sourceplan.finance.service.IPayrollService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 工资单Controller
 *
 * @author jinzhong
 * @date 2023-03-11
 */
@RestController
@RequestMapping("/finance/payroll")
public class PayrollController extends BaseController
{
    @Autowired
    private IPayrollService payrollService;

    /**
     * 工资单预览
     *
     * @param ids
     * @return
     */
    @GetMapping("/preview/{ids}")
    public AjaxResult payRollPreview(@PathVariable Long[] ids) {

        AjaxResult ajaxResult = payrollService.payRollPreview(ids);
        return ajaxResult;
    }

    /**
     * 根据用户ID查汇总工资金额
     *
     * @param id
     * @return
     */
    @GetMapping("/sumByUserId")
    public AjaxResult sumByUserId(Long id , HttpServletRequest httpServletRequest) {

        BigDecimal ajaxResult = payrollService.sumByUserId(id,httpServletRequest);
        return AjaxResult.success(ajaxResult);
    }

    /**
     * 查询工资单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Payroll payroll)
    {
        startPage();
        List<Payroll> list = payrollService.selectPayrollList(payroll);
        return getDataTable(list);
    }

    /**
     * 导出工资单列表
     */
    @PreAuthorize("@ss.hasPermi('finance:payroll:export')")
    @Log(title = "工资单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Payroll payroll)
    {
        List<Payroll> list = payrollService.selectPayrollList(payroll);
        ExcelUtil<Payroll> util = new ExcelUtil<Payroll>(Payroll.class);
        util.exportExcel(response, list, "工资单数据");
    }

    /**
     * 获取工资单详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:payroll:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(payrollService.selectPayrollById(id));
    }

    /**
     * 批量新增工资单
     */
    @Log(title = "报工单", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public AjaxResult batchSave(@RequestBody List<Payroll> list)
    {
        int count =0;
        for (int i = 0; i < list.size(); i++) {
            count+=payrollService.insertPayroll(list.get(i));
        }
        return toAjax(count);
    }

    /**
     * 新增工资单
     */
    @PreAuthorize("@ss.hasPermi('finance:payroll:add')")
    @Log(title = "工资单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Payroll payroll)
    {
        return toAjax(payrollService.insertPayroll(payroll));
    }

    /**
     * 修改工资单
     */
    @PreAuthorize("@ss.hasPermi('finance:payroll:edit')")
    @Log(title = "工资单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Payroll payroll)
    {
        return toAjax(payrollService.updatePayroll(payroll));
    }

    /**
     * 删除工资单
     */
    @PreAuthorize("@ss.hasPermi('finance:payroll:remove')")
    @Log(title = "工资单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(payrollService.deletePayrollByIds(ids));
    }
}
