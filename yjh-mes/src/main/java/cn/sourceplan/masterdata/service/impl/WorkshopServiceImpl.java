package cn.sourceplan.masterdata.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.sourceplan.common.core.domain.AjaxResult;
import cn.sourceplan.common.utils.SqlCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.sourceplan.masterdata.mapper.WorkshopMapper;
import cn.sourceplan.masterdata.domain.Workshop;
import cn.sourceplan.masterdata.service.IWorkshopService;

/**
 * 车间Service业务层处理
 *
 * @author jinzhong
 * @date 2022-09-21
 */
@Service
public class WorkshopServiceImpl implements IWorkshopService
{
    @Autowired
    private WorkshopMapper workshopMapper;

    /**
     * 查询车间
     *
     * @param id 车间主键
     * @return 车间
     */
    @Override
    public Workshop selectWorkshopById(Long id)
    {
        return workshopMapper.selectById(id);
    }

    /**
     * 查询车间列表
     *
     * @param workshop 车间
     * @return 车间
     */
    @Override
    public List<Workshop> selectWorkshopList(Workshop workshop)
    {
        QueryWrapper<Workshop> qw = new QueryWrapper<>(workshop);
        return workshopMapper.selectList(qw);
    }

    /**
     * 新增车间
     *
     * @param workshop 车间
     * @return 结果
     */
    @Override
    public AjaxResult insertWorkshop(Workshop workshop)
    {
        //编号不允许重复
        QueryWrapper<Workshop> qw = new QueryWrapper<>();
        qw.eq("number",workshop.getNumber());
        Integer count = workshopMapper.selectCount(qw);
        if(count>0){
            return AjaxResult.error(500,"该车间编号已存在，请核对");
        }
        return AjaxResult.success("操作成功"+workshopMapper.insert(workshop)+"条");
    }

    /**
     * 修改车间
     *
     * @param workshop 车间
     * @return 结果
     */
    @Override
    public AjaxResult updateWorkshop(Workshop workshop)
    {
        String result = SqlCheckUtil.sqlCheck("number","md_station",
                "where workshop_id = "+workshop.getId()+" and status ='0'");
        if(result!=null){
            return AjaxResult.error("该数据在工位["+ result +"]中已被引用，无法修改");
        }

        int i = workshopMapper.updateById(workshop);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 批量删除车间
     *
     * @param ids 需要删除的车间主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteWorkshopByIds(Long[] ids)
    {
        String result = SqlCheckUtil.sqlCheck("number","md_station",
                "where workshop_id in ("+ StringUtils.join(ids,",")+") and status ='0'");

        if(result!=null){
            return AjaxResult.error("所选数据在工位["+ result +"]中已被引用，无法删除");
        }

        int i = workshopMapper.deleteWorkshopByIds(ids);
        return i > 0 ? AjaxResult.success(i) : AjaxResult.error();
    }

    /**
     * 删除车间信息
     *
     * @param id 车间主键
     * @return 结果
     */
    @Override
    public int deleteWorkshopById(Long id)
    {
        return workshopMapper.deleteWorkshopById(id);
    }
}
