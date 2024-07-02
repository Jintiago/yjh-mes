package cn.sourceplan.production.service;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.production.domain.Process;

/**
 * 工序Service接口
 *
 * @author jinzhong
 * @date 2022-09-22
 */
public interface IProcessService
{
    /**
     * 查询工序
     *
     * @param id 工序主键
     * @return 工序
     */
    public Process selectProcessById(Long id);

    /**
     * 查询工序列表
     *
     * @param process 工序
     * @return 工序集合
     */
    public List<Process> selectProcessList(Process process);

    /**
     * 新增工序
     *
     * @param process 工序
     * @return 结果
     */
    public int insertProcess(Process process);

    /**
     * 修改工序
     *
     * @param process 工序
     * @return 结果
     */
    public AjaxResult updateProcess(Process process);

    /**
     * 批量删除工序
     *
     * @param ids 需要删除的工序主键集合
     * @return 结果
     */
    public AjaxResult deleteProcessByIds(Long[] ids);

}
