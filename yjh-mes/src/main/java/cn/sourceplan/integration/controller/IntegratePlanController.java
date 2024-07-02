package cn.sourceplan.integration.controller;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import cn.sourceplan.common.annotation.Log;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.core.page.TableDataInfo;
import cn.sourceplan.common.enums.BusinessType;
import cn.sourceplan.common.utils.SqlCheckUtil;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.integration.domain.IntegratePlan;
import cn.sourceplan.integration.service.IIntegratePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 集成方案Controller
 *
 * @author 靳忠
 * @date 2023-02-13
 */
@RestController
@RequestMapping("/integratePlan")
public class IntegratePlanController extends BaseController
{
    @Autowired
    private IIntegratePlanService integratePlanService;

    /**
     * 查询集成方案列表
     */
    @GetMapping("/list")
    public TableDataInfo list(IntegratePlan integratePlan)
    {
        startPage();
        List<IntegratePlan> list = integratePlanService.selectIntegratePlanList(integratePlan);
        return getDataTable(list);
    }

    /**
     * 导出集成方案列表
     */
    @Log(title = "集成方案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, IntegratePlan integratePlan)
    {
        List<IntegratePlan> list = integratePlanService.selectIntegratePlanList(integratePlan);
        ExcelUtil<IntegratePlan> util = new ExcelUtil<IntegratePlan>(IntegratePlan.class);
        util.exportExcel(response, list, "集成方案数据");
    }

    /**
     * 获取集成方案详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(integratePlanService.selectIntegratePlanById(id));
    }

    /**
     * 新增集成方案
     */
    @Log(title = "集成方案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IntegratePlan integratePlan) throws ClassNotFoundException {

        return integratePlanService.insertIntegratePlan(integratePlan);
    }

    /**
     * 修改集成方案
     */
    @Log(title = "集成方案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IntegratePlan integratePlan)
    {
        return integratePlanService.updateIntegratePlan(integratePlan);
    }

    /**
     * 删除集成方案
     */
    @Log(title = "集成方案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(integratePlanService.deleteIntegratePlanByIds(ids));
    }



    /**
     * 查询mes系统实体中的所有属性
     * domainName  实体名
     */
    @GetMapping("/mesFieldList")
    public AjaxResult mesFieldList(@RequestParam("domainName") String domainName)
    {
        Class<?> domainClass = null;
        String tableName="";
        try {

            String module ="masterdata";
            if("SalOrder".equals(domainName)){
                module="sale";
            }
            if("WorkOrder".equals(domainName)){
                module="production";
            }
            domainClass = Class.forName("cn.sourceplan."+module+".domain."+domainName);
             tableName = SqlHelper.table(domainClass).getTableName();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Map<String, String>> tableAllFiled = SqlCheckUtil.getTableAllFiled(tableName);
        return AjaxResult.success(tableAllFiled);
    }
}
