package org.wangshun.shun.core.entity.impl;

import org.wangshun.shun.core.entity.ITreeEntity;

import java.util.List;

import cn.hutool.core.comparator.CompareUtil;

public class BaseTreeEntity<T extends BaseTreeEntity<T>> extends BaseDatabaseEntity<T> implements ITreeEntity<T> {
    protected static final long serialVersionUID = 1L;// serialVersionUID

    protected T parent;// 父节点
    protected List<T> children;// 子节点
    protected Integer level;// 级别
    protected Integer sort;// 排序
    protected Boolean isLeaf;// 是否叶子节点

    @Override
    public T getParent() {
        return parent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setParent(T parent) {
        this.parent = parent;
        return (T)this;
    }

    @Override
    public List<T> getChildren() {
        return children;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setChildren(List<T> children) {
        this.children = children;
        return (T)this;
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setLevel(Integer level) {
        this.level = level;
        return (T)this;
    }

    @Override
    public Integer getSort() {
        return sort;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setSort(Integer sort) {
        this.sort = sort;
        return (T)this;
    }

    @Override
    public Boolean getIsLeaf() {
        return isLeaf;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
        return (T)this;
    }

    @Override
    public int compareTo(T o) {
        int i;
        i = CompareUtil.compare(o.getParent(), o.getParent());
        if (i != 0)
            return i;
        i = CompareUtil.compare(o.getSort(), o.getSort());
        if (i != 0)
            return i;
        i = CompareUtil.compare(o.getIsLeaf(), o.getIsLeaf());
        if (i != 0)
            return i;
        i = CompareUtil.compare(o.getId(), o.getId());
        return i;
    }

}
