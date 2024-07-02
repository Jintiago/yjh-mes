package cn.sourceplan.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.equipment.domain.RepairOrderEntry;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * 设备维修单Mapper接口
 *
 * @author jinzhong
 * @date 2023-04-08
 */
@Repository
public interface RepairOrderEntryMapper extends BaseMapper<RepairOrderEntry>
{
    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<RepairOrderEntry> entityList);
}
