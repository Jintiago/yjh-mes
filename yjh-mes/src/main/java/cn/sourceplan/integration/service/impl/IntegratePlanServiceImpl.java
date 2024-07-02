package cn.sourceplan.integration.service.impl;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.DateUtils;
import cn.sourceplan.common.utils.SqlCheckUtil;
import cn.sourceplan.common.utils.StringUtils;
import cn.sourceplan.integration.domain.IntePlanField;
import cn.sourceplan.integration.domain.IntegratePlan;
import cn.sourceplan.integration.mapper.IntegratePlanMapper;
import cn.sourceplan.integration.service.IIntegratePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 集成方案Service业务层处理
 *
 * @author 靳忠
 * @date 2023-02-13
 */
@Service
public class IntegratePlanServiceImpl implements IIntegratePlanService
{
    @Autowired
    private IntegratePlanMapper integratePlanMapper;

    /**
     * 查询集成方案
     *
     * @param id 集成方案主键
     * @return 集成方案
     */
    @Override
    public IntegratePlan selectIntegratePlanById(Long id)
    {
        return integratePlanMapper.selectIntegratePlanById(id);
    }

    /**
     * 查询集成方案列表
     *
     * @param integratePlan 集成方案
     * @return 集成方案
     */
    @Override
    public List<IntegratePlan> selectIntegratePlanList(IntegratePlan integratePlan)
    {
        return integratePlanMapper.selectIntegratePlanList(integratePlan);
    }

    /**
     * 新增集成方案
     *
     * @param integratePlan 集成方案
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult insertIntegratePlan(IntegratePlan integratePlan)
    {

        //如果用户没有手动填写编号
        if(StringUtils.isBlank(integratePlan.getNumber())){
            //由程序自动生成编号
            //获取编号并校验编号重复
            String number = getNumberCheck();
            if(number==null){
                return AjaxResult.error("编号生成失败");
            }
            integratePlan.setNumber(number);
        }else{
            //用户手填了编号
            //校验编号是否重复
            int b = SqlCheckUtil.sqlCheck("inte_plan", "where number = '" + integratePlan.getNumber()+"'");
            //校验失败
            if(b>0){
                return AjaxResult.error("已存在此编号");
            }
        }
        integratePlan.setCreateTime(DateUtils.getNowDate());
        int rows = integratePlanMapper.insertIntegratePlan(integratePlan);
        insertIntePlanField(integratePlan);
        return   AjaxResult.success(rows);
    }

    /**
     * 修改集成方案
     *
     * @param integratePlan 集成方案
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult updateIntegratePlan(IntegratePlan integratePlan)
    {

        //校验编号是否重复
        int b = SqlCheckUtil.sqlCheck("inte_plan", "where number = '" + integratePlan.getNumber()+"'");
        //校验失败
        if(b>1){
            return AjaxResult.error("已存在此编号");

        }
        integratePlan.setUpdateTime(DateUtils.getNowDate());
        integratePlanMapper.deleteIntePlanFieldByMainId(integratePlan.getId());
        insertIntePlanField(integratePlan);
        return  AjaxResult.success(integratePlanMapper.updateIntegratePlan(integratePlan)) ;
    }

    /**
     * 批量删除集成方案
     *
     * @param ids 需要删除的集成方案主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIntegratePlanByIds(Long[] ids)
    {
        integratePlanMapper.deleteIntePlanFieldByMainIds(ids);
        return integratePlanMapper.deleteIntegratePlanByIds(ids);
    }

    /**
     * 删除集成方案信息
     *
     * @param id 集成方案主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIntegratePlanById(Long id)
    {
        integratePlanMapper.deleteIntePlanFieldByMainId(id);
        return integratePlanMapper.deleteIntegratePlanById(id);
    }

    /**
     * 新增集成方案字段信息
     *
     * @param integratePlan 集成方案对象
     */
    public void insertIntePlanField(IntegratePlan integratePlan)
    {
        List<IntePlanField> intePlanFieldList = integratePlan.getIntePlanFieldList();
        Long id = integratePlan.getId();
        if (StringUtils.isNotNull(intePlanFieldList))
        {
            List<IntePlanField> list = new ArrayList<IntePlanField>();
            for (IntePlanField intePlanField : intePlanFieldList)
            {
                intePlanField.setMainId(id);
                list.add(intePlanField);
            }
            if (list.size() > 0)
            {
                integratePlanMapper.batchIntePlanField(list);
            }
        }
    }
    public String getNumberCheck(){
//        //由程序自动生成编号
//        String s= SelectRuleConfigUtils.getNumber("inte_plan");
//        if(s==null){
//            return null;
//        }
//        //校验编号是否重复
//        int b = SqlCheckUtil.sqlCheck("inte_plan", "where number = '" + s+"'");
//
//        if(b>0){
//            s=getNumberCheck();
//        }
        return null;
    }
}
