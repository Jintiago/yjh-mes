package cn.sourceplan.common.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.mapper.SysCodeRuleEntryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import cn.sourceplan.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import cn.sourceplan.common.domain.SysCodeRuleEntry;
import cn.sourceplan.common.mapper.SysCodeRuleMapper;
import cn.sourceplan.common.domain.SysCodeRule;
import cn.sourceplan.common.service.ISysCodeRuleService;


/**
 * 编码规则Service业务层处理
 *
 * @author jinzhong
 * @date 2023-01-17
 */
@Service
public class SysCodeRuleServiceImpl implements ISysCodeRuleService
{
    @Autowired
    private SysCodeRuleMapper sysCodeRuleMapper;

    @Autowired
    private SysCodeRuleEntryMapper sysCodeRuleEntryMapper;

    @Autowired
    private ApplicationContext applicationContext;
    /**
     * 查询编码规则
     *
     * @param id 编码规则主键
     * @return 编码规则
     */
    @Override
    public SysCodeRule selectSysCodeRuleById(Long id)
    {
        return sysCodeRuleMapper.selectSysCodeRuleById(id);
    }

    /**
     * 查询编码规则列表
     *
     * @param sysCodeRule 编码规则
     * @return 编码规则
     */
    @Override
    public List<SysCodeRule> selectSysCodeRuleList(SysCodeRule sysCodeRule)
    {
        QueryWrapper<SysCodeRule> qw = new QueryWrapper<>(sysCodeRule);
        return sysCodeRuleMapper.selectList(qw) ;
    }

