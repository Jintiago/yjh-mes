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
import cn.sourceplan.masterdata.domain.Bom;
import cn.sourceplan.masterdata.service.IBomService;
import cn.sourceplan.common.utils.poi.ExcelUtil;

/**
 * 物料BOMController
 *
 * @author jinzhong
 * @date 2022-09-16
 */
@RestController
@RequestMapping("/masterdata/bom")
public class BomController extends BaseController
{
    @Autowired
    private IBomService bomService;

    /**
     * 查询物料BOM列表
     */
    @GetMapping("/list")
    public AjaxResult list(Bom bom)
    {
        List<Bom> list = bomService.selectBomList(bom);
        return AjaxResult.success(list);
    }

    /**
     * 导出物料BOM列表
     */
    @Log(title = "物料BOM", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Bom bom)
    {
        List<Bom> list = bomService.selectBomList(bom);
        ExcelUtil<Bom> util = new ExcelUtil<Bom>(Bom.class);
        util.exportExcel(response, list, "物料BOM数据");
    }

    /**
     * 获取物料BOM详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(bomService.selectBomById(id));
    }

    /**
     * 新增物料BOM
     */
    @Log(title = "物料BOM", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Bom bom)
    {
        return bomService.insertBom(bom);
    }

    /**
     * 修改物料BOM
     */
    @Log(title = "物料BOM", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Bom bom)
    {
        return toAjax(bomService.updateBom(bom));
    }

    /**
     * 删除物料BOM
     */
    @Log(title = "物料BOM", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bomService.deleteBomByIds(ids));
    }
}
