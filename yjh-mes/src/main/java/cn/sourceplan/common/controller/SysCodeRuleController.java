package cn.sourceplan.common.controller;

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
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 编码规则Controller
 *
 * @author jinzhong
 * @date 2023-01-17
 */
@RestController
@RequestMapping("/common/sysCodeRule")
public class SysCodeRuleController extends BaseController
{
    @Autowired
    private ISysCodeRuleService sysCodeRuleService;


    /**
     * 查询编码规则列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysCodeRule sysCodeRule)
    {
        startPage();
        List<SysCodeRule> list = sysCodeRuleService.selectSysCodeRuleList(sysCodeRule);
        return getDataTable(list);
    }

    /**
     * 获取/更新编码
     */
    @GetMapping("/queryNewCodeById")
    public AjaxResult queryNewCodeById(Long codeRuleId,Boolean isUpdate){

        String s = sysCodeRuleService.queryNewCodeById(codeRuleId, isUpdate);

        return AjaxResult.success(s);
    }


    /**
     * 导出编码规则列表
     */
    @Log(title = "编码规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCodeRule sysCodeRule)
    {
        List<SysCodeRule> list = sysCodeRuleService.selectSysCodeRuleList(sysCodeRule);
        ExcelUtil<SysCodeRule> util = new ExcelUtil<SysCodeRule>(SysCodeRule.class);
        util.exportExcel(response, list, "编码规则数据");
    }

    /**
     * 获取编码规则详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysCodeRuleService.selectSysCodeRuleById(id));
    }

    /**
     * 新增编码规则
     */
    @Log(title = "编码规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCodeRule sysCodeRule)
    {
        return toAjax(sysCodeRuleService.insertSysCodeRule(sysCodeRule));
    }

    /**
     * 修改编码规则
     */
    @PreAuthorize("@ss.hasPermi('common:sysCodeRule:edit')")
    @Log(title = "编码规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCodeRule sysCodeRule)
    {
        return toAjax(sysCodeRuleService.updateSysCodeRule(sysCodeRule));
    }

    /**
     * 删除编码规则
     */
    @PreAuthorize("@ss.hasPermi('common:sysCodeRule:remove')")
    @Log(title = "编码规则", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysCodeRuleService.deleteSysCodeRuleByIds(ids));
    }
}
