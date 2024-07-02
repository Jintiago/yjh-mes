package cn.sourceplan.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.masterdata.domain.Customer;
import org.springframework.stereotype.Repository;

/**
 * 客户Mapper接口
 *
 * @author jinzhong
 * @date 2022-09-29
 */
@Repository
public interface CustomerMapper extends BaseMapper<Customer>
{

    /**
     * 删除客户
     *
     * @param id 客户主键
     * @return 结果
     */
    int deleteCustomerById(Long id);

    /**
     * 批量删除客户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCustomerByIds(Long[] ids);
}
