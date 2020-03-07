package org.wangshun.shun.core.entity;

import java.util.List;

public interface ITreeEntity<T extends ITreeEntity<T>> extends IDatabaseEntity<T>, Comparable<T> {

    T getParent();

    T setParent(T parent);

    List<T> getChildren();

    T setChildren(List<T> children);

    Integer getLevel();

    T setLevel(Integer level);

    Integer getSort();

    T setSort(Integer sort);

    Boolean getIsLeaf();

    T setIsLeaf(Boolean isLeaf);

}
