package cn.sourceplan.production.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 工序路线明细对象 pro_route_process
 *
 * @author jinzhong
 * @date 2022-09-22
 */
@TableName("pro_route_process")
@Data
public class RouteProcess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 工序路线id */
    @Excel(name = "工序路线id")
    private Long routeId;

    /** 工序id */
    @Excel(name = "工序id")
    private Long processId;

    /** 工序名 */
    @Excel(name = "工序名")
    private String processName;

    /** 工序顺序 */
    @Excel(name = "工序顺序")
    private Long sort;

    /** 工序关系 */
    @Excel(name = "工序关系")
    private String nextRule;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 质检类型 */
    @Excel(name = "质检类型")
    private String qcType;

    /** 首件数量 */
    @Excel(name = "首检数量")
    private BigDecimal firstQcQuantity;
}
