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
 * 工资单对象 fm_payroll
 *
 * @author jinzhong
 * @date 2023-03-11
 */
@Data
@TableName("fm_payroll")
public class Payroll extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 人员ID */
    private Long userId;

    /** 人员名称 */
    @Excel(name = "人员名称")
    private String userName;

    /** 报工单IDs */
    private String reportIds;

    /** 报工单Numbers */
    private String reportNumbers;

    /** 应发工资 */
    @Excel(name = "应发工资")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal shouldPay;

    /** 实发工资 */
    @Excel(name = "实发工资")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal realPay;

    /** 计价类型 */
    private String wageType;

    /** 计价方案信息 */
    private String planInfo;

    /** 工资单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "工资单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payrollDate;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

}
