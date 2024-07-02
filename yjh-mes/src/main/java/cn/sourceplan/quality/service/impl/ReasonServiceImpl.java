package cn.sourceplan.quality.service.impl;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.SqlCheckUtil;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.quality.mapper.ReasonMapper;
import cn.sourceplan.quality.domain.Reason;
import cn.sourceplan.quality.service.IReasonService;

/**
 * 质检原因Service业务层处理
 *
 * @author jinzhong
 * @date 2023-02-21
 */
@Service
public class ReasonServiceImpl implements IReasonService
{
    @Autowired
    private ReasonMapper reasonMapper;

    /**
     * 查询质检原因
     *
     * @param id 质检原因主键
     * @return 质检原因
     */
    @Override
    public Reason selectReasonById(Long id)
    {
        return reasonMapper.selectReasonById(id);
    }

    /**
     * 查询质检原因列表
     *
     * @param reason 质检原因
     * @return 质检原因
     */
    @Override
    public List<Reason> selectReasonList(Reason reason)
    {
        return reasonMapper.selectReasonList(reason);
    }

    /**
     * 新增质检原因
     *
     * @param reason 质检原因
     * @return 结果
     */
    @Override
    public int insertReason(Reason reason)
    {
        return reasonMapper.insert(reason);
    }

    /**
     * 修改质检原因
     *
     * @param reason 质检原因
     * @return 结果
     */
    @Override
    public AjaxResult updateReason(Reason reason)
    {
        //校验数据引用关系
        //报工质检
        String qcCheck = SqlCheckUtil.sqlCheck("number",
                "qc_report_quality_reason b left join qc_report_quality a on a.id = b.main_id ",
                "where b.reason_id =  "+reason.getId()+"  and status ='A' "+
                        "group by a.number");
        if(qcCheck!=null){
            return AjaxResult.error("该数据在质检单["+qcCheck+"]中已被引用，无法修改");
        }
        int i = reasonMapper.updateById(reason);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 批量删除质检原因
     *
     * @param ids 需要删除的质检原因主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteReasonByIds(Long[] ids)
    {

        String str = StringUtils.join(ids, ",");
        //校验数据引用关系
        //生产领料
        String qcCheck = SqlCheckUtil.sqlCheck("number",
                "qc_report_quality_reason b left join qc_report_quality a on a.id = b.main_id",
                "where b.reason_id in(  "+str+" ) and a.status ='A'" +
                        "group by a.number");
        if(qcCheck!=null){
            return AjaxResult.error("所选数据在质检单["+qcCheck+"]中已被引用，无法删除");
        }
        int i = reasonMapper.deleteReasonByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

}
