package cn.sourceplan.barcode.controller;

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
import cn.sourceplan.barcode.domain.MaterialSn;
import cn.sourceplan.barcode.service.IMaterialSnService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 物料SN码Controller
 *
 * @author jinzhong
 * @date 2023-04-15
 */
@RestController
@RequestMapping("/barcode/materialSn")
public class MaterialSnController extends BaseController
{
    @Autowired
    private IMaterialSnService materialSnService;

    /**
     * 查询物料SN码列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MaterialSn materialSn)
    {
        startPage();
        List<MaterialSn> list = materialSnService.selectMaterialSnList(materialSn);
        return getDataTable(list);
    }

    /**
     * 导出物料SN码列表
     */
    @Log(title = "物料SN码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialSn materialSn)
    {
        List<MaterialSn> list = materialSnService.selectMaterialSnList(materialSn);
        ExcelUtil<MaterialSn> util = new ExcelUtil<MaterialSn>(MaterialSn.class);
        util.exportExcel(response, list, "物料SN码数据");
    }

    /**
     * 获取物料SN码详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(materialSnService.selectMaterialSnById(id));
    }

    /**
     * 新增物料SN码
     */
    @Log(title = "物料SN码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MaterialSn materialSn)
    {
        return toAjax(materialSnService.insertMaterialSn(materialSn));
    }

    /**
     * 修改物料SN码
     */
    @Log(title = "物料SN码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MaterialSn materialSn)
    {
        return toAjax(materialSnService.updateMaterialSn(materialSn));
    }

    /**
     * 删除物料SN码
     */
    @PreAuthorize("@ss.hasPermi('barcode:materialSn:remove')")
    @Log(title = "物料SN码", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(materialSnService.deleteMaterialSnByIds(ids));
    }
}