    /**
     * 新增编码规则
     *
     * @param sysCodeRule 编码规则
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertSysCodeRule(SysCodeRule sysCodeRule)
    {
        int rows = sysCodeRuleMapper.insert(sysCodeRule);
        insertSysCodeRuleEntry(sysCodeRule);
        return rows;
    }

    /**
     * 修改编码规则
     *
     * @param sysCodeRule 编码规则
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateSysCodeRule(SysCodeRule sysCodeRule)
    {
        sysCodeRuleMapper.deleteSysCodeRuleEntryByRuleId(sysCodeRule.getId());
        insertSysCodeRuleEntry(sysCodeRule);
        return sysCodeRuleMapper.updateById(sysCodeRule);
    }

    /**
     * 批量删除编码规则
     *
     * @param ids 需要删除的编码规则主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteSysCodeRuleByIds(Long[] ids)
    {
        sysCodeRuleMapper.deleteSysCodeRuleEntryByRuleIds(ids);
        return sysCodeRuleMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public String queryNewCodeById(Long id, Boolean isUpdate,Long sourceValueId) {
        SysCodeRule codeRule = sysCodeRuleMapper.selectSysCodeRuleById(id);
        //将编码规则中的每行分录，拼接成一个字符
        List<SysCodeRuleEntry> subList = codeRule.getSysCodeRuleEntryList();
        StringBuilder newCodeStr = new StringBuilder();
        Long codeRuleId = codeRule.getId();
        //遍历编码规则所有子项
        for (int i = 0; i < subList.size(); i++) {
            SysCodeRuleEntry codeRuleSub =  subList.get(i);
            //类型ID
            String typeId = codeRuleSub.getTypeId();
            //子项ID
            Long subId = codeRuleSub.getId();
            //当前日期
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String today =format.format(date);
            //流水号
            if("A".equals(typeId)){
                int subSer =0;
//                缓存中没有日期，流水号用缓存的
//                缓存中有日期，不等于当前日期，流水号重置
//                缓存中有日期,等于当前日期，流水号用缓存的
//                缓存中没有流水号，流水号重置
//                其余情况用缓存中的流水号，流水号+1

                //缓存中有存最新日期，并且不等于当前日期
                 if(org.apache.commons.lang3.StringUtils.isNotBlank(codeRuleSub.getMaxDate())) {
                     String redisDate= codeRuleSub.getMaxDate();
                     if(!redisDate.equals(today)){
                         //不等于当前日期,流水号重置，日期也重置
                         codeRuleSub.setMaxDate(today);
                         //isUpdate=true,更新流水号
                         if(isUpdate){
                             codeRuleSub.setMaxFlow(2);
                         }
                         subSer=1;
                     }
                 }

                //通过缓存获取当前最新流水号
                if(codeRuleSub.getMaxFlow()==null||codeRuleSub.getMaxFlow()<2){
                    //不存在,流水号重置
                    //isUpdate=true,更新流水号
                    if(isUpdate){
                        codeRuleSub.setMaxFlow(2);
                    }
                    subSer=1;
                }
                if(subSer==0){
                    //其他情况下用缓存中的流水号,并将流水号+1
                    subSer=  codeRuleSub.getMaxFlow();
                    if(isUpdate){
                        codeRuleSub.setMaxFlow(subSer+1);
                    }

                }
                //补位符
                String codeCover = codeRuleSub.getCodeCover();
                //长度
                Integer codeLength = codeRuleSub.getLengthFlow();
                String serialStr = getSerial(codeCover, Long.valueOf(codeLength), String.valueOf(subSer));
                newCodeStr.append(serialStr);
            }
            //常量
            if("B".equals(typeId)){
                newCodeStr.append(codeRuleSub.getConstantChar()==null?"":codeRuleSub.getConstantChar());
            }
            //日期
            if("C".equals(typeId)){
                String dateFormat = codeRuleSub.getDateFormat();
                format = new SimpleDateFormat(dateFormat);
                codeRuleSub.setMaxDate(today);
                String str = format.format(date);
                newCodeStr.append(str);
            }
            //基础资料
            if("D".equals(typeId)){
                //3种方案，1.通过类名反射拿到对象(用这个) 2.通过DyMethodUtil工具类直接执行String语句 3.每个基础资料都单独调用
                codeRuleSub.setSourceValue(sourceValueId);
                String basicValue = getBasicValue(codeRuleSub);
                newCodeStr.append(basicValue);
            }
            sysCodeRuleEntryMapper.updateById(codeRuleSub);
        }
        return newCodeStr.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String queryNewCodeById(Long id, Boolean isUpdate) {
        SysCodeRule codeRule = sysCodeRuleMapper.selectSysCodeRuleById(id);
        //将编码规则中的每行分录，拼接成一个字符
        List<SysCodeRuleEntry> subList = codeRule.getSysCodeRuleEntryList();
        StringBuilder newCodeStr = new StringBuilder();
        Long codeRuleId = codeRule.getId();
        //遍历编码规则所有子项
        for (int i = 0; i < subList.size(); i++) {
            SysCodeRuleEntry codeRuleSub =  subList.get(i);
            //类型ID
            String typeId = codeRuleSub.getTypeId();
            //子项ID
            Long subId = codeRuleSub.getId();
            //当前日期
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String today =format.format(date);
            //流水号
            if("A".equals(typeId)){
                int subSer =0;
//                缓存中没有日期，流水号用缓存的
//                缓存中有日期，不等于当前日期，流水号重置
//                缓存中有日期,等于当前日期，流水号用缓存的
//                缓存中没有流水号，流水号重置
//                其余情况用缓存中的流水号，流水号+1

                //缓存中有存最新日期，并且不等于当前日期
                if(org.apache.commons.lang3.StringUtils.isNotBlank(codeRuleSub.getMaxDate())) {
                    String redisDate= codeRuleSub.getMaxDate();
                    if(!redisDate.equals(today)){
                        //不等于当前日期,流水号重置，日期也重置
                        codeRuleSub.setMaxDate(today);
                        //isUpdate=true,更新流水号
                        if(isUpdate){
                            codeRuleSub.setMaxFlow(2);
                        }
                        subSer=1;
                    }
                }

                //通过缓存获取当前最新流水号
                if(codeRuleSub.getMaxFlow()==null||codeRuleSub.getMaxFlow()<2){
                    //不存在,流水号重置
                    //isUpdate=true,更新流水号
                    if(isUpdate){
                        codeRuleSub.setMaxFlow(2);
                    }
                    subSer=1;
                }
                if(subSer==0){
                    //其他情况下用缓存中的流水号,并将流水号+1
                    subSer=  codeRuleSub.getMaxFlow();
                    if(isUpdate){
                        codeRuleSub.setMaxFlow(subSer+1);
                    }

                }
                //补位符
                String codeCover = codeRuleSub.getCodeCover();
                //长度
                Integer codeLength = codeRuleSub.getLengthFlow();
                String serialStr = getSerial(codeCover, Long.valueOf(codeLength), String.valueOf(subSer));
                newCodeStr.append(serialStr);
            }
            //常量
            if("B".equals(typeId)){
                newCodeStr.append(codeRuleSub.getConstantChar()==null?"":codeRuleSub.getConstantChar());
            }
            //日期
            if("C".equals(typeId)){
                String dateFormat = codeRuleSub.getDateFormat();
                format = new SimpleDateFormat(dateFormat);
                codeRuleSub.setMaxDate(today);
                String str = format.format(date);
                newCodeStr.append(str);
            }
            sysCodeRuleEntryMapper.updateById(codeRuleSub);
        }
        return newCodeStr.toString();
    }

    /**
     * 新增编码规则子信息
     *
     * @param sysCodeRule 编码规则对象
     */
    public void insertSysCodeRuleEntry(SysCodeRule sysCodeRule)
    {
        List<SysCodeRuleEntry> sysCodeRuleEntryList = sysCodeRule.getSysCodeRuleEntryList();
        Long id = sysCodeRule.getId();
        if (StringUtils.isNotNull(sysCodeRuleEntryList))
        {
            List<SysCodeRuleEntry> list = new ArrayList<SysCodeRuleEntry>();
            for (SysCodeRuleEntry sysCodeRuleEntry : sysCodeRuleEntryList)
            {
                sysCodeRuleEntry.setRuleId(id);
                list.add(sysCodeRuleEntry);
            }
            if (list.size() > 0)
            {
                sysCodeRuleEntryMapper.insertBatchSomeColumn(list);
            }
        }
    }

