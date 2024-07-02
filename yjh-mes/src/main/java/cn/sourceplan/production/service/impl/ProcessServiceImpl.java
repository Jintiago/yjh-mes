package cn.sourceplan.production.service.impl;

import java.util.List;

import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.domain.SysFileInfo;
import cn.sourceplan.common.mapper.SysFileInfoMapper;
import cn.sourceplan.common.utils.SqlCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.production.mapper.ProcessMapper;
import cn.sourceplan.production.domain.Process;
import cn.sourceplan.production.service.IProcessService;

/**
 * 工序Service业务层处理
 *
 * @author jinzhong
 * @date 2022-09-22
 */
@Service
public class ProcessServiceImpl implements IProcessService
{
    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private SysFileInfoMapper sysFileInfoMapper;

    /**
     * 查询工序
     *
     * @param id 工序主键
     * @return 工序
     */
    @Override
    public Process selectProcessById(Long id)
    {
        return processMapper.selectProcessById(id);
    }

    /**
     * 查询工序列表
     *
     * @param process 工序
     * @return 工序
     */
    @Override
    public List<Process> selectProcessList(Process process)
    {
        return processMapper.selectProcessList(process);
    }

    /**
     * 新增工序
     *
     * @param process 工序
     * @return 结果
     */
    @Override
    public int insertProcess(Process process)
    {
        String sysFileInfoStr = getSysFileInfoStr(process.getSysFileInfoList());
        process.setGuidance(sysFileInfoStr);
        return processMapper.insert(process);
    }

    /**
     * 修改工序
     *
     * @param process 工序
     * @return 结果
     */
    @Override
    public AjaxResult updateProcess(Process process)
    {
        //校验数据引用关系
        //计件工资方案
        String pieceWagePlanCheck = SqlCheckUtil.sqlCheck("process_name","fm_piece_wage_plan",
                "where process_id =  "+process.getId()+"  and status ='A'");
        if(pieceWagePlanCheck!=null){
            return AjaxResult.error("工序["+pieceWagePlanCheck+"]在计件工资方案中已被引用，无法修改");
        }
        //工序路线子表
        String routeEntryCheck = SqlCheckUtil.sqlCheck("name",
                "pro_route_process prp left join pro_route pr on pr.id = prp.route_id ",
                "where prp.process_id =  "+process.getId()+"  and pr.status ='0'");
        if(routeEntryCheck!=null){
            return AjaxResult.error("该数据在工序路线["+ routeEntryCheck +"]中已被引用，无法修改");
        }
        String sysFileInfoStr = getSysFileInfoStr(process.getSysFileInfoList());
        process.setGuidance(sysFileInfoStr);
        int i = processMapper.updateById(process);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 批量删除工序
     *
     * @param ids 需要删除的工序主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteProcessByIds(Long[] ids)
    {
        String str = StringUtils.join(ids, ",");
        //校验数据引用关系
        //计件工资方案
        String pieceWagePlanCheck = SqlCheckUtil.sqlCheck("process_name","fm_piece_wage_plan",
                "where process_id in(  "+str+" ) and status ='A'");
        if(pieceWagePlanCheck!=null){
            return AjaxResult.error("所选数据在计件工资方案,工序["+ pieceWagePlanCheck +"]中已被引用，无法删除");
        }
        //工序路线子表
        String routeEntryCheck = SqlCheckUtil.sqlCheck("name",
                "pro_route_process prp left join pro_route pr on pr.id = prp.route_id ",
                "where prp.process_id in(  "+str+" ) and pr.status ='0'");
        if(routeEntryCheck!=null){
            return AjaxResult.error("所选数据在工序路线["+ routeEntryCheck +"]中已被引用，无法删除");
        }
        int i = processMapper.deleteProcessByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }


    private  String getSysFileInfoStr(List<SysFileInfo> sysFileInfoList){
        StringBuilder guidance = new StringBuilder() ;
        if(sysFileInfoList!=null&&sysFileInfoList.size()>0){
            for (int i = 0; i < sysFileInfoList.size(); i++) {
                SysFileInfo sysFileInfo = sysFileInfoList.get(i);
                int insert = sysFileInfoMapper.insert(sysFileInfo);
                if(insert<1){
                    continue;
                }
                guidance.append(sysFileInfo.getFileId());
                if(i!=sysFileInfoList.size()-1){
                    guidance.append(",");
                }
            }
        }
        return StringUtils.isBlank(guidance.toString())?null:guidance.toString();
    }
}
