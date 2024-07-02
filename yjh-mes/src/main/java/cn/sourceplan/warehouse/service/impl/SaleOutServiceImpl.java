package cn.sourceplan.warehouse.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.NumberUtil;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.common.utils.SqlCheckUtil;
import cn.sourceplan.finance.domain.SaleOutAccountBill;
import cn.sourceplan.finance.domain.SaleOutAccountEntry;
import cn.sourceplan.finance.mapper.SaleOutAccountBillMapper;
import cn.sourceplan.finance.mapper.SaleOutAccountEntryMapper;
import cn.sourceplan.finance.service.ISaleOutAccountBillService;
import cn.sourceplan.production.domain.WorkOrder;
import cn.sourceplan.production.mapper.WorkOrderMapper;
import cn.sourceplan.system.service.ISysConfigService;
import cn.sourceplan.warehouse.domain.RealTimeInventory;
import cn.sourceplan.warehouse.mapper.SaleOutEntryMapper;
import cn.sourceplan.warehouse.mapper.SaleOutMapper;
import cn.sourceplan.sale.domain.SalOrderEntry;
import cn.sourceplan.sale.mapper.SalOrderEntryMapper;
import cn.sourceplan.sale.mapper.SalOrderMapper;
import cn.sourceplan.warehouse.service.IRealTimeInventoryService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;

import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.warehouse.domain.SaleOutEntry;
import cn.sourceplan.warehouse.domain.SaleOut;
import cn.sourceplan.warehouse.service.ISaleOutService;

