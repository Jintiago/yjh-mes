package cn.sourceplan.quality.service.impl;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.SqlCheckUtil;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.quality.mapper.QualityLevelMapper;
import cn.sourceplan.quality.domain.QualityLevel;
import cn.sourceplan.quality.service.IQualityLevelService;

/**
 * 质检等级Service业务层处理
 *
 * @author jinzhong
 * @date 2023-02-21
 */
@Service
public class QualityLevelServiceImpl implements IQualityLevelService
{
    @Autowired
    private QualityLevelMapper qualityLevelMapper;


    /**
     * 查询质检等级
     *
     * @param id 质检等级主键
     * @return 质检等级
     */
    @Override
    public QualityLevel selectQualityLevelById(Long id)
    {
        return qualityLevelMapper.selectQualityLevelById(id);
    }

    /**
     * 查询质检等级列表
     *
     * @param qualityLevel 质检等级
     * @return 质检等级
     */
    @Override
    public List<QualityLevel> selectQualityLevelList(QualityLevel qualityLevel)
    {
        return qualityLevelMapper.selectQualityLevelList(qualityLevel);
    }

    /**
     * 新增质检等级
     *
     * @param qualityLevel 质检等级
     * @return 结果
     */
    @Override
    public int insertQualityLevel(QualityLevel qualityLevel)
    {
        return qualityLevelMapper.insert(qualityLevel);
    }

    /**
     * 修改质检等级
     *
     * @param qualityLevel 质检等级
     * @return 结果
     */
    @Override
    public AjaxResult updateQualityLevel(QualityLevel qualityLevel)
    {
        //校验数据引用关系
        //报工质检
        String qcCheck = SqlCheckUtil.sqlCheck("number",
                "qc_report_quality_entry b left join qc_report_quality a on a.id = b.main_id ",
                "where b.level_id =  "+qualityLevel.getId()+"  and status ='A' "+
                "group by a.number");
        if(qcCheck!=null){
            return AjaxResult.error("该数据在质检单["+qcCheck+"]中已被引用，无法修改");
        }
        int i = qualityLevelMapper.updateById(qualityLevel);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 批量删除质检等级
     *
     * @param ids 需要删除的质检等级主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteQualityLevelByIds(Long[] ids)
    {
        String str = StringUtils.join(ids, ",");
        //校验数据引用关系
        //生产领料
        String qcCheck = SqlCheckUtil.sqlCheck("number",
                "qc_report_quality_entry b left join qc_report_quality a on a.id = b.main_id",
                "where b.level_id in(  "+str+" ) and a.status ='A'" +
                        "group by a.number");
        if(qcCheck!=null){
            return AjaxResult.error("所选数据在质检单["+qcCheck+"]中已被引用，无法删除");
        }
        int i = qualityLevelMapper.deleteQualityLevelByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

}
