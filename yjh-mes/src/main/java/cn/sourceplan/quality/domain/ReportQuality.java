package cn.sourceplan.quality.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 报工质检单对象 qc_report_quality
 *
 * @author jinzhong
 * @date 2023-02-21
 */
@Data
@TableName("qc_report_quality")
public class ReportQuality extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 报工单ID */
    @Excel(name = "报工单ID")
    private String reportIds;

    /** 报工单编号 */
    @Excel(name = "报工单编号")
    private String reportNumber;

    /** 工单编号 */
    @Excel(name = "工单编号")
    private String workOrderNumber;

    /** 工序 */
    @Excel(name = "工序")
    private String processName;

    /** 产品编码 */
    @Excel(name = "产品编码")
    private String materialNumber;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specification;

    /** 单位 */
    @Excel(name = "单位")
    private String materialUnitName;

    /** 批次号 */
    @Excel(name = "批次号")
    private String batchNumber;

    /** 质检类型 */
    @Excel(name = "质检类型")
    private String qcType;

    /** 检测数量 */
    @Excel(name = "检测数量")
    private BigDecimal quantity;

    /** 合格数量 */
    @Excel(name = "合格数量")
    private BigDecimal qualifiedQuantity;

    /** 不合格数量 */
    @Excel(name = "不合格数量")
    private BigDecimal unqualifiedQuantity;

    /** 报工时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报工时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reportTime;

    /** 检测时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "检测时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    /** 质检员ID */
    @Excel(name = "质检员ID")
    private Long checkUserId;

    /** 质检员名称 */
    @Excel(name = "质检员名称")
    private String checkUserName;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** SN码数组 */
    @Excel(name = "SN码数组")
    private String snArray;

    @TableField(exist = false)
    private List<ReportQualityEntry> qualityLevelList;
}
