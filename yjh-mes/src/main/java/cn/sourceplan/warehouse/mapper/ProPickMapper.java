package cn.sourceplan.warehouse.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.warehouse.domain.ProPick;
import cn.sourceplan.warehouse.domain.ProPickEntry;
import org.springframework.stereotype.Repository;

/**
 * 生产领料单Mapper接口
 *
 * @author jinzhong
 * @date 2023-02-23
 */
@Repository
public interface ProPickMapper extends BaseMapper<ProPick>
{
    /**
     * 查询生产领料单
     *
     * @param id 生产领料单主键
     * @return 生产领料单
     */
    ProPick selectProPickById(Long id);

    /**
     * 查询生产领料单列表
     *
     * @param proPick 生产领料单
     * @return 生产领料单集合
     */
    List<ProPickEntry> selectProPickList(ProPick proPick);

    /**
     * 批量删除生产领料单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteProPickByIds(Long[] ids);

    /**
     * 批量删除生产领料单子表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteProPickEntryByMainIds(Long[] ids);


    /**
     * 通过生产领料单主键删除生产领料单子表信息
     *
     * @param id 生产领料单ID
     * @return 结果
     */
    int deleteProPickEntryByMainId(Long id);
}
