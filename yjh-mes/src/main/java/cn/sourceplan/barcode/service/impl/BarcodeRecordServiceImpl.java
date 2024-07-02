package cn.sourceplan.barcode.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.barcode.domain.MaterialSn;
import cn.sourceplan.barcode.mapper.MaterialSnMapper;
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.masterdata.domain.Material;
import cn.sourceplan.masterdata.domain.Unit;
import cn.sourceplan.masterdata.mapper.MaterialMapper;
import cn.sourceplan.masterdata.mapper.UnitMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.barcode.mapper.BarcodeRecordMapper;
import cn.sourceplan.barcode.domain.BarcodeRecord;
import cn.sourceplan.barcode.service.IBarcodeRecordService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 条码生成记录Service业务层处理
 *
 * @author jinzhong
 * @date 2023-04-15
 */
@Service
public class BarcodeRecordServiceImpl implements IBarcodeRecordService
{
    @Autowired
    private BarcodeRecordMapper barcodeRecordMapper;

    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    @Autowired
    private MaterialSnMapper materialSnMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private UnitMapper unitMapper;

    /**
     * 查询条码生成记录
     *
     * @param id 条码生成记录主键
     * @return 条码生成记录
     */
    @Override
    public BarcodeRecord selectBarcodeRecordById(Long id)
    {

        return barcodeRecordMapper.selectById(id);
    }

    /**
     * 查询条码生成记录列表
     *
     * @param barcodeRecord 条码生成记录
     * @return 条码生成记录
     */
    @Override
    public List<BarcodeRecord> selectBarcodeRecordList(BarcodeRecord barcodeRecord)
    {
        QueryWrapper<BarcodeRecord> qw = new QueryWrapper<>(barcodeRecord);
        return barcodeRecordMapper.selectList(qw);
    }

    /**
     * 新增条码生成记录
     *
     * @param barcodeRecord 条码生成记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBarcodeRecord(BarcodeRecord barcodeRecord)
    {
        int insert=0;
        if("materialSN".equals(barcodeRecord.getType())){
            //封装List
            List<MaterialSn> materialList = new ArrayList<>();
            //批量生成物理SN码
            Long qSize = barcodeRecord.getQuantity();
            SysCodeRule queryDomain  = new SysCodeRule();
            queryDomain.setBasicDomain("materialSN");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            MaterialSn materialSn = new MaterialSn();
            Material material = materialMapper.selectById(barcodeRecord.getTypeId());
            materialSn.setMaterialId(material.getId());
            materialSn.setMaterialName(material.getName());
            materialSn.setMaterialNumber(material.getNumber());
            Unit unit = unitMapper.selectById(material.getUnitId());
            materialSn.setMaterialUnitName(unit.getName());
            //拿到条码生成参数中的 批次号
            String parameter = barcodeRecord.getParameter();
            JSONObject jsonObject = JSONObject.parseObject(parameter);
            if(jsonObject!=null){
                String batchNumber = (String) jsonObject.get("batchNumber");
                materialSn.setBatchNumber(batchNumber);
            }
            insert = barcodeRecordMapper.insert(barcodeRecord);
            materialSn.setMainId( barcodeRecord.getId() );
            String interval = "";
            for (int i = 0; i < qSize; i++) {
                MaterialSn materialSnTmp = new MaterialSn();
                BeanUtils.copyProperties(materialSn,materialSnTmp);
                String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(),true,barcodeRecord.getTypeId());
                materialSnTmp.setSnCode(number);
                //获取本次SN码区间
                if(i==0){
                    interval+=number+" 至 ";
                }
                if(i==qSize-1){
                    interval+=number;
                }
                materialList.add(materialSnTmp);
            }
            //批量插入
            materialSnMapper.insertBatchSomeColumn(materialList);
            barcodeRecord.setInterval(interval);
        }
        return insert;
    }

    /**
     * 修改条码生成记录
     *
     * @param barcodeRecord 条码生成记录
     * @return 结果
     */
    @Override
    public int updateBarcodeRecord(BarcodeRecord barcodeRecord)
    {
        return barcodeRecordMapper.updateById(barcodeRecord);
    }

    /**
     * 批量删除条码生成记录
     *
     * @param ids 需要删除的条码生成记录主键
     * @return 结果
     */
    @Override
    public int deleteBarcodeRecordByIds(Long[] ids)
    {

        return barcodeRecordMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除条码生成记录信息
     *
     * @param id 条码生成记录主键
     * @return 结果
     */
    @Override
    public int deleteBarcodeRecordById(Long id)
    {
        return barcodeRecordMapper.deleteById(id);
    }
}
