package cn.sourceplan.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.warehouse.domain.ProPickEntry;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * 生产领料单Mapper接口
 *
 * @author jinzhong
 * @date 2023-02-23
 */
@Repository
public interface ProPickEntryMapper extends BaseMapper<ProPickEntry>
{
    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<ProPickEntry> entityList);
}
