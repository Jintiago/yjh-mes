package cn.sourceplan.finance.mapper;

import cn.sourceplan.equipment.domain.RepairOrderEntry;
import cn.sourceplan.finance.domain.Payroll;
import cn.sourceplan.finance.domain.SaleOutAccountBill;
import cn.sourceplan.finance.domain.SaleOutAccountEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 销售出库对账单Mapper接口
 *
 * @author jinzhong
 * @date 2023-08-01
 */
@Repository
public interface SaleOutAccountEntryMapper extends BaseMapper<SaleOutAccountEntry>
{
    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<SaleOutAccountEntry> entityList);

}
