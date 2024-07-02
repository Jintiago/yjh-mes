package cn.sourceplan.sale.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.sale.mapper.SalOrderEntryMapper;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.sale.domain.SalOrderEntry;
import cn.sourceplan.sale.mapper.SalOrderMapper;
import cn.sourceplan.sale.domain.SalOrder;
import cn.sourceplan.sale.service.ISalOrderService;

/**
 * 销售订单Service业务层处理
 *
 * @author jinzhong
 * @date 2022-10-08
 */
@Service
public class SalOrderServiceImpl implements ISalOrderService
{
    @Autowired
    private SalOrderMapper salOrderMapper;

    @Autowired
    private SalOrderEntryMapper salOrderEntryMapper;

    /**
     * 查询销售订单
     *
     * @param id 销售订单主键
     * @return 销售订单
     */
    @Override
    public SalOrder selectSalOrderById(Long id)
    {
        SalOrder salOrder = salOrderMapper.selectSalOrderById(id);
        return salOrder;
    }

    /**
     * 查询销售订单列表
     *
     * @param salOrder 销售订单
     * @return 销售订单
     */
    @Override
    public List<SalOrderEntry> selectSalOrderList(SalOrder salOrder)
    {
        String[] saleDateQuery = salOrder.getSaleDateQuery();
        String[] deliveryDateQuery = salOrder.getDeliveryDateQuery();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (saleDateQuery!=null&&saleDateQuery.length>0){
            Map<String,Object> map = salOrder.getParams();
            map.put("saleDateStart",saleDateQuery[0]);
            if(saleDateQuery.length>1){
                map.put("saleDateEnd",saleDateQuery[1]);
            }else{
                String format = sdf.format(new Date());
                map.put("saleDateEnd",format);
            }
        }
        if (deliveryDateQuery!=null&&deliveryDateQuery.length>0){
            Map<String,Object> map = salOrder.getParams();
            map.put("deliveryDateStart",deliveryDateQuery[0]);
            if(deliveryDateQuery.length>1){
                map.put("deliveryDateEnd",deliveryDateQuery[1]);
            }else{
                String format = sdf.format(new Date());
                map.put("deliveryDateEnd",format);
            }
        }
        //封装已发货数量
        List<SalOrderEntry> salOrderEntryList = salOrderMapper.selectSalOrderList(salOrder);
        for (int i = 0; i < salOrderEntryList.size(); i++) {
            SalOrderEntry salOrderEntry = salOrderEntryList.get(i);
            Float aFloat = salOrderMapper.selectSaleOutQuantity(salOrderEntry.getId());
            salOrderEntry.setSaleOutQuantity(aFloat);
        }

        return salOrderEntryList;
    }

    /**
     * 新增销售订单
     *
     * @param salOrder 销售订单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertSalOrder(SalOrder salOrder)
    {

        int rows = salOrderMapper.insert(salOrder);
        insertSalOrderEntry(salOrder);
        return rows;
    }

    /**
     * 修改销售订单
     *
     * @param salOrder 销售订单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateSalOrder(SalOrder salOrder)
    {
        salOrderMapper.deleteSalOrderEntryByMainId(salOrder.getId());
        insertSalOrderEntry(salOrder);
        return salOrderMapper.updateById(salOrder);
    }

    /**
     * 批量删除销售订单
     *
     * @param ids 需要删除的销售订单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSalOrderByIds(Long[] ids)
    {
        salOrderMapper.deleteSalOrderEntryByMainIds(ids);
        return salOrderMapper.deleteSalOrderByIds(ids);
    }

    @Override
    public AjaxResult listByIds(String ids) {

        SalOrder salOrder = new SalOrder();
        salOrder.getParams().put("entryIds",ids);
        List<SalOrderEntry> salOrders = selectSalOrderList(salOrder);
        return AjaxResult.success(salOrders);
    }

    /**
     * 新增${subTable.functionName}信息
     *
     * @param salOrder 销售订单对象
     */
    public void insertSalOrderEntry(SalOrder salOrder)
    {
        List<SalOrderEntry> salOrderEntryList = salOrder.getSalOrderEntryList();
        Long id = salOrder.getId();
        if (StringUtils.isNotNull(salOrderEntryList))
        {
            List<SalOrderEntry> list = new ArrayList<SalOrderEntry>();
            for (SalOrderEntry salOrderEntry : salOrderEntryList)
            {
                JSONObject extendFieldJson = salOrderEntry.getExtendFieldJson();
                if(extendFieldJson==null){
                    extendFieldJson=JSONObject.parseObject("{}");
                }
                salOrderEntry.setExtendField(extendFieldJson.toJSONString());
                salOrderEntry.setMainId(id);
                list.add(salOrderEntry);
            }
            if (list.size() > 0)
            {
                salOrderEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }

    @Override
    public int batchUpdateStatusByEntryIds(String ids, String status) {


        return salOrderEntryMapper.batchUpdateStatusByEntryIds(ids,status);
    }
}
