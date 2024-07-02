package cn.sourceplan.warehouse.service;

import java.util.List;

import cn.sourceplan.warehouse.domain.ManufactureInto;

/**
 * 完工入库单Service接口
 *
 * @author jinzhong
 * @date 2022-11-10
 */
public interface IManufactureIntoService
{
    /**
     * 查询完工入库单
     *
     * @param id 完工入库单主键
     * @return 完工入库单
     */
    ManufactureInto selectManufactureIntoById(Long id);

    /**
     * 查询完工入库单列表
     *
     * @param manufactureInto 完工入库单
     * @return 完工入库单集合
     */
    List<ManufactureInto> selectManufactureIntoList(ManufactureInto manufactureInto);

    /**
     * 新增完工入库单
     *
     * @param manufactureInto 完工入库单
     * @return 结果
     */
    int insertManufactureInto(ManufactureInto manufactureInto);

    /**
     * 修改完工入库单
     *
     * @param manufactureInto 完工入库单
     * @return 结果
     */
    int updateManufactureInto(ManufactureInto manufactureInto);

    /**
     * 批量删除完工入库单
     *
     * @param ids 需要删除的完工入库单主键集合
     * @return 结果
     */
    int deleteManufactureIntoByIds(Long[] ids);

    /**
     * 删除完工入库单信息
     *
     * @param id 完工入库单主键
     * @return 结果
     */
    int deleteManufactureIntoById(Long id);
}
