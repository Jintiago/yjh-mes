package cn.sourceplan.warehouse.service;

import java.util.List;

import cn.sourceplan.warehouse.domain.ProPick;
import cn.sourceplan.warehouse.domain.ProPickEntry;

/**
 * 生产领料单Service接口
 *
 * @author jinzhong
 * @date 2023-02-23
 */
public interface IProPickService
{
    /**
     * 查询生产领料单
     *
     * @param id 生产领料单主键
     * @return 生产领料单
     */
    public ProPick selectProPickById(Long id);

    /**
     * 查询生产领料单列表
     *
     * @param proPick 生产领料单
     * @return 生产领料单集合
     */
    public List<ProPickEntry> selectProPickList(ProPick proPick);

    /**
     * 新增生产领料单
     *
     * @param proPick 生产领料单
     * @return 结果
     */
    public int insertProPick(ProPick proPick);

    /**
     * 修改生产领料单
     *
     * @param proPick 生产领料单
     * @return 结果
     */
    public int updateProPick(ProPick proPick);

    /**
     * 批量删除生产领料单
     *
     * @param ids 需要删除的生产领料单主键集合
     * @return 结果
     */
    public int deleteProPickByIds(Long[] ids);

    /**
     * 删除生产领料单信息
     *
     * @param id 生产领料单主键
     * @return 结果
     */
    public int deleteProPickById(Long id);

    int batchCheck(Long[] ids, String checkStatus);
}
