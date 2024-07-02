package cn.sourceplan.finance.controller;

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
import cn.sourceplan.finance.domain.PieceWagePlan;
import cn.sourceplan.finance.service.IPieceWagePlanService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 计件工资方案Controller
 *
 * @author jinzhong
 * @date 2023-03-09
 */
@RestController
@RequestMapping("/finance/pieceWagePlan")
public class PieceWagePlanController extends BaseController
{
    @Autowired
    private IPieceWagePlanService pieceWagePlanService;

    /**
     * 查询计件工资方案列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PieceWagePlan pieceWagePlan)
    {
        startPage();
        List<PieceWagePlan> list = pieceWagePlanService.selectPieceWagePlanList(pieceWagePlan);
        return getDataTable(list);
    }

    /**
     * 导出计件工资方案列表
     */
    @Log(title = "计件工资方案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PieceWagePlan pieceWagePlan)
    {
        List<PieceWagePlan> list = pieceWagePlanService.selectPieceWagePlanList(pieceWagePlan);
        ExcelUtil<PieceWagePlan> util = new ExcelUtil<PieceWagePlan>(PieceWagePlan.class);
        util.exportExcel(response, list, "计件工资方案数据");
    }

    /**
     * 获取计件工资方案详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(pieceWagePlanService.selectPieceWagePlanById(id));
    }


    /**
     * 保存计件工资方案
     */
    @Log(title = "计件工资方案", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public AjaxResult save(@RequestBody PieceWagePlan pieceWagePlan)
    {
        if(pieceWagePlan.getId()==null){
            return toAjax(pieceWagePlanService.insertPieceWagePlan(pieceWagePlan));
        }
        return toAjax(pieceWagePlanService.updatePieceWagePlan(pieceWagePlan));
    }

    /**
     * 新增计件工资方案
     */
    @Log(title = "计件工资方案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PieceWagePlan pieceWagePlan)
    {
        return toAjax(pieceWagePlanService.insertPieceWagePlan(pieceWagePlan));
    }

    /**
     * 修改计件工资方案
     */
    @Log(title = "计件工资方案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PieceWagePlan pieceWagePlan)
    {
        return toAjax(pieceWagePlanService.updatePieceWagePlan(pieceWagePlan));
    }

    /**
     * 删除计件工资方案
     */
    @Log(title = "计件工资方案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(pieceWagePlanService.deletePieceWagePlanByIds(ids));
    }
}
