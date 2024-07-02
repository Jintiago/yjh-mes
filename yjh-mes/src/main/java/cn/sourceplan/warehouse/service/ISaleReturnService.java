package cn.sourceplan.warehouse.service;

import java.util.List;
import cn.sourceplan.warehouse.domain.SaleReturn;

/**
 * 销售退货单Service接口
 *
 * @author jinzhong
 * @date 2023-08-04
 */
public interface ISaleReturnService
{
    /**
     * 查询销售退货单
     *
     * @param id 销售退货单主键
     * @return 销售退货单
     */
    public SaleReturn selectSaleReturnById(Long id);

    /**
     * 查询销售退货单列表
     *
     * @param saleReturn 销售退货单
     * @return 销售退货单集合
     */
    public List<SaleReturn> selectSaleReturnList(SaleReturn saleReturn);

    /**
     * 新增销售退货单
     *
     * @param saleReturn 销售退货单
     * @return 结果
     */
    public int insertSaleReturn(SaleReturn saleReturn);

    /**
     * 修改销售退货单
     *
     * @param saleReturn 销售退货单
     * @return 结果
     */
    public int updateSaleReturn(SaleReturn saleReturn);

    /**
     * 批量删除销售退货单
     *
     * @param ids 需要删除的销售退货单主键集合
     * @return 结果
     */
    public int deleteSaleReturnByIds(Long[] ids);

    /**
     * 删除销售退货单信息
     *
     * @param id 销售退货单主键
     * @return 结果
     */
    public int deleteSaleReturnById(Long id);
}
