package cn.sourceplan.statement.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.statement.service.PcIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台首页报表Controller
 *
 * @author jinzhong
 * @date 2023-03-31
 */
@RestController
@RequestMapping("/statement/pcIndex")
public class PcIndexController extends BaseController {
    @Autowired
    private PcIndexService pcIndexService;


    /**
     * 查询产量走势图
     */
    @GetMapping(value = "/outputTrend")
    public AjaxResult outputTrend(HttpServletRequest httpServletRequest) {

        JSONObject list =  pcIndexService.outputTrend(httpServletRequest);
        return AjaxResult.success(list);
    }

    /**
     * 查询工单延期率
     */
    @GetMapping(value = "/defermentFactor")
    public AjaxResult defermentFactor(HttpServletRequest httpServletRequest) {
        JSONObject jsonObject =  pcIndexService.defermentFactor(httpServletRequest);
        return AjaxResult.success(jsonObject);
    }

    /**
     * 生产合格率
     */
    @GetMapping(value = "/qualificationRate")
    public AjaxResult qualificationRate(HttpServletRequest httpServletRequest) {
        JSONObject jsonObject =  pcIndexService.qualificationRate(httpServletRequest);
        return AjaxResult.success(jsonObject);
    }

    /**
     * 不合格原因
     */
    @GetMapping(value = "/unqualifiedReason")
    public AjaxResult unqualifiedReason(HttpServletRequest httpServletRequest) {
        JSONArray jsonArray =  pcIndexService.unqualifiedReason(httpServletRequest);
        return AjaxResult.success(jsonArray);
    }

}
