package cn.sourceplan.quality.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.quality.domain.QualityLevel;
import org.springframework.stereotype.Repository;

/**
 * 质检等级Mapper接口
 *
 * @author jinzhong
 * @date 2023-02-21
 */
@Repository
public interface QualityLevelMapper extends BaseMapper<QualityLevel>
{
    /**
     * 查询质检等级
     *
     * @param id 质检等级主键
     * @return 质检等级
     */
    QualityLevel selectQualityLevelById(Long id);

    /**
     * 查询质检等级列表
     *
     * @param qualityLevel 质检等级
     * @return 质检等级集合
     */
    List<QualityLevel> selectQualityLevelList(QualityLevel qualityLevel);


    /**
     * 删除质检等级
     *
     * @param id 质检等级主键
     * @return 结果
     */
    int deleteQualityLevelById(Long id);

    /**
     * 批量删除质检等级
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteQualityLevelByIds(Long[] ids);
}
