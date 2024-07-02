package cn.sourceplan.finance.domain;

import cn.sourceplan.common.annotation.Excel;
import cn.sourceplan.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 薄膜核算对象 tr_bomochengben
 *
 * @author jinzhong
 * @date 2023-07-04
 */
@Data
@TableName("tr_bomochengben_neimao")
public class BomochengbenNeimao extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 名称 */
    @Excel(name = "名称")
    @TableField(value = "name",condition = SqlCondition.LIKE)
    private String name;

    /** 核算方式 */
    @Excel(name = "核算方式")
    private String priceType;

    /** 颜色 */
    @Excel(name = "颜色")
    private String yanse;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 吹膜厚度 */
    @Excel(name = "吹膜厚度")
    private BigDecimal chuimohd;

    /** 宽度 */
    @Excel(name = "宽度")
    private BigDecimal kd;

    /** 米数 */
    @Excel(name = "米数")
    private BigDecimal mishu;

    /** 吹膜速度 */
    @Excel(name = "吹膜速度")
    private BigDecimal chuimosd;

    /** 原料价格 */
    @Excel(name = "原料价格")
    private BigDecimal yuanliaojg;

    /** 胶水价格 */
    @Excel(name = "胶水价格")
    private BigDecimal jiaoshuijg;

    /** 胶水类型 */
    @Excel(name = "胶水类型")
    private String jiaoshuiType;

    /** 吹膜材料成本 */
    @Excel(name = "吹膜材料成本")
    private BigDecimal chuimocailiaocb;

    /** 吹膜人工成本 */
    @Excel(name = "吹膜人工成本")
    private BigDecimal chuimorengcb;

    /** 吹膜电费成本 */
    @Excel(name = "吹膜电费成本")
    private BigDecimal chuimodianfeicb;

    /** 上胶量 */
    @Excel(name = "上胶量")
    private BigDecimal shangjiaoliang;

    /** 胶水材料成本 */
    @Excel(name = "胶水材料成本")
    private BigDecimal jiaoshuicailiangcb;

    /** 涂布机器速度 */
    @Excel(name = "涂布机器速度")
    private BigDecimal tubujiqisd;

    /** 涂布人工成本 */
    @Excel(name = "涂布人工成本")
    private BigDecimal tuburengcb;

    /** 涂布电费成本 */
    @Excel(name = "涂布电费成本")
    private BigDecimal tubudianfeicb;

    /** 纸管 */
    @Excel(name = "纸管")
    private BigDecimal zhiguan;

    /** 纸片 */
    @Excel(name = "纸片")
    private BigDecimal zhipian;

    /** 人工 */
    @Excel(name = "人工")
    private BigDecimal rengong;

    /** 印刷 */
    @Excel(name = "印刷")
    private BigDecimal yinshua;

    /** 运费 */
    @Excel(name = "运费")
    private BigDecimal yunfei;

    /** 成本 */
    @Excel(name = "成本")
    private BigDecimal chengben;

    /** 基准价 */
    @Excel(name = "基准价")
    private BigDecimal jizhunjia;

    /** id */
    @TableId(type= IdType.AUTO)
    private Long id;



    /** 包装 */
    @Excel(name = "包装")
    private BigDecimal baozhuang;

    /** 分切 */
    @Excel(name = "分切")
    private BigDecimal fenqie;
    /** 功能性 */
    @Excel(name = "功能性")
    private BigDecimal gongneng;
    /** 不含税基准价 */
    @Excel(name = "不含税基准价")
    private BigDecimal jizhunjiaB;
    /** 外贸含税基准价 */
    @Excel(name = "外贸含税基准价")
    private BigDecimal jizhunjiaWaimao;
    /** 外贸不含税基准价 */
    @Excel(name = "外贸不含税基准价")
    private BigDecimal jizhunjiaWaimaoB;
    /** 纸管参数 */
    @Excel(name = "纸管参数")
    private BigDecimal zhiguanP;
    /** 包装参数 */
    @Excel(name = "包装参数")
    private BigDecimal baozhuangP;
    /** 分切参数 */
    @Excel(name = "分切参数")
    private BigDecimal fenqieP;

    /** 抗UV参数 */
    @Excel(name = "抗UV参数")
    private BigDecimal kanguvP;

    /** 抗UV */
    @Excel(name = "抗UV")
    private BigDecimal kanguv;

}
