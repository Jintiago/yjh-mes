package cn.sourceplan.warehouse.service.impl;

import java.util.List;

import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.warehouse.domain.ProcureInto;
import cn.sourceplan.warehouse.domain.ProcureIntoEntry;
import cn.sourceplan.warehouse.mapper.ProcureIntoMapper;
import cn.sourceplan.warehouse.mapper.ProcureIntoEntryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.warehouse.service.IProcureIntoService;

/**
 * 采购入库单Service业务层处理
 *
 * @author jinzhong
 * @date 2023-06-26
 */
@Service
public class ProcureIntoServiceImpl implements IProcureIntoService
{
    @Autowired
    private ProcureIntoMapper procureIntoMapper;

    @Autowired
    private ProcureIntoEntryMapper procureIntoEntryMapper;

    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    /**
     * 查询采购入库单
     *
     * @param id 采购入库单主键
     * @return 采购入库单
     */
    @Override
    public ProcureInto selectProcureIntoById(Long id)
    {
        return procureIntoMapper.selectProcureIntoById(id);
    }

    /**
     * 查询采购入库单列表
     *
     * @param procureInto 采购入库单
     * @return 采购入库单
     */
    @Override
    public List<ProcureIntoEntry> selectProcureIntoList(ProcureInto procureInto)
    {
        return procureIntoMapper.selectProcureIntoList(procureInto);
    }

    /**
     * 新增采购入库单
     *
     * @param procureInto 采购入库单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertProcureInto(ProcureInto procureInto)
    {
        //如果编号为null,则自动为其生成编号
        if ( StringUtils.isBlank(procureInto.getNumber()) ){
            SysCodeRule queryDomain  = new SysCodeRule();
            queryDomain.setBasicDomain("ProcureInto");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(),true);
            procureInto.setNumber(number);
        }
        int rows = procureIntoMapper.insert(procureInto);
        insertProcureIntoEntry(procureInto);
        return rows;
    }

    /**
     * 修改采购入库单
     *
     * @param procureInto 采购入库单
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateProcureInto(ProcureInto procureInto)
    {
        procureIntoMapper.deleteProcureIntoEntryByMainId(procureInto.getId());
        insertProcureIntoEntry(procureInto);
        return procureIntoMapper.updateById(procureInto);
    }

    /**
     * 批量删除采购入库单
     *
     * @param ids 需要删除的采购入库单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteProcureIntoByIds(Long[] ids)
    {
        procureIntoMapper.deleteProcureIntoEntryByMainIds(ids);
        return procureIntoMapper.deleteProcureIntoByIds(ids);
    }

    /**
     * 删除采购入库单信息
     *
     * @param id 采购入库单主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteProcureIntoById(Long id)
    {
        procureIntoMapper.deleteProcureIntoEntryByMainId(id);
        return procureIntoMapper.deleteProcureIntoById(id);
    }

    /**
     * 新增采购入库单子表信息
     *
     * @param procureInto 采购入库单对象
     */
    public void insertProcureIntoEntry(ProcureInto procureInto)
    {
        List<ProcureIntoEntry> procureIntoEntryList = procureInto.getProcureIntoEntryList();
        Long id = procureInto.getId();
        if (StringUtils.isNotNull(procureIntoEntryList))
        {
            List<ProcureIntoEntry> list = new ArrayList<ProcureIntoEntry>();
            for (ProcureIntoEntry procureIntoEntry : procureIntoEntryList)
            {
                procureIntoEntry.setMainId(id);
                list.add(procureIntoEntry);
            }
            if (list.size() > 0)
            {
                procureIntoEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }
}
