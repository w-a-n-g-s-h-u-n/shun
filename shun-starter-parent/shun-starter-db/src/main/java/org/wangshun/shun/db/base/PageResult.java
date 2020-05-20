package org.wangshun.shun.db.base;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageResult<T> {
    private Long current;
    private Long size;
    private Long total;
    private List<T> records = CollUtil.newLinkedList();
    @JsonIgnore
    private final JSONObject json = new JSONObject();

    public static <T> PageResult<T> empty(PageCondition<T> condition) {
        return new PageResult<T>().setCurrent(condition.getCurrent()).setSize(condition.getSize()).setTotal(0L);
    }

    public static <T> PageResult<T> empty(Page<T> page) {
        return new PageResult<T>().setCurrent(page.getCurrent()).setSize(page.getSize()).setTotal(page.getTotal()).setRecords(page.getRecords());
    }

    public PageResult<T> put(String key, Object value) {
        json.put(key, value);
        return this;
    }

    public JSONObject toJson() {
        if (current != null) {
            json.put("current", current);
        }
        if (size != null) {
            json.put("size", size);
        }
        if (total != null) {
            json.put("total", total);
        }
        if (records != null) {
            json.put("records", records);
        }
        return json;
    }
}
