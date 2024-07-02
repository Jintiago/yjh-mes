package cn.sourceplan.quality.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.quality.domain.Reason;
import org.springframework.stereotype.Repository;

/**
 * 质检原因Mapper接口
 *
 * @author jinzhong
 * @date 2023-02-21
 */
@Repository
public interface ReasonMapper extends BaseMapper<Reason>
{
    /**
     * 查询质检原因
     *
     * @param id 质检原因主键
     * @return 质检原因
     */
    Reason selectReasonById(Long id);

    /**
     * 查询质检原因列表
     *
     * @param reason 质检原因
     * @return 质检原因集合
     */
    List<Reason> selectReasonList(Reason reason);


    /**
     * 删除质检原因
     *
     * @param id 质检原因主键
     * @return 结果
     */
    int deleteReasonById(Long id);

    /**
     * 批量删除质检原因
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteReasonByIds(Long[] ids);
}
