package cn.sourceplan.finance.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 计件工资方案对象 fm_piece_wage_plan
 *
 * @author jinzhong
 * @date 2023-03-09
 */
@Data
@TableName("fm_piece_wage_plan")
public class PieceWagePlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 工序id */
    private Long processId;

    /** 工序名 */
    @Excel(name = "工序名")
    private String processName;

    /** 物料ID */
    private Long materialId;

    /** 物料编号 */
    @Excel(name = "物料编号")
    private String materialNumber;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specification;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String materialUnitName;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 岗位ID */
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    private Long postId;

    /** 岗位名称 */
    @Excel(name = "岗位名称")
    @TableField(updateStrategy=FieldStrategy.IGNORED)
    private String postName;

    /** 开始时间 */
    private Date timeBegin;

    /** 结束时间 */
    private Date timeEnd;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

}
