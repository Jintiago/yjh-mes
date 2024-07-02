package cn.sourceplan.buy.service;

import java.util.List;
import cn.sourceplan.buy.domain.BuyOrder;

/**
 * 采购订单Service接口
 * 
 * @author jinzhong
 * @date 2023-08-30
 */
public interface IBuyOrderService 
{
    /**
     * 查询采购订单
     * 
     * @param id 采购订单主键
     * @return 采购订单
     */
    public BuyOrder selectBuyOrderById(Long id);

    /**
     * 查询采购订单列表
     * 
     * @param buyOrder 采购订单
     * @return 采购订单集合
     */
    public List<BuyOrder> selectBuyOrderList(BuyOrder buyOrder);

    /**
     * 新增采购订单
     * 
     * @param buyOrder 采购订单
     * @return 结果
     */
    public int insertBuyOrder(BuyOrder buyOrder);

    /**
     * 修改采购订单
     * 
     * @param buyOrder 采购订单
     * @return 结果
     */
    public int updateBuyOrder(BuyOrder buyOrder);

    /**
     * 批量删除采购订单
     * 
     * @param ids 需要删除的采购订单主键集合
     * @return 结果
     */
    public int deleteBuyOrderByIds(Long[] ids);

    /**
     * 删除采购订单信息
     * 
     * @param id 采购订单主键
     * @return 结果
     */
    public int deleteBuyOrderById(Long id);
}
