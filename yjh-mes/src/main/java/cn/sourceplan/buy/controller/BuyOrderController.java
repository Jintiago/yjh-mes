package cn.sourceplan.buy.controller;

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
import cn.sourceplan.buy.domain.BuyOrder;
import cn.sourceplan.buy.service.IBuyOrderService;
import cn.sourceplan.common.utils.poi.ExcelUtil;
import cn.sourceplan.common.core.page.TableDataInfo;

/**
 * 采购订单Controller
 * 
 * @author jinzhong
 * @date 2023-08-30
 */
@RestController
@RequestMapping("/buy/buyOrder")
public class BuyOrderController extends BaseController
{
    @Autowired
    private IBuyOrderService buyOrderService;

    /**
     * 查询采购订单列表
     */
    @PreAuthorize("@ss.hasPermi('buy:buyOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(BuyOrder buyOrder)
    {
        startPage();
        List<BuyOrder> list = buyOrderService.selectBuyOrderList(buyOrder);
        return getDataTable(list);
    }

    /**
     * 导出采购订单列表
     */
    @PreAuthorize("@ss.hasPermi('buy:buyOrder:export')")
    @Log(title = "采购订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BuyOrder buyOrder)
    {
        List<BuyOrder> list = buyOrderService.selectBuyOrderList(buyOrder);
        ExcelUtil<BuyOrder> util = new ExcelUtil<BuyOrder>(BuyOrder.class);
        util.exportExcel(response, list, "采购订单数据");
    }

    /**
     * 获取采购订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('buy:buyOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(buyOrderService.selectBuyOrderById(id));
    }

    /**
     * 新增采购订单
     */
    @PreAuthorize("@ss.hasPermi('buy:buyOrder:add')")
    @Log(title = "采购订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BuyOrder buyOrder)
    {
        return toAjax(buyOrderService.insertBuyOrder(buyOrder));
    }

    /**
     * 修改采购订单
     */
    @PreAuthorize("@ss.hasPermi('buy:buyOrder:edit')")
    @Log(title = "采购订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BuyOrder buyOrder)
    {
        return toAjax(buyOrderService.updateBuyOrder(buyOrder));
    }

    /**
     * 删除采购订单
     */
    @PreAuthorize("@ss.hasPermi('buy:buyOrder:remove')")
    @Log(title = "采购订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(buyOrderService.deleteBuyOrderByIds(ids));
    }
}
