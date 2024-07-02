package cn.sourceplan.common.domain;

import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 编码规则对象 sys_code_rule
 *
 * @author jinzhong
 * @date 2023-01-17
 */
@Data
@TableName("sys_code_rule")
public class SysCodeRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 基础元素 */
    @Excel(name = "基础元素")
    private String basicDomain;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 编码规则子信息 */
    @TableField(exist = false)
    private List<SysCodeRuleEntry> sysCodeRuleEntryList;

}
