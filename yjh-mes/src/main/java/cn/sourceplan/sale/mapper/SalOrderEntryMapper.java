package cn.sourceplan.sale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.sale.domain.SalOrderEntry;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author jinzhong
 */
@Repository
public interface SalOrderEntryMapper extends BaseMapper<SalOrderEntry> {


    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<SalOrderEntry> entityList);

    /**
     * 批量修改订单状态
     *
     * @param ids 采购订单ID
     * @return 结果
     */
    @Update("update sal_order_entry set status =#{status} where id in (${ids}) ")
    int batchUpdateStatusByEntryIds(@Param("ids") String ids ,@Param("status") String status);
}
