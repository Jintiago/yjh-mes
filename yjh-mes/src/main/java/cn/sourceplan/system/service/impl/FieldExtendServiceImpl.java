package cn.sourceplan.system.service.impl;

import java.util.Arrays;
import java.util.List;
import cn.sourceplan.common.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.system.mapper.FieldExtendMapper;
import cn.sourceplan.system.domain.FieldExtend;
import cn.sourceplan.system.service.IFieldExtendService;

/**
 * 字段扩展Service业务层处理
 *
 * @author jinzhong
 * @date 2023-05-23
 */
@Service
public class FieldExtendServiceImpl implements IFieldExtendService
{
    @Autowired
    private FieldExtendMapper fieldExtendMapper;

    /**
     * 查询字段扩展
     *
     * @param id 字段扩展主键
     * @return 字段扩展
     */
    @Override
    public FieldExtend selectFieldExtendById(Long id)
    {
        return fieldExtendMapper.selectById(id);
    }

    /**
     * 查询字段扩展列表
     *
     * @param fieldExtend 字段扩展
     * @return 字段扩展
     */
    @Override
    public List<FieldExtend> selectFieldExtendList(FieldExtend fieldExtend)
    {
        QueryWrapper<FieldExtend> qw = new QueryWrapper<>(fieldExtend);
        qw.orderByAsc("sort");
        qw.ne("status","1");
        return fieldExtendMapper.selectList(qw);
    }

    /**
     * 新增字段扩展
     *
     * @param fieldExtend 字段扩展
     * @return 结果
     */
    @Override
    public int insertFieldExtend(List<FieldExtend> fieldExtend)
    {
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("source_bill","saleOrderEntry");
        fieldExtendMapper.delete(qw);
        return fieldExtendMapper.insertBatchSomeColumn(fieldExtend);
    }

    /**
     * 修改字段扩展
     *
     * @param fieldExtend 字段扩展
     * @return 结果
     */
    @Override
    public int updateFieldExtend(FieldExtend fieldExtend)
    {
        return fieldExtendMapper.updateById(fieldExtend);
    }

    /**
     * 批量删除字段扩展
     *
     * @param ids 需要删除的字段扩展主键
     * @return 结果
     */
    @Override
    public int deleteFieldExtendByIds(Long[] ids)
    {
        return fieldExtendMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除字段扩展信息
     *
     * @param id 字段扩展主键
     * @return 结果
     */
    @Override
    public int deleteFieldExtendById(Long id)
    {
        return fieldExtendMapper.deleteById(id);
    }
}
