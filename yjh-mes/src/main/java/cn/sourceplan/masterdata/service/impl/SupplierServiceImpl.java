package cn.sourceplan.masterdata.service.impl;

import java.util.Arrays;
import java.util.List;
import cn.sourceplan.common.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.masterdata.mapper.SupplierMapper;
import cn.sourceplan.masterdata.domain.Supplier;
import cn.sourceplan.masterdata.service.ISupplierService;

/**
 * 供应商Service业务层处理
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@Service
public class SupplierServiceImpl implements ISupplierService
{
    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 查询供应商
     *
     * @param id 供应商主键
     * @return 供应商
     */
    @Override
    public Supplier selectSupplierById(Long id)
    {
        return supplierMapper.selectById(id);
    }

    /**
     * 查询供应商列表
     *
     * @param supplier 供应商
     * @return 供应商
     */
    @Override
    public List<Supplier> selectSupplierList(Supplier supplier)
    {
        QueryWrapper<Supplier> qw = new QueryWrapper<>(supplier);
        return supplierMapper.selectList(qw);
    }

    /**
     * 新增供应商
     *
     * @param supplier 供应商
     * @return 结果
     */
    @Override
    public int insertSupplier(Supplier supplier)
    {
        return supplierMapper.insert(supplier);
    }

    /**
     * 修改供应商
     *
     * @param supplier 供应商
     * @return 结果
     */
    @Override
    public int updateSupplier(Supplier supplier)
    {
        return supplierMapper.updateById(supplier);
    }

    /**
     * 批量删除供应商
     *
     * @param ids 需要删除的供应商主键
     * @return 结果
     */
    @Override
    public int deleteSupplierByIds(Long[] ids)
    {
        return supplierMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除供应商信息
     *
     * @param id 供应商主键
     * @return 结果
     */
    @Override
    public int deleteSupplierById(Long id)
    {
        return supplierMapper.deleteById(id);
    }
}
