package cn.sourceplan.warehouse.service.impl;

import java.util.Arrays;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.common.utils.SecurityUtils;
import cn.sourceplan.warehouse.domain.ProfitLossRecord;
import cn.sourceplan.warehouse.domain.ProfitLossRecordEntry;
import cn.sourceplan.warehouse.mapper.ProfitLossRecordEntryMapper;
import cn.sourceplan.warehouse.mapper.ProfitLossRecordMapper;
import cn.sourceplan.warehouse.mapper.TakeInventoryEntryMapper;
import cn.sourceplan.warehouse.domain.TakeInventory;
import cn.sourceplan.warehouse.domain.TakeInventoryEntry;
import cn.sourceplan.warehouse.mapper.TakeInventoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.warehouse.service.IProfitLossRecordService;

/**
 * 盘点盈亏记录Service业务层处理
 *
 * @author jinzhong
 * @date 2023-07-20
 */
@Service
public class ProfitLossRecordServiceImpl implements IProfitLossRecordService
{
    @Autowired
    private ProfitLossRecordMapper profitLossRecordMapper;

    @Autowired
    private ProfitLossRecordEntryMapper profitLossRecordEntryMapper;

    @Autowired
    private TakeInventoryMapper takeInventoryMapper;

    @Autowired
    private TakeInventoryEntryMapper takeInventoryEntryMapper;

    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    /**
     * 查询盘点盈亏记录
     *
     * @param id 盘点盈亏记录主键
     * @return 盘点盈亏记录
     */
    @Override
    public ProfitLossRecord selectProfitLossRecordById(Long id)
    {
        return profitLossRecordMapper.selectProfitLossRecordById(id);
    }

    /**
     * 查询盘点盈亏记录列表
     *
     * @param profitLossRecord 盘点盈亏记录
     * @return 盘点盈亏记录
     */
    @Override
    public List<ProfitLossRecord> selectProfitLossRecordList(ProfitLossRecord profitLossRecord)
    {
        QueryWrapper<ProfitLossRecord> qw = new QueryWrapper<>(profitLossRecord);
        return profitLossRecordMapper.selectList(qw);
    }

