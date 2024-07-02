package cn.sourceplan.production.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.production.domain.Process;
import org.springframework.stereotype.Repository;

/**
 * 工序Mapper接口
 *
 * @author jinzhong
 * @date 2022-09-22
 */
@Repository
public interface ProcessMapper extends BaseMapper<Process>
{
    /**
     * 查询工序
     *
     * @param id 工序主键
     * @return 工序
     */
    Process selectProcessById(Long id);

    /**
     * 查询工序列表
     *
     * @param process 工序
     * @return 工序集合
     */
    List<Process> selectProcessList(Process process);


    /**
     * 删除工序
     *
     * @param id 工序主键
     * @return 结果
     */
    int deleteProcessById(Long id);

    /**
     * 批量删除工序
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteProcessByIds(Long[] ids);
}
