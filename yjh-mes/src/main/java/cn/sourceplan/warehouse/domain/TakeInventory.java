package cn.sourceplan.warehouse.domain;

import java.util.List;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 库存盘点对象 wm_take_inventory
 *
 * @author jinzhong
 * @date 2023-07-20
 */
@TableName("wm_take_inventory")
@Data
public class TakeInventory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 期初库存标记 */
    private String initializeFlag;

    /** 库存校正标记 */
    @Excel(name = "库存校正标记")
    private String correctFlag;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库编号 */
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

    /** 仓管员ID */
    private Long keeperId;

    /** 仓管名称 */
    @Excel(name = "仓管名称")
    private String keeperName;

    /** 盘点时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "盘点时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date takeTime;

    /** 审核人 */
    private String checker;

    /** 审核日期 */
    private Date checkDate;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** SN码数组 */
    private String snArray;

    /** 库存盘点明细信息 */
    @TableField(exist = false)
    private List<TakeInventoryEntry> takeInventoryEntryList;


}
