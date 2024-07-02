package cn.sourceplan.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 字段扩展对象 sys_field_extend
 *
 * @author jinzhong
 * @date 2023-05-23
 */
@Data
@TableName("sys_field_extend")
public class FieldExtend extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 原单据 */
    private String sourceBill;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 字段名 */
    @Excel(name = "字段名")
    private String field;

    /** 字段文本 */
    @Excel(name = "字段文本")
    private String fieldName;

    /** 字段类型 */
    @Excel(name = "字段类型")
    private String type;

    /** 引用数据 */
    @Excel(name = "引用数据")
    private String quoteData;

    /** 引用字段 */
    @Excel(name = "引用字段")
    private String quoteField;

    /** 计算公式 */
    @Excel(name = "计算公式")
    private String formula;

    /** 是否唯一 */
    @Excel(name = "是否唯一")
    private String isRequired;

    /** 是否必录 */
    @Excel(name = "是否必录")
    private String isMust;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 默认值 */
    @Excel(name = "默认值")
    private String defaultValue;

    /** 排序 */
    @Excel(name = "宽度")
    private Integer width;

    /** 是否系统字段 */
    private String isSystem;
}
