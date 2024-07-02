package cn.sourceplan.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.common.domain.SysCodeRuleEntry;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author jinzhong
 */
@Repository
public interface SysCodeRuleEntryMapper extends BaseMapper<SysCodeRuleEntry> {

    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<SysCodeRuleEntry> entityList);

}
