package cn.sourceplan.warehouse.service.impl;

import java.util.Arrays;
import java.util.List;

import cn.hutool.core.collection.ListUtil;
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.warehouse.domain.TakeInventory;
import cn.sourceplan.warehouse.domain.TakeInventoryEntry;
import cn.sourceplan.warehouse.mapper.TakeInventoryEntryMapper;
import cn.sourceplan.warehouse.mapper.TakeInventoryMapper;
import cn.sourceplan.warehouse.service.ITakeInventoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;

import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存盘点Service业务层处理
 *
 * @author jinzhong
 * @date 2023-07-20
 */
@Service
public class TakeInventoryServiceImpl implements ITakeInventoryService
{
    @Autowired
    private TakeInventoryMapper takeInventoryMapper;

    @Autowired
    private TakeInventoryEntryMapper takeInventoryEntryMapper;

    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    /**
     * 查询库存盘点
     *
     * @param id 库存盘点主键
     * @return 库存盘点
     */
    @Override
    public TakeInventory selectTakeInventoryById(Long id)
    {
        TakeInventory takeInventory = takeInventoryMapper.selectTakeInventoryById(id);
        List<TakeInventoryEntry> takeInventoryEntryList = takeInventory.getTakeInventoryEntryList();
        //
        List<TakeInventoryEntry> materialNumber = ListUtil.sortByProperty(takeInventoryEntryList, "materialNumber");
        takeInventory.setTakeInventoryEntryList(materialNumber);
        return takeInventory;
    }

    /**
     * 查询库存盘点列表
     *
     * @param takeInventory 库存盘点
     * @return 库存盘点
     */
    @Override
    public List<TakeInventory> selectTakeInventoryList(TakeInventory takeInventory)
    {
        Map<String, Object> params = takeInventory.getParams();
        String beginTakeTime = (String)params.get("beginTakeTime");
        String endTakeTime = (String)params.get("endTakeTime");
        QueryWrapper<TakeInventory> qw = new QueryWrapper<>(takeInventory);
        if(beginTakeTime!=null&&endTakeTime!=null){
            qw.between("take_time",beginTakeTime,endTakeTime);
        }
        return takeInventoryMapper.selectList(qw);
    }

    /**
     * 新增库存盘点
     *
     * @param takeInventory 库存盘点
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertTakeInventory(TakeInventory takeInventory)
    {
        //如果编号为null,则自动为其生成编号
        if ( StringUtils.isBlank(takeInventory.getNumber()) ){
            SysCodeRule queryDomain  = new SysCodeRule();
            queryDomain.setBasicDomain("TakeInventory");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(),true);
            takeInventory.setNumber(number);
        }
        int rows = takeInventoryMapper.insert(takeInventory);
        insertTakeInventoryEntry(takeInventory);
        return rows;
    }

    /**
     * 修改库存盘点
     *
     * @param takeInventory 库存盘点
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateTakeInventory(TakeInventory takeInventory)
    {
        QueryWrapper<TakeInventoryEntry> qw = new QueryWrapper<>();
        qw.eq("main_id",takeInventory.getId());
        takeInventoryEntryMapper.delete(qw);
        insertTakeInventoryEntry(takeInventory);
        return takeInventoryMapper.updateById(takeInventory);
    }

    /**
     * 批量删除库存盘点
     *
     * @param ids 需要删除的库存盘点主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteTakeInventoryByIds(Long[] ids)
    {
        QueryWrapper<TakeInventoryEntry> qw = new QueryWrapper<>();
        qw.in("main_id",ids);
        takeInventoryEntryMapper.delete(qw);
        return takeInventoryMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除库存盘点信息
     *
     * @param id 库存盘点主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteTakeInventoryById(Long id)
    {
        QueryWrapper<TakeInventoryEntry> qw = new QueryWrapper<>();
        qw.eq("main_id",id);
        takeInventoryEntryMapper.delete(qw);
        return takeInventoryMapper.deleteById(id);
    }

    /**
     * 新增库存盘点明细信息
     *
     * @param takeInventory 库存盘点对象
     */
    public void insertTakeInventoryEntry(TakeInventory takeInventory)
    {
        List<TakeInventoryEntry> takeInventoryEntryList = takeInventory.getTakeInventoryEntryList();
        Long id = takeInventory.getId();
        if (StringUtils.isNotNull(takeInventoryEntryList))
        {
            List<TakeInventoryEntry> list = new ArrayList<TakeInventoryEntry>();
            for (TakeInventoryEntry takeInventoryEntry : takeInventoryEntryList)
            {
                takeInventoryEntry.setMainId(id);
                list.add(takeInventoryEntry);
            }
            if (list.size() > 0)
            {
                takeInventoryEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }
}
