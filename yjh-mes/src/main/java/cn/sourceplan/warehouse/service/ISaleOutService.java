package cn.sourceplan.warehouse.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.warehouse.domain.SaleOut;

/**
 * 销售出库单主表Service接口
 *
 * @author jinzhong
 * @date 2023-06-26
 */
public interface ISaleOutService
{
    /**
     * 查询销售出库单主表
     *
     * @param id 销售出库单主表主键
     * @return 销售出库单主表
     */
    public SaleOut selectSaleOutById(Long id);

    /**
     * 查询销售出库单主表列表
     *
     * @param saleOut 销售出库单主表
     * @return 销售出库单主表集合
     */
    public List<SaleOut> selectSaleOutList(SaleOut saleOut);

    /**
     * 新增销售出库单主表
     *
     * @param saleOut 销售出库单主表
     * @return 结果
     */
    public AjaxResult insertSaleOut(SaleOut saleOut);

    /**
     * 修改销售出库单主表
     *
     * @param saleOut 销售出库单主表
     * @return 结果
     */
    public AjaxResult updateSaleOut(SaleOut saleOut);

    /**
     * 批量删除销售出库单主表
     *
     * @param ids 需要删除的销售出库单主表主键集合
     * @return 结果
     */
    public int deleteSaleOutByIds(Long[] ids);

    /**
     * 删除销售出库单主表信息
     *
     * @param id 销售出库单主表主键
     * @return 结果
     */
    public int deleteSaleOutById(Long id);
}
