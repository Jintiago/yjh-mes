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
import cn.sourceplan.finance.domain.Bomoweihu;
import cn.sourceplan.finance.service.IBomoweihuService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 天润薄膜核算维护Controller
 *
 * @author jinzhong
 * @date 2023-07-04
 */
@RestController
@RequestMapping("/finance/bomoweihu")
public class BomoweihuController extends BaseController
{
    @Autowired
    private IBomoweihuService bomoweihuService;

    /**
     * 查询天润薄膜核算维护列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Bomoweihu bomoweihu)
    {
        startPage();
        List<Bomoweihu> list = bomoweihuService.selectBomoweihuList(bomoweihu);
        return getDataTable(list);
    }

    /**
     * 导出天润薄膜核算维护列表
     */
    @Log(title = "天润薄膜核算维护", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Bomoweihu bomoweihu)
    {
        List<Bomoweihu> list = bomoweihuService.selectBomoweihuList(bomoweihu);
        ExcelUtil<Bomoweihu> util = new ExcelUtil<Bomoweihu>(Bomoweihu.class);
        util.exportExcel(response, list, "天润薄膜核算维护数据");
    }

    /**
     * 获取天润薄膜核算维护详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bomoweihuService.selectBomoweihuById(id));
    }

    /**
     * 新增天润薄膜核算维护
     */
    @Log(title = "天润薄膜核算维护", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Bomoweihu bomoweihu)
    {
        return toAjax(bomoweihuService.insertBomoweihu(bomoweihu));
    }

    /**
     * 修改天润薄膜核算维护
     */
    @Log(title = "天润薄膜核算维护", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Bomoweihu bomoweihu)
    {
        return toAjax(bomoweihuService.updateBomoweihu(bomoweihu));
    }

    /**
     * 删除天润薄膜核算维护
     */
    @Log(title = "天润薄膜核算维护", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bomoweihuService.deleteBomoweihuByIds(ids));
    }
}
