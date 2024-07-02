package cn.sourceplan.production.mapper;

import cn.sourceplan.sale.domain.SalOrderEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.production.domain.WorkOrderEntry;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * 工单Mapper接口
 *
 * @author jinzhong
 * @date 2022-12-12
 */
@Repository
public interface WorkOrderEntryMapper extends BaseMapper<WorkOrderEntry>
{
    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<WorkOrderEntry> entityList);
}
