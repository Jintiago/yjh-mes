package cn.sourceplan.sale.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.sale.domain.SalOrder;
import cn.sourceplan.sale.domain.SalOrderEntry;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 销售订单Service接口
 *
 * @author jinzhong
 * @date 2022-10-08
 */
public interface ISalOrderService
{
    /**
     * 查询销售订单
     *
     * @param id 销售订单主键
     * @return 销售订单
     */
    SalOrder selectSalOrderById(Long id);

    /**
     * 查询销售订单列表
     *
     * @param salOrder 销售订单
     * @return 销售订单集合
     */
    List<SalOrderEntry> selectSalOrderList(SalOrder salOrder);

    /**
     * 新增销售订单
     *
     * @param salOrder 销售订单
     * @return 结果
     */
    int insertSalOrder(SalOrder salOrder);

    /**
     * 修改销售订单
     *
     * @param salOrder 销售订单
     * @return 结果
     */
    int updateSalOrder(SalOrder salOrder);

    /**
     * 批量删除销售订单
     *
     * @param ids 需要删除的销售订单主键集合
     * @return 结果
     */
    int deleteSalOrderByIds(Long[] ids);

    /**
     * 根据id查询销售订单
     *
     * @param ids 订单子表IDS
     * @return 结果
     */
    AjaxResult listByIds(String ids);

    /**
     * 批量修改订单状态
     *
     * @param ids 采购订单ID
     * @return 结果
     */
    int batchUpdateStatusByEntryIds(String ids ,  String status);
}
