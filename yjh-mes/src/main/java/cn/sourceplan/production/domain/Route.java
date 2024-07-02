package cn.sourceplan.production.domain;

import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 工序路线对象 pro_route
 *
 * @author jinzhong
 * @date 2022-09-22
 */
@TableName("pro_route")
@Data
public class Route extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    @TableField(value = "number",condition = SqlCondition.LIKE)
    private String number;

    /** 工序路线名称 */
    @Excel(name = "工序路线名称")
    @TableField(value = "name",condition = SqlCondition.LIKE)
    private String name;

    /** 工序路线说明 */
    @Excel(name = "工序路线说明")
    private String describes;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 工序路线明细信息 */
    @TableField(exist = false)
    private List<RouteProcess> routeProcessList;


}
