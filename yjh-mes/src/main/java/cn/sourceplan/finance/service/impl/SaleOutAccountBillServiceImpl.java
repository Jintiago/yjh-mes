package cn.sourceplan.finance.service.impl;

import java.util.Arrays;
import java.util.List;

import cn.sourceplan.finance.mapper.SaleOutAccountEntryMapper;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;

import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.finance.domain.SaleOutAccountEntry;
import cn.sourceplan.finance.mapper.SaleOutAccountBillMapper;
import cn.sourceplan.finance.domain.SaleOutAccountBill;
import cn.sourceplan.finance.service.ISaleOutAccountBillService;

/**
 * 销售出库对账单Service业务层处理
 *
 * @author jinzhong
 * @date 2023-08-01
 */
@Service
public class SaleOutAccountBillServiceImpl implements ISaleOutAccountBillService
{
    @Autowired
    private SaleOutAccountBillMapper saleOutAccountBillMapper;

    @Autowired
    private SaleOutAccountEntryMapper saleOutAccountEntryMapper;

    /**
     * 查询销售出库对账单
     *
     * @param id 销售出库对账单主键
     * @return 销售出库对账单
     */
    @Override
    public SaleOutAccountBill selectSaleOutAccountBillById(Long id)
    {
        return saleOutAccountBillMapper.selectSaleOutAccountBillById(id);
    }

    /**
     * 查询销售出库对账单列表
     *
     * @param saleOutAccountBill 销售出库对账单
     * @return 销售出库对账单
     */
    @Override
    public List<SaleOutAccountEntry> selectSaleOutAccountBillList(SaleOutAccountBill saleOutAccountBill)
    {

        Map<String, Object> params = saleOutAccountBill.getParams();
        Object extendFieldJson =  params.get("extendFieldJson");
        if(extendFieldJson!=null){
            //有自定义字段，进行过滤
            Map map = JSON.parseObject(extendFieldJson.toString(), Map.class);
            params.put("extendFieldJson",map);
            System.out.println(map);
        }


        return saleOutAccountBillMapper.selectSaleOutAccountBillList(saleOutAccountBill);
    }

    /**
     * 新增销售出库对账单
     *
     * @param saleOutAccountBill 销售出库对账单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertSaleOutAccountBill(SaleOutAccountBill saleOutAccountBill)
    {
        int rows = saleOutAccountBillMapper.insert(saleOutAccountBill);
        insertSaleOutAccountEntry(saleOutAccountBill);
        return rows;
    }

    /**
     * 修改销售出库对账单
     *
     * @param saleOutAccountBill 销售出库对账单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateSaleOutAccountBill(SaleOutAccountBill saleOutAccountBill)
    {
        QueryWrapper<SaleOutAccountEntry> qw = new QueryWrapper<>();
        qw.eq("main_id",saleOutAccountBill.getId());
        saleOutAccountEntryMapper.delete(qw);
        insertSaleOutAccountEntry(saleOutAccountBill);
        return saleOutAccountBillMapper.updateById(saleOutAccountBill);
    }

    /**
     * 批量删除销售出库对账单
     *
     * @param ids 需要删除的销售出库对账单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSaleOutAccountBillByIds(Long[] ids)
    {
        QueryWrapper<SaleOutAccountEntry> qw = new QueryWrapper<>();
        qw.in("main_id",ids);
        saleOutAccountEntryMapper.delete(qw);
        return saleOutAccountBillMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除销售出库对账单信息
     *
     * @param id 销售出库对账单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSaleOutAccountBillById(Long id)
    {
        QueryWrapper<SaleOutAccountEntry> qw = new QueryWrapper<>();
        qw.eq("main_id",id);
        saleOutAccountEntryMapper.delete(qw);
        return saleOutAccountBillMapper.deleteById(id);
    }

    /**
     * 新增销售出库对账明细信息
     *
     * @param saleOutAccountBill 销售出库对账单对象
     */
    public void insertSaleOutAccountEntry(SaleOutAccountBill saleOutAccountBill)
    {
        List<SaleOutAccountEntry> saleOutAccountEntryList = saleOutAccountBill.getSaleOutAccountEntryList();
        Long id = saleOutAccountBill.getId();
        if (StringUtils.isNotNull(saleOutAccountEntryList))
        {
            List<SaleOutAccountEntry> list = new ArrayList<SaleOutAccountEntry>();
            for (SaleOutAccountEntry saleOutAccountEntry : saleOutAccountEntryList)
            {
                JSONObject extendFieldJson = saleOutAccountEntry.getExtendFieldJson();
                if(extendFieldJson==null){
                    extendFieldJson=JSONObject.parseObject("{}");
                }
                saleOutAccountEntry.setExtendField(extendFieldJson.toJSONString());
                saleOutAccountEntry.setMainId(id);
                list.add(saleOutAccountEntry);
            }
            if (list.size() > 0)
            {
                saleOutAccountEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }
}
