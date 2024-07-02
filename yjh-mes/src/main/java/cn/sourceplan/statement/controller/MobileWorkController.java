package cn.sourceplan.statement.controller;

import com.alibaba.fastjson2.JSONObject;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.statement.service.MobileWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 移动端工作台Controller
 *
 * @author jinzhong
 * @date 2023-04-03
 */
@RestController
@RequestMapping("/statement/mobileWork")
public class MobileWorkController extends BaseController {
    @Autowired
    private MobileWorkService mobileWorkService;




    /**
     * 1.查询执行单据数量
     * 2.生产质量
     * 3.报工统计
     */
    @GetMapping(value = "/executeBillCount")
    public AjaxResult executeBillCount(HttpServletRequest httpServletRequest) {

        JSONObject jsonObject =  mobileWorkService.executeBillCount(httpServletRequest);
        return AjaxResult.success(jsonObject);
    }



}
