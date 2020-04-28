package org.wangshun.shun.db.base;

import org.wangshun.shun.db.base.entity.BaseDatabaseEntity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;

import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class PageCondition<T extends BaseDatabaseEntity<T>> implements IPage<T> {
    private static final long serialVersionUID = 1L;
    private static final String ASC = "asc";
    private static final String SEPARATOR = ",";
    private String asc;
    private String desc;
    private Map<String, String> orders = MapUtil.newHashMap(true);

    private long total;
    private long size;
    private long current;
    private T condition;

    private List<T> records;

    @Override
    public List<OrderItem> orders() {
        List<OrderItem> orderList = CollUtil.newLinkedList();
        if (StrUtil.isNotBlank(asc)) {
            orderList.addAll(OrderItem.ascs(StrUtil.split(asc, SEPARATOR)));
        }
        if (StrUtil.isNotBlank(desc)) {
            orderList.addAll(OrderItem.descs(StrUtil.split(desc, SEPARATOR)));
        }
        orders.forEach((column, asc) -> {
            orderList.add(new OrderItem().setColumn(column).setAsc(StrUtil.equals(ASC, asc)));
        });
        return orderList;
    }

}
