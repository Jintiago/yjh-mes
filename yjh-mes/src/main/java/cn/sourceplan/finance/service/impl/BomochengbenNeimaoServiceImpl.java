package cn.sourceplan.finance.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.sourceplan.finance.domain.Bomochengben;
import cn.sourceplan.finance.domain.BomochengbenNeimao;
import cn.sourceplan.finance.domain.Bomoweihu;
import cn.sourceplan.finance.mapper.BomochengbenNeimaoMapper;
import cn.sourceplan.finance.mapper.BomoweihuMapper;
import cn.sourceplan.finance.service.IBomochengbenNeimaoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 薄膜核算Service业务层处理
 *
 * @author jinzhong
 * @date 2023-07-04
 */
@Service
public class BomochengbenNeimaoServiceImpl implements IBomochengbenNeimaoService
{
    @Autowired
    private BomochengbenNeimaoMapper bomochengbenMapper;

    @Autowired
    private BomoweihuMapper bomoweihuMapper;

    /**
     * 查询薄膜核算
     *
     * @param id 薄膜核算主键
     * @return 薄膜核算
     */
    @Override
    public BomochengbenNeimao selectBomochengbenNeimaoById(Long id)
    {
        return bomochengbenMapper.selectById(id);
    }

    /**
     * 查询薄膜核算列表
     *
     * @param bomochengben 薄膜核算
     * @return 薄膜核算
     */
    @Override
    public List<BomochengbenNeimao> selectBomochengbenNeimaoList(BomochengbenNeimao bomochengben)
    {
        QueryWrapper<BomochengbenNeimao> qw = new QueryWrapper<>(bomochengben);
        return bomochengbenMapper.selectList(qw);
    }

    /**
     * 新增薄膜核算
     *
     * @param bomochengben 薄膜核算
     * @return 结果
     */
    @Override
    public int insertBomochengbenNeimao(BomochengbenNeimao bomochengben)
    {
        return bomochengbenMapper.insert(chengbenjisuan(bomochengben));
    }

    /**
     * 修改薄膜核算
     *
     * @param bomochengben 薄膜核算
     * @return 结果
     */
    @Override
    public int updateBomochengbenNeimao(BomochengbenNeimao bomochengben)
    {
        return bomochengbenMapper.updateById(chengbenjisuan(bomochengben));
    }

