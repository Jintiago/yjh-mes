package cn.sourceplan.integration.service;


import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.integration.domain.IntegratePlan;

import java.util.List;

/**
 * 集成方案Service接口
 *
 * @author 靳忠
 * @date 2023-02-13
 */
public interface IIntegratePlanService
{
    /**
     * 查询集成方案
     *
     * @param id 集成方案主键
     * @return 集成方案
     */
    public IntegratePlan selectIntegratePlanById(Long id);

    /**
     * 查询集成方案列表
     *
     * @param integratePlan 集成方案
     * @return 集成方案集合
     */
    public List<IntegratePlan> selectIntegratePlanList(IntegratePlan integratePlan);

    /**
     * 新增集成方案
     *
     * @param integratePlan 集成方案
     * @return 结果
     */
    public AjaxResult insertIntegratePlan(IntegratePlan integratePlan);

    /**
     * 修改集成方案
     *
     * @param integratePlan 集成方案
     * @return 结果
     */
    public AjaxResult updateIntegratePlan(IntegratePlan integratePlan);

    /**
     * 批量删除集成方案
     *
     * @param ids 需要删除的集成方案主键集合
     * @return 结果
     */
    public int deleteIntegratePlanByIds(Long[] ids);

    /**
     * 删除集成方案信息
     *
     * @param id 集成方案主键
     * @return 结果
     */
    public int deleteIntegratePlanById(Long id);


}
