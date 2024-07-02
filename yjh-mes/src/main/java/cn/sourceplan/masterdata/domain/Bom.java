package cn.sourceplan.masterdata.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.TreeEntity;

/**
 * 物料BOM对象 md_bom
 *
 * @author jinzhong
 * @date 2022-09-16
 */
@Data
@TableName(value = "md_bom")
public class Bom extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 版本号 */
    @Excel(name = "版本号")
    private String number;

    /** 物料id */
    @Excel(name = "物料id")
    private Long materialId;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 比例 */
    @Excel(name = "比例")
    private BigDecimal proportion;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    @TableField(exist = false)
    private Material material;

}
