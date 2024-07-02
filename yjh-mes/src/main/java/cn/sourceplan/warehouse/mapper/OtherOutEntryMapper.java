package cn.sourceplan.warehouse.mapper;

import cn.sourceplan.warehouse.domain.OtherOut;
import cn.sourceplan.warehouse.domain.OtherOutEntry;
import cn.sourceplan.warehouse.domain.SaleOutEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 其他出库单子表Mapper接口
 *
 * @author jinzhong
 * @date 2023-09-28
 */
@Repository
public interface OtherOutEntryMapper extends BaseMapper<OtherOutEntry>
{

    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<OtherOutEntry> entityList);

}
