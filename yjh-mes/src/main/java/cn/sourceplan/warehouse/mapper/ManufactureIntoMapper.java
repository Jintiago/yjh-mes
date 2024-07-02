package cn.sourceplan.warehouse.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.warehouse.domain.ManufactureInto;
import org.springframework.stereotype.Repository;

/**
 * 完工入库单Mapper接口
 *
 * @author jinzhong
 * @date 2022-11-10
 */
@Repository
public interface ManufactureIntoMapper extends BaseMapper<ManufactureInto>
{


    /**
     * 删除完工入库单
     *
     * @param id 完工入库单主键
     * @return 结果
     */
     int deleteManufactureIntoById(Long id);

    /**
     * 批量删除完工入库单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
     int deleteManufactureIntoByIds(Long[] ids);

    /**
     * 批量删除生产入库单子表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
     int deleteManufactureIntoEntryByMainIds(Long[] ids);

    /**
     * 通过完工入库单主键删除生产入库单子表信息
     *
     * @param id 完工入库单ID
     * @return 结果
     */
     int deleteManufactureIntoEntryByMainId(Long id);


     ManufactureInto selectManufactureIntoById(Long id);

     List<ManufactureInto> selectManufactureIntoList(ManufactureInto mi);
}
