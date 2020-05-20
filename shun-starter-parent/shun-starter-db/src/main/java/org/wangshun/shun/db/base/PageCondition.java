package org.wangshun.shun.db.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

@Data
public class PageCondition<T> {
    private static final String ASC = "asc";
    private static final String SEPARATOR = ",";
    private String asc;
    private String desc;
    private String orders;

    private long size = 10;
    private long current = 1;

    public List<OrderItem> orders() {
        List<OrderItem> orderList = CollUtil.newLinkedList();
        if (StrUtil.isNotBlank(asc)) {
            orderList.addAll(OrderItem.ascs(StrUtil.split(asc, SEPARATOR)));
        }
        if (StrUtil.isNotBlank(desc)) {
            orderList.addAll(OrderItem.descs(StrUtil.split(desc, SEPARATOR)));
        }
        if (StrUtil.isNotBlank(orders)) {
            JSONObject ordersJson = JSONArray.parseObject(orders);
            ordersJson.forEach((key, value) -> {
                orderList.add(new OrderItem().setColumn(key).setAsc(ASC.equals(value)));
            });
        }
        return orderList;
    }

    public IPage<T> toPage() {
        Page<T> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        page.setOrders(orders());
        return page;
    }
}
