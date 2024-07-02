package cn.sourceplan.quality.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.quality.domain.ReportQualityReason;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * 质检等级Mapper接口
 *
 * @author jinzhong
 * @date 2023-02-21
 */
@Repository
public interface ReportQualityReasonMapper extends BaseMapper<ReportQualityReason>
{
    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<ReportQualityReason> entityList);
}
