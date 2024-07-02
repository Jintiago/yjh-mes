package cn.sourceplan.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.common.domain.SysCodeRule;
import org.springframework.stereotype.Repository;

/**
 * 编码规则Mapper接口
 *
 * @author jinzhong
 * @date 2023-01-17
 */
@Repository
public interface SysCodeRuleMapper extends BaseMapper<SysCodeRule>
{
    /**
     * 查询编码规则
     *
     * @param id 编码规则主键
     * @return 编码规则
     */
    SysCodeRule selectSysCodeRuleById(Long id);

    /**
     * 批量删除编码规则子
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSysCodeRuleEntryByRuleIds(Long[] ids);


    /**
     * 通过编码规则主键删除编码规则子信息
     *
     * @param id 编码规则ID
     * @return 结果
     */
    int deleteSysCodeRuleEntryByRuleId(Long id);
}
