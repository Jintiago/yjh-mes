package cn.sourceplan.barcode.mapper;

import java.util.Collection;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.barcode.domain.MaterialSn;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 物料SN码Mapper接口
 *
 * @author jinzhong
 * @date 2023-04-15
 */
@Repository
public interface MaterialSnMapper extends BaseMapper<MaterialSn>
{
    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<MaterialSn> entityList);


    /**
     * 批量更新
     * @param map 参数
     * @return 影响行数
     */
    Integer updateBatchSomeColumn(@Param("map") Map<String,Object> map);

}
