package cn.sourceplan.warehouse.mapper;

import java.util.List;

import cn.sourceplan.warehouse.domain.ProcureInto;
import cn.sourceplan.warehouse.domain.ProcureIntoEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 采购入库单Mapper接口
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@Repository
public interface ProcureIntoMapper extends BaseMapper<ProcureInto>
{
    /**
     * 查询采购入库单
     *
     * @param id 采购入库单主键
     * @return 采购入库单
     */
    ProcureInto selectProcureIntoById(Long id);

    /**
     * 查询采购入库单列表
     *
     * @param procureInto 采购入库单
     * @return 采购入库单集合
     */
    List<ProcureIntoEntry> selectProcureIntoList(ProcureInto procureInto);

    /**
     * 删除采购入库单
     *
     * @param id 采购入库单主键
     * @return 结果
     */
    public int deleteProcureIntoById(Long id);

    /**
     * 批量删除采购入库单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProcureIntoByIds(Long[] ids);

    /**
     * 批量删除采购入库单子表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProcureIntoEntryByMainIds(Long[] ids);


    /**
     * 通过采购入库单主键删除采购入库单子表信息
     *
     * @param id 采购入库单ID
     * @return 结果
     */
    public int deleteProcureIntoEntryByMainId(Long id);
}
