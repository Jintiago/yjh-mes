package cn.sourceplan.system.service;

import java.util.List;
import cn.sourceplan.system.domain.FieldExtend;

/**
 * 字段扩展Service接口
 *
 * @author jinzhong
 * @date 2023-05-23
 */
public interface IFieldExtendService
{
    /**
     * 查询字段扩展
     *
     * @param id 字段扩展主键
     * @return 字段扩展
     */
    public FieldExtend selectFieldExtendById(Long id);

    /**
     * 查询字段扩展列表
     *
     * @param fieldExtend 字段扩展
     * @return 字段扩展集合
     */
    public List<FieldExtend> selectFieldExtendList(FieldExtend fieldExtend);

    /**
     * 新增字段扩展
     *
     * @param fieldExtend 字段扩展
     * @return 结果
     */
    public int insertFieldExtend(List<FieldExtend> fieldExtend);

    /**
     * 修改字段扩展
     *
     * @param fieldExtend 字段扩展
     * @return 结果
     */
    public int updateFieldExtend(FieldExtend fieldExtend);

    /**
     * 批量删除字段扩展
     *
     * @param ids 需要删除的字段扩展主键集合
     * @return 结果
     */
    public int deleteFieldExtendByIds(Long[] ids);

    /**
     * 删除字段扩展信息
     *
     * @param id 字段扩展主键
     * @return 结果
     */
    public int deleteFieldExtendById(Long id);
}
