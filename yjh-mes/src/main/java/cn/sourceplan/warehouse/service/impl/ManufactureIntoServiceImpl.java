package cn.sourceplan.warehouse.service.impl;

import java.util.HashMap;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import cn.sourceplan.barcode.mapper.MaterialSnMapper;
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.warehouse.domain.ManufactureInto;
import cn.sourceplan.warehouse.domain.ManufactureIntoEntry;
import cn.sourceplan.warehouse.mapper.ManufactureIntoEntryMapper;
import cn.sourceplan.warehouse.mapper.ManufactureIntoMapper;
import cn.sourceplan.warehouse.service.IManufactureIntoService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;

import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 完工入库单Service业务层处理
 *
 * @author jinzhong
 * @date 2022-11-10
 */
@Service
public class ManufactureIntoServiceImpl implements IManufactureIntoService
{
    @Autowired
    private ManufactureIntoMapper manufactureIntoMapper;

    @Autowired
    private ManufactureIntoEntryMapper manufactureIntoEntryMapper;
    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    @Autowired
    private MaterialSnMapper materialSnMapper;

    /**
     * 查询完工入库单
     *
     * @param id 完工入库单主键
     * @return 完工入库单
     */
    @Override
    public ManufactureInto selectManufactureIntoById(Long id)
    {
        return manufactureIntoMapper.selectManufactureIntoById(id);
    }

    /**
     * 查询完工入库单列表
     *
     * @param manufactureInto 完工入库单
     * @return 完工入库单
     */
    @Override
    public List<ManufactureInto> selectManufactureIntoList(ManufactureInto manufactureInto)
    {
        return manufactureIntoMapper.selectManufactureIntoList(manufactureInto);
    }

    /**
     * 新增完工入库单
     *
     * @param manufactureInto 完工入库单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertManufactureInto(ManufactureInto manufactureInto)
    {

        //如果编号为null,则自动为其生成编号
        if ( StringUtils.isBlank(manufactureInto.getNumber()) ){
            SysCodeRule queryDomain  = new SysCodeRule();
            queryDomain.setBasicDomain("ManufactureInto");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(),true);
            manufactureInto.setNumber(number);
        }
        int rows = manufactureIntoMapper.insert(manufactureInto);
        //更新SN码追溯表中的内容
        String snArray = manufactureInto.getSnArray();
        if(StringUtils.isNotBlank(snArray)){
            int l = snArray.lastIndexOf(",");
            snArray = snArray.substring(0,l);
            String SnContent = DateUtil.now()+"： "+manufactureInto.getDelivererName()+"-进行入库操作("+manufactureInto.getNumber()+")<br/> ";
            Map<String,Object> map = new HashMap<>();
            map.put("content",SnContent);
            map.put("whereSql", " and sn_code in(" +snArray+")");
            materialSnMapper.updateBatchSomeColumn(map);
        }
        insertManufactureIntoEntry(manufactureInto);
        return rows;
    }

    /**
     * 修改完工入库单
     *
     * @param manufactureInto 完工入库单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateManufactureInto(ManufactureInto manufactureInto)
    {
        manufactureIntoMapper.deleteManufactureIntoEntryByMainId(manufactureInto.getId());
        insertManufactureIntoEntry(manufactureInto);
        return manufactureIntoMapper.updateById(manufactureInto);
    }

    /**
     * 批量删除完工入库单
     *
     * @param ids 需要删除的完工入库单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteManufactureIntoByIds(Long[] ids)
    {
        manufactureIntoMapper.deleteManufactureIntoEntryByMainIds(ids);
        return manufactureIntoMapper.deleteManufactureIntoByIds(ids);
    }

    /**
     * 删除完工入库单信息
     *
     * @param id 完工入库单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteManufactureIntoById(Long id)
    {
        manufactureIntoMapper.deleteManufactureIntoEntryByMainId(id);
        return manufactureIntoMapper.deleteManufactureIntoById(id);
    }

    /**
     * 新增生产入库单子表信息
     *
     * @param manufactureInto 完工入库单对象
     */
    public void insertManufactureIntoEntry(ManufactureInto manufactureInto)
    {

        List<ManufactureIntoEntry> manufactureIntoEntryList = manufactureInto.getManufactureIntoEntryList();
        Long id = manufactureInto.getId();
        if (StringUtils.isNotNull(manufactureIntoEntryList))
        {
            List<ManufactureIntoEntry> list = new ArrayList<ManufactureIntoEntry>();
            for (ManufactureIntoEntry manufactureIntoEntry : manufactureIntoEntryList)
            {
                JSONObject extendFieldJson = manufactureIntoEntry.getExtendFieldJson();
                if(extendFieldJson==null){
                    extendFieldJson=JSONObject.parseObject("{}");
                }
                manufactureIntoEntry.setExtendField(extendFieldJson.toJSONString());
                manufactureIntoEntry.setMainId(id);
                list.add(manufactureIntoEntry);
            }
            if (list.size() > 0)
            {
                manufactureIntoEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }
}
