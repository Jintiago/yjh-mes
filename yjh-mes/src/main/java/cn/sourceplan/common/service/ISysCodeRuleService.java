package cn.sourceplan.common.service;

import java.util.List;
import cn.sourceplan.common.domain.SysCodeRule;

/**
 * 编码规则Service接口
 *
 * @author jinzhong
 * @date 2023-01-17
 */
public interface ISysCodeRuleService
{
    /**
     * 查询编码规则
     *
     * @param id 编码规则主键
     * @return 编码规则
     */
    SysCodeRule selectSysCodeRuleById(Long id);

    /**
     * 查询编码规则列表
     *
     * @param sysCodeRule 编码规则
     * @return 编码规则集合
     */
    List<SysCodeRule> selectSysCodeRuleList(SysCodeRule sysCodeRule);

    /**
     * 新增编码规则
     *
     * @param sysCodeRule 编码规则
     * @return 结果
     */
    int insertSysCodeRule(SysCodeRule sysCodeRule);

    /**
     * 修改编码规则
     *
     * @param sysCodeRule 编码规则
     * @return 结果
     */
    int updateSysCodeRule(SysCodeRule sysCodeRule);

    /**
     * 批量删除编码规则
     *
     * @param ids 需要删除的编码规则主键集合
     * @return 结果
     */
    int deleteSysCodeRuleByIds(Long[] ids);

    /**
     * 获取/更新编码
     * 编码规则有基础资料时调用
     * @param id codeRule 编码规则的ID
     * @param isUpdate 是否更新
     * @param sourceValueId 基础元素ID,不需要则传null
     * @return 结果
     */
    String queryNewCodeById(Long id, Boolean isUpdate,Long sourceValueId);

    /**
     * 获取/更新编码
     * 编码规则无基础资料时调用
     *
     * @param id codeRule 编码规则的ID
     * @param isUpdate 是否更新
     * @return 结果
     */
    String queryNewCodeById(Long id, Boolean isUpdate);

}
