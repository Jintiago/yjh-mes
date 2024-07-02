package cn.sourceplan.buy.service.impl;

import java.util.List;
import cn.sourceplan.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.buy.domain.BuyOrderEntry;
import cn.sourceplan.buy.mapper.BuyOrderMapper;
import cn.sourceplan.buy.domain.BuyOrder;
import cn.sourceplan.buy.service.IBuyOrderService;

/**
 * 采购订单Service业务层处理
 * 
 * @author jinzhong
 * @date 2023-08-30
 */
@Service
public class BuyOrderServiceImpl implements IBuyOrderService 
{
    @Autowired
    private BuyOrderMapper buyOrderMapper;

    /**
     * 查询采购订单
     * 
     * @param id 采购订单主键
     * @return 采购订单
     */
    @Override
    public BuyOrder selectBuyOrderById(Long id)
    {
        return buyOrderMapper.selectBuyOrderById(id);
    }

    /**
     * 查询采购订单列表
     * 
     * @param buyOrder 采购订单
     * @return 采购订单
     */
    @Override
    public List<BuyOrder> selectBuyOrderList(BuyOrder buyOrder)
    {
        return buyOrderMapper.selectBuyOrderList(buyOrder);
    }

    /**
     * 新增采购订单
     * 
     * @param buyOrder 采购订单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertBuyOrder(BuyOrder buyOrder)
    {
        buyOrder.setCreateTime(DateUtils.getNowDate());
        int rows = buyOrderMapper.insertBuyOrder(buyOrder);
        insertBuyOrderEntry(buyOrder);
        return rows;
    }

    /**
     * 修改采购订单
     * 
     * @param buyOrder 采购订单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateBuyOrder(BuyOrder buyOrder)
    {
        buyOrder.setUpdateTime(DateUtils.getNowDate());
        buyOrderMapper.deleteBuyOrderEntryByMainId(buyOrder.getId());
        insertBuyOrderEntry(buyOrder);
        return buyOrderMapper.updateBuyOrder(buyOrder);
    }

    /**
     * 批量删除采购订单
     * 
     * @param ids 需要删除的采购订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteBuyOrderByIds(Long[] ids)
    {
        buyOrderMapper.deleteBuyOrderEntryByMainIds(ids);
        return buyOrderMapper.deleteBuyOrderByIds(ids);
    }

    /**
     * 删除采购订单信息
     * 
     * @param id 采购订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteBuyOrderById(Long id)
    {
        buyOrderMapper.deleteBuyOrderEntryByMainId(id);
        return buyOrderMapper.deleteBuyOrderById(id);
    }

    /**
     * 新增采购订单明细信息
     * 
     * @param buyOrder 采购订单对象
     */
    public void insertBuyOrderEntry(BuyOrder buyOrder)
    {
        List<BuyOrderEntry> buyOrderEntryList = buyOrder.getBuyOrderEntryList();
        Long id = buyOrder.getId();
        if (StringUtils.isNotNull(buyOrderEntryList))
        {
            List<BuyOrderEntry> list = new ArrayList<BuyOrderEntry>();
            for (BuyOrderEntry buyOrderEntry : buyOrderEntryList)
            {
                buyOrderEntry.setMainId(id);
                list.add(buyOrderEntry);
            }
            if (list.size() > 0)
            {
                buyOrderMapper.batchBuyOrderEntry(list);
            }
        }
    }
}
