package cn.sourceplan.sale.mapper;

import java.util.List;

import cn.sourceplan.sale.domain.SalOrderEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.sale.domain.SalOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 销售订单Mapper接口
 *
 * @author jinzhong
 * @date 2022-10-08
 */
@Repository
public interface SalOrderMapper extends BaseMapper<SalOrder>
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
     * 批量删除销售订单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSalOrderByIds(Long[] ids);

    /**
     * 批量删除${subTable.functionName}
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSalOrderEntryByMainIds(Long[] ids);

    /**
     * 通过销售订单主键删除${subTable.functionName}信息
     *
     * @param id 销售订单ID
     * @return 结果
     */
    int deleteSalOrderEntryByMainId(Long id);

    /**
     * 查询已发货数量
     *
     * @param orderEntryId 销售订单子表ID
     * @return 结果
     */
    Float selectSaleOutQuantity (@Param("orderEntryId") Long orderEntryId);
}
