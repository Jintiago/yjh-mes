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
import cn.sourceplan.masterdata.domain.MaterialClass;
import cn.sourceplan.masterdata.service.IMaterialClassService;
import cn.sourceplan.common.utils.poi.ExcelUtil;

/**
 * 物料分类Controller
 *
 * @author jinzhong
 * @date 2022-09-13
 */
@RestController
@RequestMapping("/masterdata/materialClass")
public class MaterialClassController extends BaseController
{
    @Autowired
    private IMaterialClassService materialClassService;

    /**
     * 查询物料分类列表
     */
    @GetMapping("/list")
    public AjaxResult list(MaterialClass materialClass)
    {
        List<MaterialClass> list = materialClassService.selectMaterialClassList(materialClass);
        return AjaxResult.success(list);
    }

    /**
     * 导出物料分类列表
     */
    @Log(title = "物料分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialClass materialClass)
    {
        List<MaterialClass> list = materialClassService.selectMaterialClassList(materialClass);
        ExcelUtil<MaterialClass> util = new ExcelUtil<MaterialClass>(MaterialClass.class);
        util.exportExcel(response, list, "物料分类数据");
    }

    /**
     * 获取物料分类详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(materialClassService.selectMaterialClassById(id));
    }

    /**
     * 新增物料分类
     */
    @Log(title = "物料分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MaterialClass materialClass)
    {
        return toAjax(materialClassService.insertMaterialClass(materialClass));
    }

    /**
     * 修改物料分类
     */
    @Log(title = "物料分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MaterialClass materialClass)
    {
        return materialClassService.updateMaterialClass(materialClass);
    }

    /**
     * 删除物料分类
     */
    @Log(title = "物料分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return materialClassService.deleteMaterialClassByIds(ids);
    }
}