    /**
     * 批量删除薄膜核算
     *
     * @param ids 需要删除的薄膜核算主键
     * @return 结果
     */
    @Override
    public int deleteBomochengbenNeimaoByIds(Long[] ids)
    {
        return bomochengbenMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除薄膜核算信息
     *
     * @param id 薄膜核算主键
     * @return 结果
     */
    @Override
    public int deleteBomochengbenNeimaoById(Long id)
    {
        return bomochengbenMapper.deleteById(id);
    }


    BomochengbenNeimao chengbenjisuan(BomochengbenNeimao domain){
        //获取，计价方式及类型，查询符合要求的后台参数
        String priceType = domain.getPriceType();
        QueryWrapper<Bomoweihu>  bmwhQW = new QueryWrapper<>();
        bmwhQW.eq("type",domain.getType());
        bmwhQW.orderByDesc("update_time,create_time");
        Bomoweihu bomoweihu = bomoweihuMapper.selectOne(bmwhQW);
        //原料价格
        BigDecimal yuanliaojg = bomoweihu.getYuanliaojg();
        //胶水价格
        BigDecimal jiaoshuijg = bomoweihu.getJiaoshuijg();
        domain.setYuanliaojg(yuanliaojg);
        domain.setJiaoshuijg(jiaoshuijg);
        //吹膜厚度
        BigDecimal chuimohd = domain.getChuimohd();
        //宽度
        BigDecimal kd = domain.getKd();
        //米数
        BigDecimal mishu = domain.getMishu();
        //吹膜速度
        BigDecimal chuimosd = domain.getChuimosd();
        //上胶量
        BigDecimal shangjiaoliang = domain.getShangjiaoliang();
        //涂布机器速度
        BigDecimal tubujiqisd = domain.getTubujiqisd();
        domain.setTubujiqisd(tubujiqisd);
        //颜色
        String yanse = domain.getYanse();
        //抗UV参数
        BigDecimal kanguvP = domain.getKanguvP();
        if("公斤价".equals(priceType)){

            //计算吹膜材料成本
            BigDecimal aaa =  NumberUtil.div(NumberUtil.mul(0.925, chuimohd),100) ;
            BigDecimal aab = NumberUtil.div(NumberUtil.mul(shangjiaoliang, 0.55), 1000);
            BigDecimal aac = NumberUtil.div(1000, NumberUtil.add(aaa, aab));
            BigDecimal aad = NumberUtil.mul(NumberUtil.mul(0.925, aac), chuimohd);
            BigDecimal aae = NumberUtil.mul(NumberUtil.div(aad, 100), yuanliaojg);
            domain.setChuimocailiaocb(aae);

            //计算吹膜人工成本
            BigDecimal aba = NumberUtil.div(NumberUtil.div(1000, chuimohd), 0.925);
            BigDecimal abb = NumberUtil.div(NumberUtil.mul(aba, 100), chuimosd);
            BigDecimal abc = NumberUtil.div(NumberUtil.div(abb, 2), 60);
            BigDecimal abd = NumberUtil.div(NumberUtil.mul(abc, 16.2), kd);
            domain.setChuimorengcb(abd);

            //计算吹膜电费成本
            BigDecimal acd = NumberUtil.div(NumberUtil.mul(abc, 60), kd);
            domain.setChuimodianfeicb(acd);

            //计算材料成本(胶水) jiaoshuicailiangcb
            BigDecimal ada = NumberUtil.div(NumberUtil.mul(0.925, chuimohd), 100);
            BigDecimal adb = NumberUtil.div(NumberUtil.mul(0.55, shangjiaoliang), 1000);
            BigDecimal adc = NumberUtil.div(1000, ada.add(adb)).multiply(shangjiaoliang).multiply(jiaoshuijg);
            domain.setJiaoshuicailiangcb(NumberUtil.div(adc,100) );

            //计算涂布人工成本tuburengcb
            BigDecimal aea = NumberUtil.div(NumberUtil.div(1000, chuimohd), 0.925);
            BigDecimal aeb = NumberUtil.div(NumberUtil.mul(aea, 100), tubujiqisd);
            BigDecimal aec =NumberUtil.mul(NumberUtil.div(aeb, 60),49.6);
            domain.setTuburengcb(aec.divide(kd,8, BigDecimal.ROUND_HALF_UP));

            //计算涂布电费成本
            BigDecimal afc =NumberUtil.mul(NumberUtil.div(aeb, 60),90);
            domain.setTubudianfeicb(afc.divide(kd,8, BigDecimal.ROUND_HALF_UP));

            //计算纸管
            BigDecimal aga = chuimohd.add(NumberUtil.div(shangjiaoliang, 20));
            BigDecimal agb = NumberUtil.div(NumberUtil.div(1000, aga), 0.93);
            BigDecimal agc = NumberUtil.div(NumberUtil.mul(agb, 100), mishu);
            domain.setZhiguan( NumberUtil.mul(agc,10));

            //计算纸片
            domain.setZhipian( NumberUtil.mul(agc,4));
            //计算人工
            domain.setRengong(NumberUtil.mul(agc,4.5));
            //计算印刷
            BigDecimal aha = NumberUtil.div(NumberUtil.mul(shangjiaoliang, 0.55), 1000);
            BigDecimal ahb = NumberUtil.div(NumberUtil.mul(0.925, chuimohd), 100);
            BigDecimal ahc = NumberUtil.mul(NumberUtil.div(1000, aha.add(ahb)),new BigDecimal(yanse)  );
            domain.setYinshua(ahc);

            //计算抗UV
            BigDecimal uva = NumberUtil.div(1000, aha.add(ahb));
            domain.setKanguv(uva.multiply(kanguvP.multiply(chuimohd)));
            //计算成本
            BigDecimal chuimocailiaocb = domain.getChuimocailiaocb();
            BigDecimal yinshua = domain.getYinshua();
            BigDecimal chuimorengcb = domain.getChuimorengcb();
            BigDecimal chuimodianfeicb = domain.getChuimodianfeicb();
            BigDecimal tuburengcb = domain.getTuburengcb();
            BigDecimal tubudianfeicb = domain.getTubudianfeicb();
            BigDecimal zhiguan = domain.getZhiguan();
            BigDecimal zhipian = domain.getZhipian();
            BigDecimal rengong = domain.getRengong();
            BigDecimal yunfei = domain.getYunfei();
            BigDecimal kanguv = domain.getKanguv();
            BigDecimal jiaoshuicailiangcb = domain.getJiaoshuicailiangcb();
            BigDecimal chengben = chuimocailiaocb.add(yinshua).add(chuimorengcb).add(chuimodianfeicb)
                    .add(tuburengcb)
                    .add(tubudianfeicb)
                    .add(zhiguan)
                    .add(zhipian)
                    .add(kanguv)
                    .add(rengong).add(yunfei).add(jiaoshuicailiangcb);
            domain.setChengben( NumberUtil.div(chengben,1000)  );
            BigDecimal jizhunjia = NumberUtil.div(domain.getChengben(), 0.675);
            domain.setJizhunjia(jizhunjia);
            domain.setJizhunjiaB(NumberUtil.mul(jizhunjia,0.93));
        }

        if("平方价".equals(priceType)){

            //计算吹膜材料成本
            //吹膜厚度*0.925*原料价格/100
            BigDecimal aaa =  NumberUtil.mul(NumberUtil.mul(0.925, chuimohd),yuanliaojg) ;
            domain.setChuimocailiaocb(NumberUtil.div(aaa,100));

            //计算吹膜人工成本
            //16.6/(吹膜速度*60*2)/宽度
            BigDecimal aba = NumberUtil.div(16.6, NumberUtil.mul(chuimosd, 120));
            domain.setChuimorengcb(NumberUtil.div(aba,kd));

            //计算吹膜电费成本
            //60/(吹膜速度*60*2)/宽度
            BigDecimal abc = NumberUtil.div(60, NumberUtil.mul(chuimosd, 120));
            domain.setChuimodianfeicb(NumberUtil.div(abc,kd));

            //计算材料成本(胶水) jiaoshuicailiangcb
            //上胶量*胶水价格/100
            BigDecimal ada = NumberUtil.div(NumberUtil.mul(shangjiaoliang, jiaoshuijg), 100);
            domain.setJiaoshuicailiangcb(ada);

            //计算涂布人工成本tuburengcb
            //49.8/(机器速度*60)/宽度
            BigDecimal aeb = NumberUtil.div(49.8, NumberUtil.mul(tubujiqisd, 60));
            domain.setTuburengcb(NumberUtil.div(aeb,kd)  );

            //计算涂布电费成本
            //90/(机器速度*60)/宽度
            BigDecimal afc =NumberUtil.div(90, NumberUtil.mul(tubujiqisd, 60));
            domain.setTubudianfeicb(  NumberUtil.div(afc,kd) );

            //计算抗UV
            domain.setKanguv(kanguvP.multiply(chuimohd));

            //计算纸管
            //11/米数
            domain.setZhiguan( NumberUtil.div(10,mishu));

            //计算纸片
            //2/米数
            domain.setZhipian( NumberUtil.div(2,mishu));
            //计算人工
            //45/20/米数
            domain.setRengong(NumberUtil.div(2.25,mishu));
            //计算成本
            BigDecimal chuimocailiaocb = domain.getChuimocailiaocb();
            BigDecimal chuimorengcb = domain.getChuimorengcb();
            BigDecimal chuimodianfeicb = domain.getChuimodianfeicb();
            BigDecimal tuburengcb = domain.getTuburengcb();
            BigDecimal tubudianfeicb = domain.getTubudianfeicb();
            BigDecimal zhiguan = domain.getZhiguan();
            BigDecimal zhipian = domain.getZhipian();
            BigDecimal rengong = domain.getRengong();
            BigDecimal yunfei = domain.getYunfei();
            BigDecimal kanguv = domain.getKanguv();
            BigDecimal jiaoshuicailiangcb = domain.getJiaoshuicailiangcb();
            BigDecimal chengben = chuimocailiaocb.add(chuimorengcb).add(chuimodianfeicb)
                    .add(tuburengcb)
                    .add(tubudianfeicb)
                    .add(zhiguan)
                    .add(zhipian)
                    .add(kanguv)
                    .add(rengong).add(yunfei).add(jiaoshuicailiangcb).add(new BigDecimal(yanse) );
            domain.setChengben(chengben);
            BigDecimal jizhunjia = NumberUtil.div(chengben, 0.675);
            domain.setJizhunjia(jizhunjia);
            domain.setJizhunjiaB(NumberUtil.mul(jizhunjia,0.93));

        }
        return domain ;
    }
}
