package cn.sourceplan.warehouse.service.impl;

import java.util.HashMap;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import cn.sourceplan.barcode.mapper.MaterialSnMapper;
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.production.domain.Process;
import cn.sourceplan.production.domain.WorkOrderEntry;
import cn.sourceplan.production.mapper.ProcessMapper;
import cn.sourceplan.production.mapper.WorkOrderEntryMapper;
import cn.sourceplan.warehouse.domain.ProPick;
import cn.sourceplan.warehouse.domain.ProPickEntry;
import cn.sourceplan.warehouse.mapper.ProPickEntryMapper;
import cn.sourceplan.warehouse.mapper.ProPickMapper;
import cn.sourceplan.warehouse.service.IProPickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;

import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 生产领料单Service业务层处理
 *
 * @author jinzhong
 * @date 2023-02-23
 */
@Service
public class ProPickServiceImpl implements IProPickService
{
    @Autowired
    private ProPickMapper proPickMapper;

    @Autowired
    private ProPickEntryMapper proPickEntryMapper;

    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    @Autowired
    private MaterialSnMapper materialSnMapper;

    @Autowired
    private WorkOrderEntryMapper workOrderEntryMapper;

    @Autowired
    private ProcessMapper processMapper;
    /**
     * 查询生产领料单
     *
     * @param id 生产领料单主键
     * @return 生产领料单
     */
    @Override
    public ProPick selectProPickById(Long id)
    {
        return proPickMapper.selectProPickById(id);
    }

    /**
     * 查询生产领料单列表
     *
     * @param proPick 生产领料单
     * @return 生产领料单
     */
    @Override
    public List<ProPickEntry> selectProPickList(ProPick proPick)
    {
        List<ProPickEntry> proPicks = proPickMapper.selectProPickList(proPick);
        return proPicks;
    }

    /**
     * 新增生产领料单
     *
     * @param proPick 生产领料单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertProPick(ProPick proPick)
    {
        //如果编号为null,则自动为其生成编号
        if ( StringUtils.isBlank(proPick.getNumber()) ){
            SysCodeRule queryDomain  = new SysCodeRule();
            queryDomain.setBasicDomain("ProPick");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(),true);
            proPick.setNumber(number);
        }
        //获取生产工单子表ID
        Long workOrderEntryId = proPick.getWorkOrderEntryId();
        if(workOrderEntryId==null){
            //未经过生产领料的单子需要审核
            proPick.setStatus("B");
        }
        //天润项目定制化功能begin
            //所有半成品区域的领料，都免审核
        if(proPick.getWarehouseName().contains("半成品")){
            proPick.setStatus("A");
        }
        //天润项目定制化功能bend


        int rows = proPickMapper.insert(proPick);
        //更新SN码追溯表中的内容
        String snArray = proPick.getSnArray();
        if(StringUtils.isNotBlank(snArray)){
            int l = snArray.lastIndexOf(",");
            snArray = snArray.substring(0,l);
            String SnContent = DateUtil.now()+"： "+proPick.getPickUserName()+"-进行领料操作("+proPick.getNumber()+")<br/> ";
            Map<String,Object> map = new HashMap<>();
            map.put("content",SnContent);
            map.put("whereSql", " and sn_code in(" +snArray+")");
            materialSnMapper.updateBatchSomeColumn(map);
        }
        insertProPickEntry(proPick);
        return rows;
    }

    /**
     * 修改生产领料单
     *
     * @param proPick 生产领料单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateProPick(ProPick proPick)
    {
        proPickMapper.deleteProPickEntryByMainId(proPick.getId());
        insertProPickEntry(proPick);
        return proPickMapper.updateById(proPick);
    }

    /**
     * 批量删除生产领料单
     *
     * @param ids 需要删除的生产领料单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteProPickByIds(Long[] ids)
    {
        proPickMapper.deleteProPickEntryByMainIds(ids);
        return proPickMapper.deleteProPickByIds(ids);
    }

    /**
     * 删除生产领料单信息
     *
     * @param id 生产领料单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteProPickById(Long id)
    {
        proPickMapper.deleteProPickEntryByMainId(id);
        return proPickMapper.deleteById(id);
    }

    @Override
    public int batchCheck(Long[] ids, String checkStatus) {
        int l =0;

        for (int i = 0; i < ids.length; i++) {
            ProPick proPick = proPickMapper.selectById(ids[i]);
            if("A".equals(checkStatus)){
                //只有待审核状态的单据才能审核
                if("B".equals(proPick.getStatus())){
                    proPick.setStatus("A");
                    l+=proPickMapper.updateById(proPick);

                }
            }
        }
        return l;
    }

    /**
     * 新增生产领料单子表信息
     *
     * @param proPick 生产领料单对象
     */
    public void insertProPickEntry(ProPick proPick)
    {
        List<ProPickEntry> proPickEntryList = proPick.getProPickEntryList();
        Long id = proPick.getId();
        if (StringUtils.isNotNull(proPickEntryList))
        {
            List<ProPickEntry> list = new ArrayList<ProPickEntry>();
            for (ProPickEntry proPickEntry : proPickEntryList)
            {
                proPickEntry.setMainId(id);
                list.add(proPickEntry);
            }
            if (list.size() > 0)
            {
                proPickEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }
}
