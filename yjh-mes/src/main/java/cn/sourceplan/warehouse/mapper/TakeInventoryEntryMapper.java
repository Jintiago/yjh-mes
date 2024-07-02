package cn.sourceplan.warehouse.mapper;

import cn.sourceplan.warehouse.domain.TakeInventoryEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * 库存盘点Mapper接口
 *
 * @author jinzhong
 * @date 2023-07-20
 */
@Repository
public interface TakeInventoryEntryMapper extends BaseMapper<TakeInventoryEntry>
{
    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<TakeInventoryEntry> entityList);

}
