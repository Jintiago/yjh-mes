package cn.sourceplan.warehouse.service.impl;

import java.util.Arrays;
import java.util.List;

import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.warehouse.mapper.SaleReturnEntryMapper;
import cn.sourceplan.warehouse.mapper.SaleReturnMapper;
import cn.sourceplan.warehouse.service.ISaleReturnService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.warehouse.domain.SaleReturnEntry;
import cn.sourceplan.warehouse.domain.SaleReturn;

/**
 * 销售退货单Service业务层处理
 *
 * @author jinzhong
 * @date 2023-08-04
 */
@Service
public class SaleReturnServiceImpl implements ISaleReturnService
{
    @Autowired
    private SaleReturnMapper saleReturnMapper;

    @Autowired
    private SaleReturnEntryMapper saleReturnEntryMapper;

    @Autowired
    private ISysCodeRuleService sysCodeRuleService;
    /**
     * 查询销售退货单
     *
     * @param id 销售退货单主键
     * @return 销售退货单
     */
    @Override
    public SaleReturn selectSaleReturnById(Long id)
    {
        return saleReturnMapper.selectSaleReturnById(id);
    }

    /**
     * 查询销售退货单列表
     *
     * @param saleReturn 销售退货单
     * @return 销售退货单
     */
    @Override
    public List<SaleReturn> selectSaleReturnList(SaleReturn saleReturn)
    {
        return saleReturnMapper.selectSaleReturnList(saleReturn);
    }

    /**
     * 新增销售退货单
     *
     * @param saleReturn 销售退货单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertSaleReturn(SaleReturn saleReturn)
    {
        //如果编号为null,则自动为其生成编号
        if ( StringUtils.isBlank(saleReturn.getNumber()) ){
            SysCodeRule queryDomain  = new SysCodeRule();
            queryDomain.setBasicDomain("SaleReturn");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(),true);
            saleReturn.setNumber(number);
        }
        int rows = saleReturnMapper.insert(saleReturn);
        insertSaleReturnEntry(saleReturn);
        return rows;
    }

    /**
     * 修改销售退货单
     *
     * @param saleReturn 销售退货单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateSaleReturn(SaleReturn saleReturn)
    {
        QueryWrapper<SaleReturnEntry> qw = new QueryWrapper<>();
        qw.eq("main_id",saleReturn.getId());
        saleReturnEntryMapper.delete(qw);
        insertSaleReturnEntry(saleReturn);
        return saleReturnMapper.updateById(saleReturn);
    }

    /**
     * 批量删除销售退货单
     *
     * @param ids 需要删除的销售退货单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSaleReturnByIds(Long[] ids)
    {
        QueryWrapper<SaleReturnEntry> qw = new QueryWrapper<>();
        qw.in("main_id",ids);
        saleReturnEntryMapper.delete(qw);
        return saleReturnMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除销售退货单信息
     *
     * @param id 销售退货单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSaleReturnById(Long id)
    {
        QueryWrapper<SaleReturnEntry> qw = new QueryWrapper<>();
        qw.eq("main_id",id);
        saleReturnEntryMapper.delete(qw);
        return saleReturnMapper.deleteById(id);
    }

    /**
     * 新增销售退货单子表信息
     *
     * @param saleReturn 销售退货单对象
     */
    public void insertSaleReturnEntry(SaleReturn saleReturn)
    {
        List<SaleReturnEntry> saleReturnEntryList = saleReturn.getSaleReturnEntryList();
        Long id = saleReturn.getId();
        if (StringUtils.isNotNull(saleReturnEntryList))
        {
            List<SaleReturnEntry> list = new ArrayList<SaleReturnEntry>();
            for (SaleReturnEntry saleReturnEntry : saleReturnEntryList)
            {
                JSONObject extendFieldJson = saleReturnEntry.getExtendFieldJson();
                if(extendFieldJson==null){
                    extendFieldJson=JSONObject.parseObject("{}");
                }
                saleReturnEntry.setExtendField(extendFieldJson.toJSONString());
                saleReturnEntry.setMainId(id);
                list.add(saleReturnEntry);
            }
            if (list.size() > 0)
            {
                saleReturnEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }
}
