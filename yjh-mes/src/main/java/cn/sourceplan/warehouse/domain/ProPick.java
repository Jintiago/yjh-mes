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
 * 生产领料单对象
 *
 * @author jinzhong
 * @date 2023-02-23
 */
@TableName("wm_production_pick")
@Data
public class ProPick extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工单子表ID */
    private Long workOrderEntryId;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 领料单编号 */
    @Excel(name = "领料单编号")
    private String number;

    /** 车间ID */
    private Long workshopId;

    /** 车间名称 */
    private String workshopName;

    /** 工位ID */
    private Long stationId;

    /** 工位名称 */
    private String stationName;

    /** 工单子表IDs */
    private String workOrderEntryIds;

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

    /** 领料人ID */
    private Long pickUserId;

    /** 领料人名称 */
    @Excel(name = "领料人名称")
    private String pickUserName;

    /** 仓管员ID */
    private Long keeperId;

    /** 仓管名称 */
    @Excel(name = "仓管名称")
    private String keeperName;

    /** 领料时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "领料时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date pickTime;

    /** 审核人 */
    @Excel(name = "审核人")
    private String checker;

    /** 审核日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkDate;

    /** 状态 */
    private String status;

    /** SN码数组 */
    @Excel(name = "SN码数组")
    private String snArray;

    /** 生产领料单子表信息 */
    @TableField(exist = false)
    private List<ProPickEntry> proPickEntryList;


}
