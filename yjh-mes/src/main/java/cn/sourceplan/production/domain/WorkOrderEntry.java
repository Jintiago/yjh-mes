package cn.sourceplan.production.domain;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 工单分录对象 pro_workorder_entry
 *
 * @author jinzhong
 * @date 2022-12-12
 */
@Data
@TableName("pro_workorder_entry")
public class WorkOrderEntry
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

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

    /** 工单ID */
    @Excel(name = "工单ID")
    private Long workorderId;

    /** 工序ID */
    @Excel(name = "工序ID")
    private Long processId;

    /** 工序名称 */
    @Excel(name = "工序名称")
    private String processName;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 质检类型 */
    @Excel(name = "质检类型")
    private String qcType;

    /** 首件数量 */
    @Excel(name = "首检数量")
    private BigDecimal firstQcQuantity;

    /** 领料ID */
    private Long pickMaterialId;

    /** 领料规格 */
    private String pickSpecification;

    /** 领料名称 */
    private String pickMaterialName;

    /** 领料数量 */
    private BigDecimal pickQuantity;


    /** 自动入库ID */
    private Long inWarehouseId;

    /** 入库名称 */
    private String inWarehouseName;

    /** 自动出库ID */
    private Long outWarehouseId;

    /** 出库名称 */
    private String outWarehouseName;

    /** 扩展字段 */
    private String extendField;

    public void setExtendField(String extendField) {
        this.extendField = extendField;
        this.extendFieldJson = JSONObject.parseObject(extendField);
    }

    /** 扩展字段 String转json*/
    @TableField(exist = false)
    private JSONObject extendFieldJson;

    /** 设备ID */
    private Long machineId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String machineName;

    /** 类型：标准A ,领料pick，报工report */
    private String type;

    /** 报工物料ID */
    private Long reportMaterialId;

    /** 报工物料规格 */
    private String reportSpecification;

    /** 报工物料名称 */
    private String reportMaterialName;

    /** 报工物料数量 */
    private BigDecimal reportQuantity;


    /** 完成状态
     *  A：未完成  B:已完成
     * */
    private String status;

    /** 备注 */
    private String remark;


    /** 工序顺序 */
    private Integer processSort;

}
