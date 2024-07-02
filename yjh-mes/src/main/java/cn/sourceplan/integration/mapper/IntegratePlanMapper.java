package cn.sourceplan.integration.mapper;


import cn.sourceplan.integration.domain.IntePlanField;
import cn.sourceplan.integration.domain.IntegratePlan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 集成方案Mapper接口
 *
 * @author 靳忠
 * @date 2023-02-13
 */
@Repository
public interface IntegratePlanMapper
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
    public int insertIntegratePlan(IntegratePlan integratePlan);

    /**
     * 修改集成方案
     *
     * @param integratePlan 集成方案
     * @return 结果
     */
    public int updateIntegratePlan(IntegratePlan integratePlan);

    /**
     * 删除集成方案
     *
     * @param id 集成方案主键
     * @return 结果
     */
    public int deleteIntegratePlanById(Long id);

    /**
     * 批量删除集成方案
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteIntegratePlanByIds(Long[] ids);

    /**
     * 批量删除集成方案字段
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteIntePlanFieldByMainIds(Long[] ids);

    /**
     * 批量新增集成方案字段
     *
     * @param intePlanFieldList 集成方案字段列表
     * @return 结果
     */
    public int batchIntePlanField(List<IntePlanField> intePlanFieldList);


    /**
     * 通过集成方案主键删除集成方案字段信息
     *
     * @param id 集成方案ID
     * @return 结果
     */
    public int deleteIntePlanFieldByMainId(Long id);
}
