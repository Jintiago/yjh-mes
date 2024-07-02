package cn.sourceplan.warehouse.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.sourceplan.common.utils.FieldExtendUtil;
import cn.sourceplan.warehouse.domain.RealTimeInventory;
import cn.sourceplan.warehouse.service.IRealTimeInventoryService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.sourceplan.common.annotation.Log;
import cn.sourceplan.common.core.controller.BaseController;
import cn.sourceplan.common.enums.BusinessType;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 即时库存Controller
 *
 * @author jinzhong
 * @date 2022-11-29
 */
@RestController
@RequestMapping("/warehouse/inventory")
public class RealTimeInventoryController extends BaseController
{
    @Autowired
    private IRealTimeInventoryService realTimeInventoryService;

    /**
     * 查询即时库存列表
     */
    @GetMapping("/list")
    public TableDataInfo list(RealTimeInventory realTimeInventory)
    {
        startPage();
        List<JSONObject> list = realTimeInventoryService.selectRealTimeInventoryList(realTimeInventory);
        return getDataTable(list);
    }


    /**
     * 查询有库存的规格型号
     * 用于用户输入规格型号时给出建议，如现有即时库存
     * 1060x5.3x8000m库存100个
     * 1060x5.3x8000m库存100个
     * 1060x5.4x8000m库存100个
     * 用户输入1060x5.3时 返回数组1060x5.3x8000m，1060x5.3x8000m
     */
    @GetMapping("/getSpecificationList")
    public TableDataInfo getSpecificationList(RealTimeInventory realTimeInventory)
    {
        startPage();
        List<String> list = realTimeInventoryService.getSpecificationList(realTimeInventory);
        return getDataTable(list);
    }

    /**
     * 导出即时库存列表
     */
    @Log(title = "即时库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RealTimeInventory realTimeInventory)
    {
        List<JSONObject> list = realTimeInventoryService.selectRealTimeInventoryList(realTimeInventory);
        JSONArray jsonArray = new JSONArray(list);
        try {
            FieldExtendUtil.exportExcelByJson("realTimeInventory",jsonArray,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
