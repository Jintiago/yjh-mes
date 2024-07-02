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
 * 设备项目对象 dm_inspection_item
 *
 * @author jinzhong
 * @date 2023-04-08
 */
@Data
@TableName("dm_inspection_item")
public class InspectionItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String number;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 项目类型 */
    @Excel(name = "项目类型")
    private String type;

    /** 项目内容 */
    @Excel(name = "项目内容")
    private String content;

    /** 标准 */
    @Excel(name = "标准")
    private String standard;

    /** 启用状态 */
    @Excel(name = "启用状态")
    private String status;


}