    /**
     * 新增盘点盈亏记录
     *
     * @param profitLossRecord 盘点盈亏记录
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertProfitLossRecord(ProfitLossRecord profitLossRecord)
    {

        //如果编号为null,则自动为其生成编号
        if ( StringUtils.isBlank(profitLossRecord.getNumber()) ){
            SysCodeRule queryDomain  = new SysCodeRule();
            queryDomain.setBasicDomain("ProfitLossRecord");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(),true);
            profitLossRecord.setNumber(number);
        }
        int rows = profitLossRecordMapper.insert(profitLossRecord);
        insertProfitLossRecordEntry(profitLossRecord);
        return rows;
    }

    /**
     * 修改盘点盈亏记录
     *
     * @param profitLossRecord 盘点盈亏记录
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateProfitLossRecord(ProfitLossRecord profitLossRecord)
    {
        QueryWrapper<ProfitLossRecordEntry> qw = new QueryWrapper<>();
        qw.eq("main_id",profitLossRecord.getId());
        profitLossRecordEntryMapper.delete(qw);
        insertProfitLossRecordEntry(profitLossRecord);
        return profitLossRecordMapper.updateById(profitLossRecord);
    }

    /**
     * 批量删除盘点盈亏记录
     *
     * @param ids 需要删除的盘点盈亏记录主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteProfitLossRecordByIds(Long[] ids)
    {
        QueryWrapper<ProfitLossRecordEntry> qw = new QueryWrapper<>();
        qw.in("main_id",ids);
        profitLossRecordEntryMapper.delete(qw);
        return profitLossRecordMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除盘点盈亏记录信息
     *
     * @param id 盘点盈亏记录主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteProfitLossRecordById(Long id)
    {
        QueryWrapper<ProfitLossRecordEntry> qw = new QueryWrapper<>();
        qw.eq("main_id",id);
        profitLossRecordEntryMapper.delete(qw);
        return profitLossRecordMapper.deleteById(id);
    }

    @Override
    public AjaxResult addByTakeInventory(Long id) {
        //获取所有盘点单
        TakeInventory takeInventory = takeInventoryMapper.selectTakeInventoryById(id);
        //封装盈亏单
        List<TakeInventoryEntry> takeInventoryEntryList = takeInventory.getTakeInventoryEntryList();

        //盘盈单
        ProfitLossRecord plr= new ProfitLossRecord();
        //表头
        plr =sameProfitLossRecordPackage(plr,takeInventory);
        plr.setType("A");
        List<ProfitLossRecordEntry> profitLossRecordEntryList = new ArrayList<>();
        plr.setProfitLossRecordEntryList(profitLossRecordEntryList);
        //盘亏单
        ProfitLossRecord plrB = new ProfitLossRecord();
        plrB = sameProfitLossRecordPackage(plrB,takeInventory);
        plrB.setType("B");
        List<ProfitLossRecordEntry> lossRecordEntryList = new ArrayList<>();
        plrB.setProfitLossRecordEntryList(lossRecordEntryList);
        for (int i = 0; i < takeInventoryEntryList.size(); i++) {
            TakeInventoryEntry takeInventoryEntry = takeInventoryEntryList.get(i);
            String status = takeInventoryEntry.getStatus();
            ProfitLossRecordEntry plre = new ProfitLossRecordEntry();
            //表体
            plre.setMaterialId(takeInventoryEntry.getMaterialId());
            plre.setMaterialName(takeInventoryEntry.getMaterialName());
            plre.setMaterialNumber(takeInventoryEntry.getMaterialNumber());
            plre.setMaterialUnitId(takeInventoryEntry.getMaterialUnitId());
            plre.setMaterialUnitName(takeInventoryEntry.getMaterialUnitName());
            plre.setSpecification(takeInventoryEntry.getSpecification());
            plre.setQuantity(takeInventoryEntry.getDifferenceQuantity());
            //将所有盘盈单据封装
            if(status.equals("B")){
                List<ProfitLossRecordEntry> pel = plr.getProfitLossRecordEntryList();
                pel.add(plre);
                plr.setProfitLossRecordEntryList(pel);
            }else if (status.equals("C")){
             //将所有盘亏单封装
                List<ProfitLossRecordEntry> pel = plrB.getProfitLossRecordEntryList();
                pel.add(plre);
                plrB.setProfitLossRecordEntryList(pel);
            }else{
                continue;
            }
        }
        if(plr.getProfitLossRecordEntryList().size()>0){
            insertProfitLossRecord(plr);
        }
        if(plrB.getProfitLossRecordEntryList().size()>0){
            insertProfitLossRecord(plrB);
        }
        //回写盘点单状态为已矫正
        takeInventory.setCorrectFlag("Y");
        int i = takeInventoryMapper.updateById(takeInventory);

        return AjaxResult.success();
    }

    //相同属性进行封装
    public ProfitLossRecord sameProfitLossRecordPackage(ProfitLossRecord plr ,TakeInventory takeInventory ){
        //如果编号为null,则自动为其生成编号
            SysCodeRule queryDomain  = new SysCodeRule();
            queryDomain.setBasicDomain("ProfitLossRecord");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(),true);
        plr.setNumber(number);
        plr.setKeeperId(takeInventory.getKeeperId());
        plr.setKeeperName(takeInventory.getKeeperName());
        plr.setName(takeInventory.getName());
        plr.setRecordTime(DateUtil.date());
        plr.setStatus("A");
        plr.setWarehouseId(takeInventory.getWarehouseId());
        plr.setWarehouseName(takeInventory.getWarehouseName());
        plr.setWarehouseNumber(takeInventory.getWarehouseNumber());
        plr.setCreateBy(SecurityUtils.getUsername());
        plr.setCreateTime(DateUtil.date());
        plr.setTakeInventoryId(takeInventory.getId());
        return plr;
    }

    /**
     * 新增盘点盈亏记录明细信息
     *
     * @param profitLossRecord 盘点盈亏记录对象
     */
    public void insertProfitLossRecordEntry(ProfitLossRecord profitLossRecord)
    {
        List<ProfitLossRecordEntry> profitLossRecordEntryList = profitLossRecord.getProfitLossRecordEntryList();
        Long id = profitLossRecord.getId();
        if (StringUtils.isNotNull(profitLossRecordEntryList))
        {
            List<ProfitLossRecordEntry> list = new ArrayList<ProfitLossRecordEntry>();
            for (ProfitLossRecordEntry profitLossRecordEntry : profitLossRecordEntryList)
            {
                profitLossRecordEntry.setMainId(id);
                list.add(profitLossRecordEntry);
            }
            if (list.size() > 0)
            {
                profitLossRecordEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }
}
