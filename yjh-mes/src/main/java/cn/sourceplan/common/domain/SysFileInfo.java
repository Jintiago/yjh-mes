package cn.sourceplan.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.sourceplan.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * @author jinzhong
 */
@Data
@TableName("sys_file_info")
public class SysFileInfo  {

    @TableId(type= IdType.AUTO)
    private Long fileId ;
    private String filePath;
    private String fileName;
}
