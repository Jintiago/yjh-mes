package cn.sourceplan.common.mybatisplus;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import cn.sourceplan.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 填充器
 *
 * @author nieqiurong 2018-08-10 22:59:23.
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createBy", () -> SecurityUtils.getUsername(), String.class);
        this.strictInsertFill(metaObject, "createTime", () -> DateUtil.date(System.currentTimeMillis()), Date.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateBy", () -> SecurityUtils.getUsername(), String.class);
        this.strictUpdateFill(metaObject, "updateTime", () -> DateUtil.date(System.currentTimeMillis()), Date.class);
    }
}
