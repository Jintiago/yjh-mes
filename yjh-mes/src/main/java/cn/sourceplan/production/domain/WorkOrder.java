package cn.sourceplan.production.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

import cn.sourceplan.sale.domain.SalOrder;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 工单对象 pro_workorder
 *
 * @author jinzhong
 * @date 2022-12-12
 */
@Data
@TableName("pro_workorder")
public class WorkOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 伪列 */
    @TableField(exist = false)
    private Long rowNum;

    /** 编号 */
    @Excel(name = "编号")
    @TableField(condition = SqlCondition.LIKE)
    private String number;

    /** 工单名称 */
    private String name;

    /** 来源类型 */
    private String sourceType;

    /** 来源系统 */
    private String sourceSystem;

    /** 来源信息 */
    private String sourceInfo;

    /** 批次号 */
    @Excel(name = "批次号")
    private String batchNumber;

    /** 产品ID */
    @Excel(name = "产品ID")
    private Long materialId;

    /** 产品编号 */
    @Excel(name = "产品编号")
    private String materialNumber;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specification;

    /** 单位ID */
    @Excel(name = "单位ID")
    private Long materialUnitId;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String materialUnitName;

    /** 生产数量 */
    @Excel(name = "生产数量")
    private BigDecimal quantity;

    /** 开始生产日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginProDate;

    /** 计划完成日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划完成日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planFinishDate;

    /** 实际完成日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际完成日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date realFinishDate;

    /** 生产状态 */
    @Excel(name = "生产状态")
    @TableField(condition = "%s in (${%s})")
    private String proStatus;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 是否超期 */
    @Excel(name = "是否超期")
    private String isPostpone;

    /** 工单分录信息 */
    @TableField(exist = false)
    private List<WorkOrderEntry> workOrderEntryList;

    /** BOM展开 */
    @TableField(exist = false)
    private List<WorkOrder> children;

    /** 返回给前端用的当前行索引 */
    @TableField(exist = false)
    private int index;

    /** 返回给前端用的父项行索引 */
    @TableField(exist = false)
    private Integer parentIndex;

    /** 当前库存 */
    @TableField(exist = false)
    private Double inventory;

    /** 移动端标签用的状态 */
    @TableField(exist = false)
    private String mobileStatus;

    /** 返修标记 */
    private String repairFlag;

    /** 优先级 */
    private Integer priority;

    /** 附件 */
    private String annex;

    /** 对应销售订单 */
    @TableField(exist = false)
    private SalOrder salOrder;

    /** 扩展字段 */
    private String extendField;

    public void setExtendField(String extendField) {
        this.extendField = extendField;
        this.extendFieldJson = JSONObject.parseObject(extendField);
    }

    /** 扩展字段 String转json*/
    @TableField(exist = false)
    private JSONObject extendFieldJson;

    /** 自定义编号
     *  为移动端服务，根据工位，设备自定义排序
     * */
    private Integer customSort;

    /** 工序路线ID */
    private Long routeId;
}
