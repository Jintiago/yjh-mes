package cn.sourceplan.masterdata.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 车间对象 md_workshop
 *
 * @author jinzhong
 * @date 2022-09-21
 */
@TableName("md_workshop")
@Data
public class Workshop extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    @TableField(value = "number",condition = SqlCondition.LIKE)
    private String number;

    /** 名称 */
    @Excel(name = "名称")
    @TableField(value = "name",condition = SqlCondition.LIKE)
    private String name;

    /** 负责人 */
    @Excel(name = "负责人")
    @TableField(value = "person",condition = SqlCondition.LIKE)
    private String person;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

}
