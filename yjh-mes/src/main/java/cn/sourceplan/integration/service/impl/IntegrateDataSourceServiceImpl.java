package cn.sourceplan.integration.service.impl;


import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.DateUtils;
import cn.sourceplan.common.utils.SqlCheckUtil;
import cn.sourceplan.common.utils.StringUtils;
import cn.sourceplan.integration.domain.InteDataSourceField;
import cn.sourceplan.integration.domain.IntegrateDataSource;
import cn.sourceplan.integration.mapper.IntegrateDataSourceMapper;
import cn.sourceplan.integration.service.IIntegrateDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 集成数据源Service业务层处理
 *
 * @author 靳忠
 * @date 2023-02-11
 */
@Service
public class IntegrateDataSourceServiceImpl implements IIntegrateDataSourceService
{
    @Autowired
    private IntegrateDataSourceMapper integrateDataSourceMapper;

    /**
     * 查询集成数据源
     *
     * @param id 集成数据源主键
     * @return 集成数据源
     */
    @Override
    public IntegrateDataSource selectIntegrateDataSourceById(Long id)
    {
        return integrateDataSourceMapper.selectIntegrateDataSourceById(id);
    }

    /**
     * 查询集成数据源列表
     *
     * @param integrateDataSource 集成数据源
     * @return 集成数据源
     */
    @Override
    public List<IntegrateDataSource> selectIntegrateDataSourceList(IntegrateDataSource integrateDataSource)
    {
        return integrateDataSourceMapper.selectIntegrateDataSourceList(integrateDataSource);
    }

    /**
     * 新增集成数据源
     *
     * @param integrateDataSource 集成数据源
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult insertIntegrateDataSource(IntegrateDataSource integrateDataSource)
    {

        //如果用户没有手动填写编号
        if(StringUtils.isBlank(integrateDataSource.getNumber())){
            //由程序自动生成编号
            //获取编号并校验编号重复
            String number = getNumberCheck();
            if(number==null){
                return AjaxResult.error("编号生成失败");
            }
            integrateDataSource.setNumber(number);
        }else{
            //用户手填了编号
            //校验编号是否重复
            int b = SqlCheckUtil.sqlCheck("inte_data_source", "where number = '" + integrateDataSource.getNumber()+"'");
            //校验失败
            if(b>0){
                return AjaxResult.error("已存在此编号");
            }
        }

        integrateDataSource.setCreateTime(DateUtils.getNowDate());
        int rows = integrateDataSourceMapper.insertIntegrateDataSource(integrateDataSource);
        insertInteDataSourceField(integrateDataSource);
        return AjaxResult.success(rows) ;
    }

    /**
     * 修改集成数据源
     *
     * @param integrateDataSource 集成数据源
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult updateIntegrateDataSource(IntegrateDataSource integrateDataSource)
    {

        //校验编号是否重复
        int b = SqlCheckUtil.sqlCheck("inte_data_source", "where number = '" + integrateDataSource.getNumber()+"'");
        //校验失败
        if(b>1){
            return AjaxResult.error("已存在此编号");

        }
        integrateDataSource.setUpdateTime(DateUtils.getNowDate());
        integrateDataSourceMapper.deleteInteDataSourceFieldByMainId(integrateDataSource.getId());
        insertInteDataSourceField(integrateDataSource);
        return   AjaxResult.success(integrateDataSourceMapper.updateIntegrateDataSource(integrateDataSource)) ;
    }

    /**
     * 批量删除集成数据源
     *
     * @param ids 需要删除的集成数据源主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIntegrateDataSourceByIds(Long[] ids)
    {
        integrateDataSourceMapper.deleteInteDataSourceFieldByMainIds(ids);
        return integrateDataSourceMapper.deleteIntegrateDataSourceByIds(ids);
    }

    /**
     * 删除集成数据源信息
     *
     * @param id 集成数据源主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIntegrateDataSourceById(Long id)
    {
        integrateDataSourceMapper.deleteInteDataSourceFieldByMainId(id);
        return integrateDataSourceMapper.deleteIntegrateDataSourceById(id);
    }

    /**
     * 新增数据源字段信息
     *
     * @param integrateDataSource 集成数据源对象
     */
    public void insertInteDataSourceField(IntegrateDataSource integrateDataSource)
    {
        List<InteDataSourceField> inteDataSourceFieldList = integrateDataSource.getInteDataSourceFieldList();
        Long id = integrateDataSource.getId();
        if (StringUtils.isNotNull(inteDataSourceFieldList))
        {
            List<InteDataSourceField> list = new ArrayList<InteDataSourceField>();
            for (InteDataSourceField inteDataSourceField : inteDataSourceFieldList)
            {
                inteDataSourceField.setMainId(id);
                list.add(inteDataSourceField);
            }
            if (list.size() > 0)
            {
                integrateDataSourceMapper.batchInteDataSourceField(list);
            }
        }
    }

    public String getNumberCheck(){
//        //由程序自动生成编号
//        String s= SelectRuleConfigUtils.getNumber("inte_data_source");
//        if(s==null){
//            return null;
//        }
//        //校验编号是否重复
//        int b = SqlCheckUtil.sqlCheck("inte_data_source", "where number = '" + s+"'");
//
//        if(b>0){
//            s=getNumberCheck();
//        }
        return null;
    }
}
