package cn.sourceplan.buy.mapper;

import java.util.List;
import cn.sourceplan.buy.domain.BuyOrder;
import cn.sourceplan.buy.domain.BuyOrderEntry;

/**
 * 采购订单Mapper接口
 * 
 * @author jinzhong
 * @date 2023-08-30
 */
public interface BuyOrderMapper 
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
     * 删除采购订单
     * 
     * @param id 采购订单主键
     * @return 结果
     */
    public int deleteBuyOrderById(Long id);

    /**
     * 批量删除采购订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBuyOrderByIds(Long[] ids);

    /**
     * 批量删除采购订单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBuyOrderEntryByMainIds(Long[] ids);
    
    /**
     * 批量新增采购订单明细
     * 
     * @param buyOrderEntryList 采购订单明细列表
     * @return 结果
     */
    public int batchBuyOrderEntry(List<BuyOrderEntry> buyOrderEntryList);
    

    /**
     * 通过采购订单主键删除采购订单明细信息
     * 
     * @param id 采购订单ID
     * @return 结果
     */
    public int deleteBuyOrderEntryByMainId(Long id);
}
