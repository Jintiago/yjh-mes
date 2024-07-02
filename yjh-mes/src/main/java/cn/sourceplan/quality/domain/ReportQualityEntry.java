package cn.sourceplan.quality.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jinzhong
 */
@Data
@TableName("qc_report_quality_entry")
public class ReportQualityEntry {


    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** mainId */
    private Long mainId;

    /** 等级ID */
    private Long levelId;

    /** 等级名称 */
    private String levelName;

    /** 类型 */
    private String type;

    /** 数量 */
    private BigDecimal quantity;

    @TableField(exist = false)
    private List<ReportQualityReason> reasonList ;

}
