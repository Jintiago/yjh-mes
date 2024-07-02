package cn.sourceplan.warehouse.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 生产入库单子表对象 wm_manufacture_into_entry
 *
 * @author jinzhong
 * @date 2022-11-10
 */
@TableName("wm_manufacture_into_entry")
@Data
public class ManufactureIntoEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 主表ID */
    private Long mainId;

    /** 序号 */
    private int sort;

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

    /** 单位ID */
    private Long materialUnitId;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String materialUnitName;

    /** 数量 */
    @Excel(name = "数量")
    private BigDecimal quantity;

    /** 批次编号 */
    @Excel(name = "批次编号")
    private String batchNumber;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库编号 */
    @Excel(name = "仓库编号")
    private String warehouseNumber;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String warehouseName;

    /** 库区ID */
    private Long regionId;

    /** 库区编号 */
    private String regionNumber;

    /** 库区名称 */
    private String regionName;

    /** 库位ID */
    private Long areaId;

    /** 库位编号 */
    private String areaNumber;

    /** 库位名称 */
    private String areaName;

    /** 生产日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date manufactureDate;

    @TableField(exist = false)
    private ManufactureInto manufactureInto;

    /** 扩展字段 */
    @Excel(name = "扩展字段")
    private String extendField;

    public void setExtendField(String extendField) {
        this.extendField = extendField;
        this.extendFieldJson = JSONObject.parseObject(extendField);
    }

    /** 扩展字段 String转json*/
    @TableField(exist = false)
    private JSONObject extendFieldJson;
}
