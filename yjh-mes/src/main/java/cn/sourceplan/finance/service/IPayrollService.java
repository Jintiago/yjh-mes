package cn.sourceplan.finance.service;

import java.math.BigDecimal;
import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.finance.domain.Payroll;

import javax.servlet.http.HttpServletRequest;

/**
 * 工资单Service接口
 *
 * @author jinzhong
 * @date 2023-03-11
 */
public interface IPayrollService
{
    /**
     * 查询工资单
     *
     * @param id 工资单主键
     * @return 工资单
     */
    public Payroll selectPayrollById(Long id);

    /**
     * 查询工资单列表
     *
     * @param payroll 工资单
     * @return 工资单集合
     */
    public List<Payroll> selectPayrollList(Payroll payroll);

    /**
     * 新增工资单
     *
     * @param payroll 工资单
     * @return 结果
     */
    public int insertPayroll(Payroll payroll);

    /**
     * 修改工资单
     *
     * @param payroll 工资单
     * @return 结果
     */
    public int updatePayroll(Payroll payroll);

    /**
     * 批量删除工资单
     *
     * @param ids 需要删除的工资单主键集合
     * @return 结果
     */
    public int deletePayrollByIds(Long[] ids);

    /**
     * 删除工资单信息
     *
     * @param id 工资单主键
     * @return 结果
     */
    public int deletePayrollById(Long id);


    /**
     * 工资单预览
     *
     * @param ids 报工单ids
     * @return 结果
     */
    AjaxResult payRollPreview(Long[] ids);

    /**
     * 根据用户ID查汇总工资金额
     *
     * @param id 人员ID
     * @return 结果
     */
    BigDecimal sumByUserId(Long id, HttpServletRequest httpServletRequest);
}
