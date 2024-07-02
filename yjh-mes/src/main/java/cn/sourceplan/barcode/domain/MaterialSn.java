package cn.sourceplan.barcode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;

/**
 * 物料SN码对象 wm_material_sn
 *
 * @author jinzhong
 * @date 2023-04-15
 */
@Data
@TableName("wm_material_sn")
public class MaterialSn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** 条码生成ID */
    private Long mainId;

    /** SN码 */
    @Excel(name = "SN码")
    private String snCode;

    /** 物料ID */
    private Long materialId;

    /** 物料编号 */
    @Excel(name = "物料编号")
    private String materialNumber;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 规格型号 */
    private String specification;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String materialUnitName;

    /** 批次编号 */
    @Excel(name = "批次编号")
    private String batchNumber;

    /** 条码内容 */
    @Excel(name = "条码内容")
    private String content;

}
