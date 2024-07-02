package cn.sourceplan.quality.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.quality.domain.QualityLevel;

/**
 * 质检等级Service接口
 *
 * @author jinzhong
 * @date 2023-02-21
 */
public interface IQualityLevelService
{
    /**
     * 查询质检等级
     *
     * @param id 质检等级主键
     * @return 质检等级
     */
    public QualityLevel selectQualityLevelById(Long id);

    /**
     * 查询质检等级列表
     *
     * @param qualityLevel 质检等级
     * @return 质检等级集合
     */
    public List<QualityLevel> selectQualityLevelList(QualityLevel qualityLevel);

    /**
     * 新增质检等级
     *
     * @param qualityLevel 质检等级
     * @return 结果
     */
    public int insertQualityLevel(QualityLevel qualityLevel);

    /**
     * 修改质检等级
     *
     * @param qualityLevel 质检等级
     * @return 结果
     */
    public AjaxResult updateQualityLevel(QualityLevel qualityLevel);

    /**
     * 批量删除质检等级
     *
     * @param ids 需要删除的质检等级主键集合
     * @return 结果
     */
    public AjaxResult deleteQualityLevelByIds(Long[] ids);

}
