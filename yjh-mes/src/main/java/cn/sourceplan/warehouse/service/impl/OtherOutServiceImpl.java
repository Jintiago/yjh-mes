package cn.sourceplan.warehouse.service.impl;

import java.util.Arrays;
import java.util.List;

import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;
import cn.sourceplan.common.utils.DateUtils;
import cn.sourceplan.warehouse.mapper.OtherOutEntryMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.warehouse.domain.OtherOutEntry;
import cn.sourceplan.warehouse.mapper.OtherOutMapper;
import cn.sourceplan.warehouse.domain.OtherOut;
import cn.sourceplan.warehouse.service.IOtherOutService;

/**
 * 其他出库单主表Service业务层处理
 *
 * @author jinzhong
 * @date 2023-09-28
 */
@Service
public class OtherOutServiceImpl implements IOtherOutService
{
    @Autowired
    private OtherOutMapper otherOutMapper;

    @Autowired
    private OtherOutEntryMapper otherOutEntryMapper;

    @Autowired
    private ISysCodeRuleService sysCodeRuleService;

    /**
     * 查询其他出库单主表
     *
     * @param id 其他出库单主表主键
     * @return 其他出库单主表
     */
    @Override
    public OtherOut selectOtherOutById(Long id)
    {
        return otherOutMapper.selectOtherOutById(id);
    }

    /**
     * 查询其他出库单主表列表
     *
     * @param otherOut 其他出库单主表
     * @return 其他出库单主表
     */
    @Override
    public List<OtherOut> selectOtherOutList(OtherOut otherOut)
    {
        return otherOutMapper.selectOtherOutList(otherOut);
    }

    /**
     * 新增其他出库单主表
     *
     * @param otherOut 其他出库单主表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOtherOut(OtherOut otherOut)
    {

        //如果编号为null,则自动为其生成编号
        if ( StringUtils.isBlank(otherOut.getNumber()) ){
            SysCodeRule queryDomain  = new SysCodeRule();
            queryDomain.setBasicDomain("OtherOut");
            List<SysCodeRule> sysCodeRules = sysCodeRuleService.selectSysCodeRuleList(queryDomain);
            String number = sysCodeRuleService.queryNewCodeById(sysCodeRules.get(0).getId(),true);
            otherOut.setNumber(number);
        }
        int rows = otherOutMapper.insert(otherOut);
        insertOtherOutEntry(otherOut);
        return rows;
    }

    /**
     * 修改其他出库单主表
     *
     * @param otherOut 其他出库单主表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOtherOut(OtherOut otherOut)
    {

        QueryWrapper<OtherOutEntry> qw = new QueryWrapper<>();
        qw.eq("main_id",otherOut.getId());
        int delete = otherOutEntryMapper.delete(qw);
        insertOtherOutEntry(otherOut);
        return otherOutMapper.updateById(otherOut);
    }

    /**
     * 批量删除其他出库单主表
     *
     * @param ids 需要删除的其他出库单主表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOtherOutByIds(Long[] ids)
    {
        QueryWrapper<OtherOutEntry> qw = new QueryWrapper<>();
        qw.in("main_id",ids);
        int delete = otherOutEntryMapper.delete(qw);
        return otherOutMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除其他出库单主表信息
     *
     * @param id 其他出库单主表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOtherOutById(Long id)
    {
        QueryWrapper<OtherOutEntry> qw = new QueryWrapper<>();
        qw.eq("main_id",id);
        int delete = otherOutEntryMapper.delete(qw);
        return otherOutMapper.deleteById(id);
    }

    /**
     * 新增其他出库单子表信息
     *
     * @param otherOut 其他出库单主表对象
     */
    public void insertOtherOutEntry(OtherOut otherOut)
    {
        List<OtherOutEntry> otherOutEntryList = otherOut.getOtherOutEntryList();
        Long id = otherOut.getId();
        if (StringUtils.isNotNull(otherOutEntryList))
        {
            List<OtherOutEntry> list = new ArrayList<OtherOutEntry>();
            for (OtherOutEntry otherOutEntry : otherOutEntryList)
            {
                otherOutEntry.setMainId(id);
                list.add(otherOutEntry);
            }
            if (list.size() > 0)
            {
                otherOutEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }
}
