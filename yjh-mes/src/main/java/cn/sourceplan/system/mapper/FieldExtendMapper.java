package cn.sourceplan.system.mapper;

import java.util.Collection;
import java.util.List;

import cn.sourceplan.barcode.domain.MaterialSn;
import cn.sourceplan.system.domain.Carousel;
import cn.sourceplan.system.domain.FieldExtend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 字段扩展Mapper接口
 *
 * @author jinzhong
 * @date 2023-05-23
 */
@Repository
public interface FieldExtendMapper extends BaseMapper<FieldExtend>
{
    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<FieldExtend> entityList);
}
