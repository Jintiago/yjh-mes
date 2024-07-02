package cn.sourceplan.finance.mapper;

import java.util.Collection;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.finance.domain.PieceWagePlan;
import org.springframework.stereotype.Repository;

/**
 * 计件工资方案Mapper接口
 *
 * @author jinzhong
 * @date 2023-03-09
 */
@Repository
public interface PieceWagePlanMapper extends BaseMapper<PieceWagePlan>
{

    /**
     * 批量删除计件工资方案
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deletePieceWagePlanByIds(String[] ids);

    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<PieceWagePlan> entityList);

}
