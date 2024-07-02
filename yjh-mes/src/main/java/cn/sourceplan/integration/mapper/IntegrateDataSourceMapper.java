package cn.sourceplan.integration.mapper;

import cn.sourceplan.integration.domain.InteDataSourceField;
import cn.sourceplan.integration.domain.IntegrateDataSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 集成数据源Mapper接口
 *
 * @author 靳忠
 * @date 2023-02-11
 */
@Repository
public interface IntegrateDataSourceMapper
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
    public int insertIntegrateDataSource(IntegrateDataSource integrateDataSource);

    /**
     * 修改集成数据源
     *
     * @param integrateDataSource 集成数据源
     * @return 结果
     */
    public int updateIntegrateDataSource(IntegrateDataSource integrateDataSource);

    /**
     * 删除集成数据源
     *
     * @param id 集成数据源主键
     * @return 结果
     */
    public int deleteIntegrateDataSourceById(Long id);

    /**
     * 批量删除集成数据源
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteIntegrateDataSourceByIds(Long[] ids);

    /**
     * 批量删除数据源字段
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInteDataSourceFieldByMainIds(Long[] ids);

    /**
     * 批量新增数据源字段
     *
     * @param inteDataSourceFieldList 数据源字段列表
     * @return 结果
     */
    public int batchInteDataSourceField(List<InteDataSourceField> inteDataSourceFieldList);


    /**
     * 通过集成数据源主键删除数据源字段信息
     *
     * @param id 集成数据源ID
     * @return 结果
     */
    public int deleteInteDataSourceFieldByMainId(Long id);
}