/**
 * 销售出库单主表Service业务层处理
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@Service
public class SaleOutServiceImpl implements ISaleOutService
{
    @Autowired
    private SaleOutMapper saleOutMapper;

    @Autowired
    private SaleOutEntryMapper saleOutEntryMapper;

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private SalOrderMapper salOrderMapper;

    @Autowired
    private SalOrderEntryMapper salOrderEntryMapper;
    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    @Autowired
    private ISaleOutAccountBillService saleOutAccountBillService;

    @Autowired
    private SaleOutAccountBillMapper saleOutAccountBillMapper;

    @Autowired
    private SaleOutAccountEntryMapper saleOutAccountEntryMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IRealTimeInventoryService realTimeInventoryService;

    /**
     * 查询销售出库单主表
     *
     * @param id 销售出库单主表主键
     * @return 销售出库单主表
     */
    @Override
    public SaleOut selectSaleOutById(Long id)
    {
        return saleOutMapper.selectSaleOutById(id);
    }

    /**
     * 查询销售出库单主表列表
     *
     * @param saleOut 销售出库单主表
     * @return 销售出库单主表
     */
    @Override
    public List<SaleOut> selectSaleOutList(SaleOut saleOut)
    {
        String[] outTimeQuery = saleOut.getOutTimeQuery();
        if (outTimeQuery!=null&&outTimeQuery.length>0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String,Object> map = saleOut.getParams();
            map.put("outTimeStart",outTimeQuery[0]);
            if(outTimeQuery.length>1){
                map.put("outTimeEnd",outTimeQuery[1]);
            }else{
                String format = sdf.format(new Date());
                map.put("outTimeEnd",format);
            }
        }
        return saleOutMapper.selectSaleOutList(saleOut);
    }

    /**
     * 新增销售出库单主表
     *
     * @param saleOut 销售出库单主表
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult insertSaleOut(SaleOut saleOut) {
        //如果编号为null,则自动为其生成编号
        if (StringUtils.isBlank(saleOut.getNumber())) {
            SysCodeRule queryDomain = new SysCodeRule();
            queryDomain.setBasicDomain("SaleOut");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(), true);
            saleOut.setNumber(number);
        }

        int rows = saleOutMapper.insert(saleOut);

        String result = null;
        try {
            // 执行数据库操作
            result = insertSaleOutEntry(saleOut);
            // 这里可以抛出异常来触发事务回滚
            // 如果抛出异常，方法执行的操作将会被回滚
            if (!result.equals("true")) {
                throw new RuntimeException("Something went wrong");
            }
        } catch (Exception e) {
            // 处理异常
            // 如果发生异常，事务将被回滚
            return AjaxResult.error(result);
        }

        //生成销售出库对账单
        SaleOutAccountBill saleOutAccountBill = generationAccoutBill(saleOut);
        if (saleOutAccountBill != null) {
            saleOutAccountBillService.insertSaleOutAccountBill(saleOutAccountBill);
        }
        return AjaxResult.success();
    }

    private SaleOutAccountBill generationAccoutBill(SaleOut saleOut) {
        //封装表头
        SaleOutAccountBill saleOutAccountBill = new SaleOutAccountBill();
        saleOutAccountBill.setSaleOutNumber(saleOut.getNumber());
        saleOutAccountBill.setSaleOutId(saleOut.getId());
        saleOutAccountBill.setChecker(saleOut.getChecker());
        saleOutAccountBill.setCheckDate(saleOut.getCheckDate());
        saleOutAccountBill.setCustomerId(saleOut.getCustomerId());
        saleOutAccountBill.setCustomerName(saleOut.getCustomerName());
        saleOutAccountBill.setKeeperId(saleOut.getKeeperId());
        saleOutAccountBill.setKeeperName(saleOut.getKeeperName());
        saleOutAccountBill.setLogisticsInfo(saleOut.getLogisticsInfo());
        saleOutAccountBill.setOutTime(saleOut.getOutTime());
        saleOutAccountBill.setStatus("A");
        saleOutAccountBill.setMethod(saleOut.getMethod());
        saleOutAccountBill.setInvoiceMark(saleOut.getInvoiceMark());
        saleOutAccountBill.setShipLocation(saleOut.getShipLocation());
        //封装表体
        List<SaleOutEntry> saleOutEntryList = saleOut.getSaleOutEntryList();
        List<SaleOutAccountEntry> list = new ArrayList<>();
        //表头标准总金额
        BigDecimal standardAmount = new BigDecimal(0.0);
        //表头销售总金额
        BigDecimal priceAmount = new BigDecimal(0.0);
        for (int i = 0; i < saleOutEntryList.size(); i++) {
            SaleOutEntry saleOutEntry = saleOutEntryList.get(i);
            BigDecimal quantity = saleOutEntry.getQuantity();
            SaleOutAccountEntry saleOutAccountEntry = new SaleOutAccountEntry();
            saleOutAccountEntry.setMaterialId(saleOutEntry.getMaterialId());
            saleOutAccountEntry.setMaterialNumber(saleOutEntry.getMaterialNumber());
            saleOutAccountEntry.setMaterialName(saleOutEntry.getMaterialName());
            saleOutAccountEntry.setSpecification(saleOutEntry.getSpecification());
            saleOutAccountEntry.setMaterialUnitId(saleOutEntry.getMaterialUnitId());
            saleOutAccountEntry.setMaterialUnitName(saleOutEntry.getMaterialUnitName());
            saleOutAccountEntry.setQuantity(quantity);
            //天润项目定制功能 begin
                // 更新平方数
                JSONObject extendField = JSONObject.parseObject(saleOutEntry.getExtendField());

                if(StringUtils.isNotBlank(saleOutEntry.getSpecification())){
                    if (extendField==null){
                        extendField =new JSONObject();
                    }
                    try {
                        // 数量 = 平方数长*宽*数量
                        // 需要再乘以规格型号中的平方  1.25m*500m*6c
                        String specification = saleOutEntry.getSpecification();
                        // 取到第一个m  下标 4
                        int indexA = specification.indexOf('m');
                        // 取到1.25
                        String numA = specification.substring(0,indexA);
                        //取到  500m*6c
                        int indexT=indexA+2;
                        String strB = specification.substring(indexT);
                        //取到 第二个m下标 3
                        int indexB = strB.indexOf("m");
                        //取到 500
                        String numB =strB.substring(0,indexB);
                        //长*宽获得每卷面积
                        BigDecimal mul = NumberUtil.mul(Double.parseDouble(numA) * Double.parseDouble(numB), quantity);
                        extendField.put("pingfang",mul);
                        saleOutEntry.setExtendField(extendField.toJSONString());
                        quantity= mul   ;
                    }
                        catch (java.lang.StringIndexOutOfBoundsException e){
                        extendField.put("pingfang",0);
                    }
                }
            //天润项目定制功能 end
            saleOutAccountEntry.setSort(i+1);
            saleOutAccountEntry.setRemark(saleOutEntry.getRemark());
            saleOutAccountEntry.setExtendField(saleOutEntry.getExtendField());
            //获取订单子表
            SalOrderEntry salOrderEntry = salOrderEntryMapper.selectById(saleOutEntry.getSaleOrderEntryId());
            //标准单价
            BigDecimal standardPrice = new BigDecimal(0.0);
            //销售单价
            BigDecimal price = new BigDecimal(0.0);

            //销售金额
            BigDecimal totalPrice = new BigDecimal(0.0);
            if(salOrderEntry==null){
                //取销售出库里面的价格
                standardPrice=saleOutEntry.getPrice()==null?new BigDecimal(0.0):saleOutEntry.getPrice();
                price = saleOutEntry.getPrice()==null?new BigDecimal(0.0):saleOutEntry.getPrice();
            }else{
                //取销售订单里面的值
                standardPrice=salOrderEntry.getPrice()==null?new BigDecimal(0.0):salOrderEntry.getPrice();
                price = salOrderEntry.getTaxPrice()==null?new BigDecimal(0.0):salOrderEntry.getTaxPrice();
            }

            //标准单价
            saleOutAccountEntry.setStandardPrice(standardPrice);
            //标准金额
            BigDecimal standardAmountE = NumberUtil.mul(quantity, standardPrice);
            saleOutAccountEntry.setStandardAmount(standardAmountE);
            standardAmount=standardAmount.add(standardAmountE);
            //销售单价
            saleOutAccountEntry.setSalePrice(price);
            //销售金额
            BigDecimal priceAmountE = NumberUtil.mul(quantity, price);
            saleOutAccountEntry.setSaleAmount(priceAmountE);
            priceAmount=priceAmount.add(priceAmountE);
            //折扣金额 = 销售金额 -标准金额
            saleOutAccountEntry.setDiscountAmount(NumberUtil.sub(totalPrice,standardAmountE));
            //折扣率 = 销售金额 / 标准金额
            BigDecimal divide = new BigDecimal(0.0);
            if(standardAmountE.compareTo(new BigDecimal(0.0))!=0){
                divide = totalPrice.divide(standardAmountE, 2, RoundingMode.HALF_UP);
            }
            saleOutAccountEntry.setDiscountRate( NumberUtil.mul(NumberUtil.sub(divide, 1), 100).doubleValue());
            list.add(saleOutAccountEntry);
        }
        if(list.size()==0){
            return null;
        }
        //表头封装合计
        //标准金额
        saleOutAccountBill.setStandardAmount(standardAmount);
        //销售金额
        saleOutAccountBill.setSaleAmount(priceAmount);
        //折扣金额 = 销售金额 -标准金额
        saleOutAccountBill.setDiscountAmount(NumberUtil.sub(priceAmount,standardAmount));
        //折扣率 = （（销售金额/标准金额）-1）*100
        BigDecimal div = new BigDecimal(0.0);
        if(standardAmount.compareTo(new BigDecimal(0.0))!=0){
            div=NumberUtil.div(priceAmount, standardAmount);
        }
        BigDecimal discountRate = NumberUtil.mul(NumberUtil.sub(div, 1), 100);
        saleOutAccountBill.setDiscountRate(discountRate.doubleValue());
        saleOutAccountBill.setSaleOutAccountEntryList(list);
        return saleOutAccountBill;
    }

    /**
     * 修改销售出库单主表
     *
     * @param saleOut 销售出库单主表
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult updateSaleOut(SaleOut saleOut)
    {
        saleOutMapper.deleteSaleOutEntryByMainId(saleOut.getId());
        String result = insertSaleOutEntry(saleOut);
        if(!"true".equals(result)){
            return AjaxResult.error(result);
        }
        int i = saleOutMapper.updateById(saleOut);
        //重新生成销售出库对账单
        QueryWrapper<SaleOutAccountEntry> qwE = new QueryWrapper<>();
        qwE.eq("main_id",saleOut.getId());
        saleOutAccountEntryMapper.delete(qwE);
        QueryWrapper<SaleOutAccountBill> qw = new QueryWrapper<>();
        qw.eq("sale_out_id",saleOut.getId());
        saleOutAccountBillMapper.delete(qw);
        SaleOutAccountBill saleOutAccountBill = generationAccoutBill(saleOut);
        if(saleOutAccountBill!=null){
            saleOutAccountBillService.insertSaleOutAccountBill(saleOutAccountBill);
        }
        return AjaxResult.success();
    }

    /**
     * 批量删除销售出库单主表
     *
     * @param ids 需要删除的销售出库单主表主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSaleOutByIds(Long[] ids)
    {
        saleOutMapper.deleteSaleOutEntryByMainIds(ids);
        int i1 = saleOutMapper.deleteSaleOutByIds(ids);
        //删除对应对账单
        QueryWrapper<SaleOutAccountBill> qw = new QueryWrapper<>();
        qw.in("sale_out_id",ids);
        List<SaleOutAccountBill> saleOutAccountBills = saleOutAccountBillMapper.selectList(qw);
        if(saleOutAccountBills!=null&&saleOutAccountBills.size()>0){
            Long[] accIds = new Long[saleOutAccountBills.size()];
            for (int i = 0; i < saleOutAccountBills.size(); i++) {
                accIds[i]=saleOutAccountBills.get(i).getId();
            }
            saleOutAccountBillService.deleteSaleOutAccountBillByIds(accIds);
        }

        return i1;
    }

    /**
     * 删除销售出库单主表信息
     *
     * @param id 销售出库单主表主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSaleOutById(Long id)
    {
        saleOutMapper.deleteSaleOutEntryByMainId(id);
        return saleOutMapper.deleteSaleOutById(id);
    }

    /**
     * 新增销售出库子表信息
     *
     * @param saleOut 销售出库单主表对象
     * @return  返回 -1(禁止负库存) 返回0(无销售出库明细)
     */
    public String insertSaleOutEntry(SaleOut saleOut)
    {
        List<SaleOutEntry> saleOutEntryList = saleOut.getSaleOutEntryList();
        Long id = saleOut.getId();
        Integer retult =0;
        if (StringUtils.isNotNull(saleOutEntryList))
        {
            List<SaleOutEntry> list = new ArrayList<SaleOutEntry>();
            //是否允许负库存
            String s = configService.selectConfigByKey("mes.wm.negativeInventory");
            boolean negativeInventory = "true".equals(s);
            for (SaleOutEntry saleOutEntry : saleOutEntryList)
            {
                if(!negativeInventory){
                    RealTimeInventory realTimeInventory = new RealTimeInventory();
                    realTimeInventory.setMaterialId(saleOutEntry.getMaterialId());
                    realTimeInventory.setWarehouseId(saleOut.getWarehouseId());
                    realTimeInventory.setSpecification(saleOutEntry.getSpecification());
                    List<JSONObject> jsonObjects = realTimeInventoryService.selectRealTimeInventoryList(realTimeInventory);
                    if(jsonObjects==null|| jsonObjects.isEmpty()){
                        return "物料："+saleOutEntry.getMaterialName()+"("+saleOutEntry.getSpecification()+")库存不足,";
                    }
                    for (JSONObject jsonObject : jsonObjects) {
                        Float quantity = jsonObject.getFloat("quantity");
                        float v = saleOutEntry.getQuantity().floatValue();
                        if (quantity < v) {
                            return "物料："+saleOutEntry.getMaterialName()+"("+saleOutEntry.getSpecification()+")库存不足,";
                        }
                    }
                }

                Long saleOrderEntryId = saleOutEntry.getSaleOrderEntryId();
                //如果该出库单由销售订单下推生成，则需要回写销售订单
                if(saleOrderEntryId!=null&&saleOrderEntryId!=0){

                    SalOrderEntry salOrderEntry = salOrderEntryMapper.selectById(saleOrderEntryId);
                    //子表发货日期  deliveryDate
                    //主表状态 判断是部分发货还是已全部发货 C全部发货  E部分发货
                    Float aFloat = salOrderMapper.selectSaleOutQuantity(salOrderEntry.getId());
                    salOrderEntry.setSaleOutQuantity(aFloat==null?0F:aFloat);
                    salOrderEntry.setDeliveryDate(saleOut.getOutTime());
                    BigDecimal quantity = salOrderEntry.getQuantity();
                    quantity=quantity.subtract(saleOutEntry.getQuantity());
                    BigDecimal bFloat = new BigDecimal(aFloat==null?0F:aFloat);
                    salOrderEntry.setStatus(quantity.compareTo(bFloat)==1?"E":"C");
                    salOrderEntryMapper.updateById(salOrderEntry);

                    //回写生产订单状态为已发货状态 F
                    //"sourceInfo": "{\"saleOrderEntryId\":[12,13]}"
                    QueryWrapper<WorkOrder> qw = new QueryWrapper<>();
                    Long[] lArr = {saleOrderEntryId};
                    qw.apply("1=1"+SqlCheckUtil.querySourceInfo("saleOrderEntryId",lArr));
                    List<WorkOrder> workOrderList = workOrderMapper.selectList(qw);
                    for (int i = 0; i < workOrderList.size(); i++) {
                        WorkOrder workOrder = workOrderList.get(i);
                        workOrder.setProStatus("F");
                        workOrder.setRealFinishDate(saleOut.getOutTime());
                        workOrderMapper.updateById(workOrder);
                    }
                }
                JSONObject extendFieldJson = saleOutEntry.getExtendFieldJson();
                if(extendFieldJson==null){
                    extendFieldJson=JSONObject.parseObject("{}");
                }
                saleOutEntry.setExtendField(extendFieldJson.toJSONString());
                saleOutEntry.setMainId(id);
                list.add(saleOutEntry);
            }
            if (!list.isEmpty())
            {
                retult = saleOutEntryMapper.insertBatchSomeColumn(list);
            }
        }
        return retult>0?"true":"false";

    }
}
