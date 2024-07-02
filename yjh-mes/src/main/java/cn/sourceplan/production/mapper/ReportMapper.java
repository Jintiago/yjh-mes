package cn.sourceplan.production.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import cn.sourceplan.production.domain.Report;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 报工单Mapper接口
 *
 * @author jinzhong
 * @date 2023-02-15
 */
@Repository
public interface ReportMapper extends BaseMapper<Report>
{

    /**
     * 查询报工单列表
     *
     * @param report 查询实体
     * @return 报工单集合
     */
    public List<Report> selectReportList( Report report );

    /**
     * 批量删除报工单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteReportByIds(Long[] ids);


    /**
     * 查询报工单列表数量
     *
     * @param report 查询实体
     * @return 报工单集合
     */
    public int selectReportListCount( Report report );
}
