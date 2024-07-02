package cn.sourceplan.finance.mapper;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.finance.domain.Payroll;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 工资单Mapper接口
 *
 * @author jinzhong
 * @date 2023-03-11
 */
@Repository
public interface PayrollMapper extends BaseMapper<Payroll>
{


    /**
     * 批量删除工资单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deletePayrollByIds(Long[] ids);

    BigDecimal sumByUserId(@Param("beginDate") String beginDate,@Param("endDate") String endDate,@Param("userId") Long id);
}
