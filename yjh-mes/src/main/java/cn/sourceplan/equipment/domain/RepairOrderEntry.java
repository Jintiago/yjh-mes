package cn.sourceplan.equipment.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 设备维修单明细对象 dm_repair_order_entry
 *
 * @author jinzhong
 * @date 2023-04-08
 */
@Data
@TableName("dm_repair_order_entry")
public class RepairOrderEntry extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 维修单ID */
    @Excel(name = "维修单ID")
    private Long mainId;

    /** 项目ID */
    @Excel(name = "项目ID")
    private Long itemId;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String itemNumber;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 项目类型 */
    @Excel(name = "项目类型")
    private String itemType;

    /** 项目内容 */
    @Excel(name = "项目内容")
    private String itemContent;

    /** 标准 */
    @Excel(name = "标准")
    private String itemStandard;

    /** 故障图片 */
    private String pictureUrl;


}
