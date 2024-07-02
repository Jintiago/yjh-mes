package cn.sourceplan.quality.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jinzhong
 */
@Data
@TableName("qc_report_quality_reason")
public class ReportQualityReason {

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;

    /** mainId */
    private Long mainId;

    /** 原因ID */
    private Long reasonId;

    /** 原因名称 */
    private String reasonName;

    /** 数量 */
    private BigDecimal quantity;

}
