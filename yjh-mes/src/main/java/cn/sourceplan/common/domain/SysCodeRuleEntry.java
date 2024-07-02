package cn.sourceplan.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 编码规则子对象 sys_code_rule_entry
 *
 * @author jinzhong
 * @date 2023-01-17
 */
@Data
@TableName("sys_code_rule_entry")
public class SysCodeRuleEntry
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 主表ID */
    @Excel(name = "主表ID")
    private Long ruleId;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 类型ID */
    @Excel(name = "类型ID")
    private String typeId;

    /** 流水长度 */
    @Excel(name = "流水长度")
    private Integer lengthFlow;

    /** 流水号 */
    @Excel(name = "流水号")
    private Integer maxFlow;

    /** 补位符 */
    @Excel(name = "补位符")
    private String codeCover;

    /** 日期格式 */
    @Excel(name = "日期格式")
    private String dateFormat;

    /** 常量 */
    @Excel(name = "常量")
    private String constantChar;

    /** 元素键值 */
    @Excel(name = "元素键值")
    private String elementSourceTable;

    /** 元素属性 */
    @Excel(name = "元素属性")
    private String sourceAttribute;

    /** 元素ID */
    @Excel(name = "元素ID")
    private Long sourceValue;

    /** 当前日期 */
    private String maxDate;


}
