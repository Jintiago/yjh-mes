package cn.sourceplan.barcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.sourceplan.barcode.domain.BarcodeRecord;
import org.springframework.stereotype.Repository;

/**
 * 条码生成记录Mapper接口
 *
 * @author jinzhong
 * @date 2023-04-15
 */
@Repository
public interface BarcodeRecordMapper extends BaseMapper<BarcodeRecord>
{

}
