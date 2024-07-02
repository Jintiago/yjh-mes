package cn.sourceplan.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.warehouse.domain.ManufactureIntoEntry;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * 完工入库单Mapper接口
 *
 * @author jinzhong
 * @date 2022-11-10
 */
@Repository
public interface ManufactureIntoEntryMapper extends BaseMapper<ManufactureIntoEntry>
{
    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<ManufactureIntoEntry> entityList);


}
