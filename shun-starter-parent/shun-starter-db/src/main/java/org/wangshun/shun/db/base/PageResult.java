package org.wangshun.shun.db.base;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private long current = 1;
    private long size = 10;
    private long total = 0;
    private List<T> records = CollUtil.newLinkedList();

    public static <T> PageResult<T> empty(PageCondition<T> condition) {
        return new PageResult<T>().setCurrent(condition.getCurrent()).setSize(condition.getSize());
    }

    public static <T> PageResult<T> empty(Page<T> page) {
        return new PageResult<T>().setCurrent(page.getCurrent()).setSize(page.getSize()).setTotal(page.getTotal()).setRecords(page.getRecords());
    }
}
