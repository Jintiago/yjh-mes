package cn.sourceplan.warehouse.mapper;

import java.util.List;
import cn.sourceplan.warehouse.domain.OtherOut;
import cn.sourceplan.warehouse.domain.OtherOutEntry;
import cn.sourceplan.warehouse.domain.SaleOutEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 其他出库单主表Mapper接口
 *
 * @author jinzhong
 * @date 2023-09-28
 */
@Repository
public interface OtherOutMapper extends BaseMapper<OtherOut>
{
    /**
     * 查询其他出库单主表
     *
     * @param id 其他出库单主表主键
     * @return 其他出库单主表
     */
    OtherOut selectOtherOutById(Long id);

    /**
     * 查询其他出库单主表列表
     *
     * @param otherOut 其他出库单主表
     * @return 其他出库单主表集合
     */
    List<OtherOut> selectOtherOutList(OtherOut otherOut);

}
