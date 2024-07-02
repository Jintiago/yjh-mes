package cn.sourceplan.barcode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 条码生成记录对象 wm_barcode_record
 *
 * @author jinzhong
 * @date 2023-04-15
 */
@Data
@TableName("wm_barcode_record")
public class BarcodeRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 条码格式 */
    private String format;

    /** 条码类型 */
    @Excel(name = "条码类型")
    private String type;

    /** 类型ID */
    private Long typeId;

    /** 类型编号 */
    @Excel(name = "类型编号")
    private String typeNumber;

    /** 类型名称 */
    @Excel(name = "类型名称")
    private String typeName;

    /** 编码规则ID */
    private Long codeRuleId;

    /** 生成数量 */
    @Excel(name = "生成数量")
    private Long quantity;

    /** 条码区间 */
    @Excel(name = "条码区间")
    @TableField("`INTERVAL`")
    private String interval;

    /** 参数 */
    private String parameter;

    /** 打印次数 */
    private Integer printCount;

}
