package org.wangshun.shun.db.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.wangshun.shun.db.base.entity.BaseDatabaseEntity;

public abstract class BaseService<M extends BaseMapper<T>, T extends BaseDatabaseEntity<T>> extends ServiceImpl<M, T> {
    public boolean update(LambdaUpdateChainWrapper<T> updateWrapper, T entity, SFunction<T, ?>... columns) {
        for (SFunction<T, ?> column : columns) {
            updateWrapper.set(column, column.apply(entity));
        }
        return super.update(updateWrapper);
    }

}
