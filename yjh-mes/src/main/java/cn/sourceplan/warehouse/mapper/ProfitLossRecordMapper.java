package cn.sourceplan.warehouse.mapper;

import cn.sourceplan.warehouse.domain.ProfitLossRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 盘点盈亏记录Mapper接口
 *
 * @author jinzhong
 * @date 2023-07-20
 */
@Repository
public interface ProfitLossRecordMapper extends BaseMapper<ProfitLossRecord>
{
    /**
     * 查询盘点盈亏记录
     *
     * @param id 盘点盈亏记录主键
     * @return 盘点盈亏记录
     */
    public ProfitLossRecord selectProfitLossRecordById(Long id);


}
