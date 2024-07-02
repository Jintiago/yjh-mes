package cn.sourceplan.warehouse.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 仓库信息对象 wm_warehouse
 *
 * @author jinzhong
 * @date 2022-11-03
 */
@TableName("wm_warehouse")
@Data
public class Warehouse extends BaseEntity
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

    /** 位置 */
    private String location;

    /** 面积 */
    private BigDecimal area;

    /** 负责人 */
    @Excel(name = "负责人")
    @TableField(value = "user",condition = SqlCondition.LIKE)
    private String user;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

}
