package cn.sourceplan.production.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.sourceplan.quality.domain.ReportQuality;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 报工单对象 pro_report
 *
 * @author jinzhong
 * @date 2023-02-15
 */
@Data
@TableName("pro_report")
public class Report extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 工单子表ID */
    private Long workOrderEntryId;

    /** 报工人ID */
    private Long reportUserId;

    /** 报工人名称 */
    @Excel(name = "报工人名称")
    private String reportUserName;

    /** 报工单号 */
    @Excel(name = "报工单号")
    private String number;

    /** 报工途径 */
    @Excel(name = "报工途径")
    private String reportChannel;

    /** 报工时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报工时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reportTime;

    /** 报工数量 */
    @Excel(name = "报工数量")
    private BigDecimal reportQuantity;

    /** 合格数量 */
    @Excel(name = "合格数量")
    private BigDecimal qualifiedQuantity;

    /** 不合格数量 */
    @Excel(name = "不合格数量")
    private BigDecimal unqualifiedQuantity;

    /** 车间ID */
    @Excel(name = "车间ID")
    private Long workshopId;

    /** 车间名称 */
    @Excel(name = "车间名称")
    private String workshopName;

    /** 工位ID */
    @Excel(name = "工位ID")
    private Long stationId;

    /** 工位名称 */
    @Excel(name = "工位名称")
    private String stationName;

    /** 是否结算工资 */
    @Excel(name = "是否结算工资")
    private String isSettle;

    /** 结算工资 */
    @Excel(name = "结算工资")
    private BigDecimal wages;

    /** 质检状态
     *  A免检，B待检验、C检验通过、D检验异常
     * */
    @Excel(name = "质检状态")
    private String qualityStatus;

    /** SN码数组 */
    @Excel(name = "SN码数组")
    private String snArray;

    /*以下字段仅用于前端展示，数据库不进行存储*/

    @TableField(exist = false)
    private ReportQuality reportQuality;

    /** 工单编号 */
    @Excel(name = "工单编号")
    @TableField(exist = false)
    private String workOrderNumber;

    /** 质检类型 */
    @Excel(name = "质检类型")
    @TableField(exist = false)
    private String qcType;

    /** 工单名称 */
    @Excel(name = "工单名称")
    @TableField(exist = false)
    private String workOrderName;

    /** 产品编号 */
    @Excel(name = "产品编号")
    @TableField(exist = false,condition = SqlCondition.LIKE)
    private String materialNumber;

    /** 产品编号 */
    @Excel(name = "产品编号")
    @TableField(exist = false)
    private Long materialId;

    /** 产品名称 */
    @Excel(name = "产品名称")
    @TableField(exist = false,condition = SqlCondition.LIKE)
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    @TableField(exist = false)
    private String specification;

    /** 单位名称 */
    @Excel(name = "单位名称")
    @TableField(exist = false)
    private String materialUnitName;

    /** 排产数量 */
    @Excel(name = "排产数量")
    @TableField(exist = false)
    private BigDecimal quantity;

    /** 计划完成日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划完成日期", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date planFinishDate;

    /** 工单状态 */
    @Excel(name = "工单状态")
    @TableField(exist = false)
    private String proStatus;

    /** 工单备注 */
    @Excel(name = "工单备注")
    @TableField(exist = false)
    private String workOrderRemark;

    /** 工序名称 */
    @Excel(name = "工序名称")
    @TableField(exist = false)
    private String processName;

    /** 工序ID */
    @Excel(name = "工序ID")
    @TableField(exist = false)
    private Long processId;

    /** 批次号 */
    @Excel(name = "批次号")
    @TableField(exist = false)
    private String batchNumber;

    /** 工序顺序 */
    @Excel(name = "工序顺序")
    @TableField(exist = false)
    private String sort;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 客户名称*/
    @TableField(exist = false)
    private String customerName;

    /** 扩展字段
     * 报工单的扩展字段取生产工单上的
     * */
    @TableField(exist = false)
    private String extendField;

    public void setExtendField(String extendField) {
        this.extendField = extendField;
        this.extendFieldJson = JSONObject.parseObject(extendField);
    }

    /** 扩展字段 String转json*/
    @TableField(exist = false)
    private JSONObject extendFieldJson;


    @TableField(exist = false)
    private String[] reportTimeQuery;
}
