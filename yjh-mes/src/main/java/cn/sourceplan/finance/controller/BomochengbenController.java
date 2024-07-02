package cn.sourceplan.finance.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.finance.domain.BomochengbenNeimao;
import cn.sourceplan.finance.domain.BomochengbenWaimao;
import cn.sourceplan.finance.service.IBomochengbenNeimaoService;
import cn.sourceplan.finance.service.IBomochengbenWaimaoService;
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
import cn.sourceplan.finance.domain.Bomochengben;
import cn.sourceplan.finance.service.IBomochengbenService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 薄膜核算Controller
 *
 * @author jinzhong
 * @date 2023-07-04
 */
@RestController
@RequestMapping("/finance/bomochengben")
public class BomochengbenController extends BaseController
{
    @Autowired
    private IBomochengbenService bomochengbenService;

    @Autowired
    private IBomochengbenNeimaoService bomochengbenNeimaoService;

    @Autowired
    private IBomochengbenWaimaoService bomochengbenWaimaoService;

    /**
     * 查询薄膜核算列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Bomochengben bomochengben)
    {
        startPage();
        List<Bomochengben> list = bomochengbenService.selectBomochengbenList(bomochengben);
        return getDataTable(list);
    }

    /**
     * 导出薄膜核算列表
     */
    @Log(title = "薄膜核算", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Bomochengben bomochengben)
    {
        List<Bomochengben> list = bomochengbenService.selectBomochengbenList(bomochengben);
        ExcelUtil<Bomochengben> util = new ExcelUtil<Bomochengben>(Bomochengben.class);
        util.exportExcel(response, list, "薄膜核算数据");
    }

    /**
     * 获取薄膜核算详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bomochengbenService.selectBomochengbenById(id));
    }

    /**
     * 新增薄膜核算
     */
    @Log(title = "薄膜核算", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Bomochengben bomochengben)
    {
        return toAjax(bomochengbenService.insertBomochengben(bomochengben));
    }

    /**
     * 修改薄膜核算
     */
    @Log(title = "薄膜核算", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Bomochengben bomochengben)
    {
        return toAjax(bomochengbenService.updateBomochengben(bomochengben));
    }

    /**
     * 删除薄膜核算
     */
    @Log(title = "薄膜核算", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bomochengbenService.deleteBomochengbenByIds(ids));
    }


    /*内贸部分 ----------------------------------------------------------------------------------------------------*/

    /**
     * 查询薄膜核算列表
     */
    @GetMapping("/listNeimao")
    public TableDataInfo listNeimao(BomochengbenNeimao bomochengben)
    {
        startPage();
        List<BomochengbenNeimao> list = bomochengbenNeimaoService.selectBomochengbenNeimaoList(bomochengben);
        return getDataTable(list);
    }

    /**
     * 导出薄膜核算列表
     */
    @Log(title = "薄膜核算", businessType = BusinessType.EXPORT)
    @PostMapping("/exportNeimao")
    public void export(HttpServletResponse response, BomochengbenNeimao bomochengben)
    {
        List<BomochengbenNeimao> list = bomochengbenNeimaoService.selectBomochengbenNeimaoList(bomochengben);
        ExcelUtil<BomochengbenNeimao> util = new ExcelUtil<BomochengbenNeimao>(BomochengbenNeimao.class);
        util.exportExcel(response, list, "薄膜核算数据");
    }

    /**
     * 获取薄膜核算详细信息
     */
    @GetMapping(value = "/neimao/{id}")
    public AjaxResult getInfoByNeimao(@PathVariable("id") Long id)
    {
        return success(bomochengbenNeimaoService.selectBomochengbenNeimaoById(id));
    }

    /**
     * 新增薄膜核算
     */
    @Log(title = "薄膜核算", businessType = BusinessType.INSERT)
    @PostMapping("/neimao")
    public AjaxResult addByNeimao(@RequestBody BomochengbenNeimao bomochengben)
    {
        return toAjax(bomochengbenNeimaoService.insertBomochengbenNeimao(bomochengben));
    }

    /**
     * 修改薄膜核算
     */
    @Log(title = "薄膜核算", businessType = BusinessType.UPDATE)
    @PutMapping("/neimao")
    public AjaxResult editByNeimao(@RequestBody BomochengbenNeimao bomochengben)
    {
        return toAjax(bomochengbenNeimaoService.updateBomochengbenNeimao(bomochengben));
    }

    /**
     * 删除薄膜核算
     */
    @Log(title = "薄膜核算", businessType = BusinessType.DELETE)
    @DeleteMapping("/neimao/{ids}")
    public AjaxResult removeByNeimao(@PathVariable Long[] ids)
    {
        return toAjax(bomochengbenNeimaoService.deleteBomochengbenNeimaoByIds(ids));
    }

    /*  外贸 -----------------------------------------------------------------------------------------------*/
    /**
     * 查询薄膜核算列表
     */
    @GetMapping("/listWaimao")
    public TableDataInfo listWaimao(BomochengbenWaimao bomochengben)
    {
        startPage();
        List<BomochengbenWaimao> list = bomochengbenWaimaoService.selectBomochengbenWaimaoList(bomochengben);
        return getDataTable(list);
    }

    /**
     * 导出薄膜核算列表
     */
    @Log(title = "薄膜核算", businessType = BusinessType.EXPORT)
    @PostMapping("/exportWaimao")
    public void export(HttpServletResponse response, BomochengbenWaimao bomochengben)
    {
        List<BomochengbenWaimao> list = bomochengbenWaimaoService.selectBomochengbenWaimaoList(bomochengben);
        ExcelUtil<BomochengbenWaimao> util = new ExcelUtil<BomochengbenWaimao>(BomochengbenWaimao.class);
        util.exportExcel(response, list, "薄膜核算数据");
    }

    /**
     * 获取薄膜核算详细信息
     */
    @GetMapping(value = "/waimao/{id}")
    public AjaxResult getInfoByWaimao(@PathVariable("id") Long id)
    {
        return success(bomochengbenWaimaoService.selectBomochengbenWaimaoById(id));
    }

    /**
     * 新增薄膜核算
     */
    @Log(title = "薄膜核算", businessType = BusinessType.INSERT)
    @PostMapping("/waimao")
    public AjaxResult addByWaimao(@RequestBody BomochengbenWaimao bomochengben)
    {
        return toAjax(bomochengbenWaimaoService.insertBomochengbenWaimao(bomochengben));
    }

    /**
     * 修改薄膜核算
     */
    @Log(title = "薄膜核算", businessType = BusinessType.UPDATE)
    @PutMapping("/waimao")
    public AjaxResult editByWaimao(@RequestBody BomochengbenWaimao bomochengben)
    {
        return toAjax(bomochengbenWaimaoService.updateBomochengbenWaimao(bomochengben));
    }

    /**
     * 删除薄膜核算
     */
    @Log(title = "薄膜核算", businessType = BusinessType.DELETE)
    @DeleteMapping("/waimao/{ids}")
    public AjaxResult removeByWaimao(@PathVariable Long[] ids)
    {
        return toAjax(bomochengbenWaimaoService.deleteBomochengbenWaimaoByIds(ids));
    }

}
