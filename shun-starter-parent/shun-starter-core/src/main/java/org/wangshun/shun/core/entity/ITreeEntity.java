package org.wangshun.shun.core.entity;

import java.io.Serializable;
import java.util.List;

public interface ITreeEntity<T extends ITreeEntity<T>> extends Serializable, Cloneable, Comparable<T> {

    Long getId();

    T setId(Long id);

    T getParent();

    T setParent(T parent);

    List<T> getChildren();

    T setChildren(List<T> children);

    Integer getLevel();

    T setLevel(Integer level);

    Integer getSort();

    T setSort(Integer sort);

    Integer getIsLeaf();

    T setIsLeaf(Integer isLeaf);

}
