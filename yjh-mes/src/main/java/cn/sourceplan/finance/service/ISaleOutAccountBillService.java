package cn.sourceplan.finance.service;

import java.util.List;
import cn.sourceplan.finance.domain.SaleOutAccountBill;
import cn.sourceplan.finance.domain.SaleOutAccountEntry;

/**
 * 销售出库对账单Service接口
 *
 * @author jinzhong
 * @date 2023-08-01
 */
public interface ISaleOutAccountBillService
{
    /**
     * 查询销售出库对账单
     *
     * @param id 销售出库对账单主键
     * @return 销售出库对账单
     */
    public SaleOutAccountBill selectSaleOutAccountBillById(Long id);

    /**
     * 查询销售出库对账单列表
     *
     * @param saleOutAccountBill 销售出库对账单
     * @return 销售出库对账单集合
     */
    public List<SaleOutAccountEntry> selectSaleOutAccountBillList(SaleOutAccountBill saleOutAccountBill);

    /**
     * 新增销售出库对账单
     *
     * @param saleOutAccountBill 销售出库对账单
     * @return 结果
     */
    public int insertSaleOutAccountBill(SaleOutAccountBill saleOutAccountBill);

    /**
     * 修改销售出库对账单
     *
     * @param saleOutAccountBill 销售出库对账单
     * @return 结果
     */
    public int updateSaleOutAccountBill(SaleOutAccountBill saleOutAccountBill);

    /**
     * 批量删除销售出库对账单
     *
     * @param ids 需要删除的销售出库对账单主键集合
     * @return 结果
     */
    public int deleteSaleOutAccountBillByIds(Long[] ids);

    /**
     * 删除销售出库对账单信息
     *
     * @param id 销售出库对账单主键
     * @return 结果
     */
    public int deleteSaleOutAccountBillById(Long id);
}