    public String getBasicValue(SysCodeRuleEntry codeRuleSub){
        //获取实体/表名 ，这里举例用产品的基础资料  elementSourceTable 为”Material“
        String elementSourceTable = codeRuleSub.getElementSourceTable();
        //获取Service类
        //获取来源值，例如来源实体为material产品，来源值就是具体的哪个产品的id
        Long id = Long.valueOf(codeRuleSub.getSourceValue());
        //获取来源属性，就是具体的字段，例如编号 sourceAttribute为"Number"
        String sourceAttribute = codeRuleSub.getSourceAttribute();
        //根据实体名material 获取该实体对应的service方法名称
        String serviceName = elementSourceTable.substring(0,1).toLowerCase()+elementSourceTable.substring(1)+"ServiceImpl";
        //获取service方法的Object对象
        Object serviceObj = applicationContext.getBean(serviceName);
        try {
            //获取Material实体Class
            Class<?> domainClass = Class.forName("cn.sourceplan.masterdata.domain."+elementSourceTable);
            //实例化
            Object domainObj=domainClass.newInstance();
            //获取Material的 setId方法
            Method setId = domainClass.getDeclaredMethod("setId", Long.class);
            //给id赋值
            setId.invoke(domainObj,id);
            //在service类中找到selectMaterialById方法
            Method selectNbyId = serviceObj.getClass().getDeclaredMethod("select" + elementSourceTable + "ById", Long.class);
            //放入已经赋值id的Material对象，并执行，获得Material结果Object
            Object resultObj = selectNbyId.invoke(serviceObj,id);
            //调用material中的get***方法，获取相应的值，比如sourceAttribute为 "Number",
            Method getAttribute = resultObj.getClass().getMethod("get" + sourceAttribute.substring(0,1).toUpperCase()+sourceAttribute.substring(1));
            //执行getNumber方法，将值转为String返回
            Object attribute = getAttribute.invoke(resultObj,new Object[0]);
            return String.valueOf(attribute);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSerial(String codeCover,Long codeLength,String subSer){
        StringBuilder serial = new StringBuilder();
        //获取末尾流水号的长度
        int subSerL = subSer.length();
        //需要补的位数
        int coverL = codeLength - subSerL<0?0:Math.toIntExact(codeLength - subSerL);
        int codeCoverL = codeCover.length();
        //拼接补位符
        for (int i = 0; i < coverL/codeCoverL; i++) {
            serial.append(codeCover);
        }
        //取余
        serial.append(codeCover, 0, coverL%codeCoverL);
        serial.append(subSer);
        return serial.toString();
    }

}
