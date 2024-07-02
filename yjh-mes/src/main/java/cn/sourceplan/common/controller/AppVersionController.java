package cn.sourceplan.common.controller;

import cn.sourceplan.common.annotation.Log;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.core.page.TableDataInfo;
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.enums.BusinessType;
import cn.sourceplan.common.mapper.AppVersionMapper;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * APP版本号
 *
 * @author jinzhong
 * @date 2023-01-17
 */
@RestController
@RequestMapping("/common/appVersion")
public class AppVersionController extends BaseController
{
    @Autowired
    private AppVersionMapper appVersionMapper;


    /**
     * 查询app当前版本号
     */
    @GetMapping(value="/query")
    public AjaxResult query()
    {
        Double query = appVersionMapper.query();
        return AjaxResult.success(query);
    }


}
