package cn.sourceplan.integration.service;


import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.integration.domain.IntegrateDataSource;

import java.util.List;

/**
 * 集成数据源Service接口
 *
 * @author 靳忠
 * @date 2023-02-11
 */
public interface IIntegrateDataSourceService
{
    /**
     * 查询集成数据源
     *
     * @param id 集成数据源主键
     * @return 集成数据源
     */
    public IntegrateDataSource selectIntegrateDataSourceById(Long id);

    /**
     * 查询集成数据源列表
     *
     * @param integrateDataSource 集成数据源
     * @return 集成数据源集合
     */
    public List<IntegrateDataSource> selectIntegrateDataSourceList(IntegrateDataSource integrateDataSource);

    /**
     * 新增集成数据源
     *
     * @param integrateDataSource 集成数据源
     * @return 结果
     */
    public AjaxResult insertIntegrateDataSource(IntegrateDataSource integrateDataSource);

    /**
     * 修改集成数据源
     *
     * @param integrateDataSource 集成数据源
     * @return 结果
     */
    public AjaxResult updateIntegrateDataSource(IntegrateDataSource integrateDataSource);

    /**
     * 批量删除集成数据源
     *
     * @param ids 需要删除的集成数据源主键集合
     * @return 结果
     */
    public int deleteIntegrateDataSourceByIds(Long[] ids);

    /**
     * 删除集成数据源信息
     *
     * @param id 集成数据源主键
     * @return 结果
     */
    public int deleteIntegrateDataSourceById(Long id);
}
