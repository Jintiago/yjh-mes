package cn.sourceplan.common.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.common.utils.SecurityUtils;
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
import cn.sourceplan.common.domain.SysUserStation;
import cn.sourceplan.common.service.ISysUserStationService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 用户工位绑定Controller
 *
 * @author jinzhong
 * @date 2023-02-07
 */
@RestController
@RequestMapping("/common/sysUserStation")
public class SysUserStationController extends BaseController
{
    @Autowired
    private ISysUserStationService sysUserStationService;

    /**
     * 查询用户工位绑定列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysUserStation sysUserStation)
    {
        startPage();
        List<SysUserStation> list = sysUserStationService.selectSysUserStationList(sysUserStation);
        return getDataTable(list);
    }

    /**
     * 根据当前登录用户ID查询工位绑定信息
     */
    @GetMapping("/getInfoByUserId")
    public AjaxResult getInfoByUserId()
    {

        Long userId = SecurityUtils.getUserId();
        SysUserStation data =  sysUserStationService.getInfoByUserId(userId);
        return  AjaxResult.success(data) ;
    }

    /**
     * 导出用户工位绑定列表
     */
    @Log(title = "用户工位绑定", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUserStation sysUserStation)
    {
        List<SysUserStation> list = sysUserStationService.selectSysUserStationList(sysUserStation);
        ExcelUtil<SysUserStation> util = new ExcelUtil<SysUserStation>(SysUserStation.class);
        util.exportExcel(response, list, "用户工位绑定数据");
    }

    /**
     * 获取用户工位绑定详细信息
     */
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long id)
    {
        return success(sysUserStationService.selectSysUserStationById(id));
    }

    /**
     * 新增用户工位绑定
     */
    @Log(title = "用户工位绑定", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUserStation sysUserStation)
    {
        return toAjax(sysUserStationService.insertSysUserStation(sysUserStation));
    }

    /**
     * 修改用户工位绑定
     */
    @Log(title = "用户工位绑定", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUserStation sysUserStation)
    {
        return toAjax(sysUserStationService.updateSysUserStation(sysUserStation));
    }

    /**
     * 删除用户工位绑定
     */
    @Log(title = "用户工位绑定", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysUserStationService.deleteSysUserStationByIds(ids));
    }

    /**
     * 移动端扫码绑定用户工位
     */
    @Log(title = "移动端扫码绑定用户工位", businessType = BusinessType.INSERT)
    @PostMapping("/mobileBind")
    public AjaxResult mobileBindUserStation(@RequestBody SysUserStation sysUserStation)
    {
        return toAjax(sysUserStationService.mobileBindUserStation(sysUserStation));
    }

}
