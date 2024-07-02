package cn.sourceplan.quality.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.quality.domain.Reason;

/**
 * 质检原因Service接口
 *
 * @author jinzhong
 * @date 2023-02-21
 */
public interface IReasonService
{
    /**
     * 查询质检原因
     *
     * @param id 质检原因主键
     * @return 质检原因
     */
    public Reason selectReasonById(Long id);

    /**
     * 查询质检原因列表
     *
     * @param reason 质检原因
     * @return 质检原因集合
     */
    public List<Reason> selectReasonList(Reason reason);

    /**
     * 新增质检原因
     *
     * @param reason 质检原因
     * @return 结果
     */
    public int insertReason(Reason reason);

    /**
     * 修改质检原因
     *
     * @param reason 质检原因
     * @return 结果
     */
    public AjaxResult updateReason(Reason reason);

    /**
     * 批量删除质检原因
     *
     * @param ids 需要删除的质检原因主键集合
     * @return 结果
     */
    public AjaxResult deleteReasonByIds(Long[] ids);

}
