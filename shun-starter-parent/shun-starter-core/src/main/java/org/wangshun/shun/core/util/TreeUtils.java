package org.wangshun.shun.core.util;

import org.wangshun.shun.core.entity.ITreeEntity;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.CompareUtil;

public class TreeUtils {
    /**
     * 
     * 从集合转换为树结构
     */
    public static <T extends ITreeEntity<T>> List<T> converToTree(Collection<T> allEntity) {
        List<T> tree = CollUtil.newArrayList();
        Map<Long, T> allEntityMap = allEntity.parallelStream().collect(Collectors.toMap(T::getId, Function.identity()));
        allEntity.forEach(i -> {
            T parent = i.getParent();
            if (null == parent) {
                tree.add(allEntityMap.get(i.getId()));
            } else {
                allEntityMap.get(parent.getId()).getChildren().add(i);
            }
        });
        return tree;
    }

    /**
     * 
     * 获取所有子节点
     */
    private static <T extends ITreeEntity<T>> List<T> getAllChildren(Collection<T> entities) {
        List<T> allChildren = CollUtil.newArrayList();
        entities.stream().flatMap(i -> getAllChildren(i).stream()).collect(Collectors.toList());
        allChildren.addAll(entities);
        return allChildren;
    }

    /**
     * 
     * 获取所有子节点 不包括自己
     */

    public static <T extends ITreeEntity<T>> List<T> getAllChildren(T entity) {
        List<T> allChildren = CollUtil.newArrayList();
        allChildren.addAll(entity.getChildren());
        allChildren.addAll(getAllChildren(entity.getChildren()));
        return allChildren;
    }

    /**
     * 
     * 获取所有节点 包括自己
     */
    public static <T extends ITreeEntity<T>> List<T> getAllNode(Collection<T> entities) {
        List<T> allNode = entities.stream().flatMap(i -> getAllNode(i).stream()).collect(Collectors.toList());
        return allNode;
    }

    /**
     * 
     * 获取所有节点 包括自己
     */
    public static <T extends ITreeEntity<T>> List<T> getAllNode(T entity) {
        List<T> allNode = getAllNode(entity.getChildren());
        allNode.add(entity);
        return allNode;
    }

    /**
     * 
     * 填充子节点的level、sort、isLeaf属性
     */
    public static <T extends ITreeEntity<T>> void fill(T entity, int rootLevel) {
        entity.setLevel(rootLevel);
        if (null == entity.getIsLeaf()) {
            entity.setIsLeaf(0);
        }
        entity.getChildren().stream().forEach(e -> {
            fill(e, rootLevel + 1);
            e.setSort(getMaxSort(entity) + 1);
            e.setParent(entity);
            entity.setIsLeaf(0);
        });
    }

    /**
     * 
     * 填充子节点的level、sort、isLeaf属性
     */
    public static <T extends ITreeEntity<T>> void fill(List<T> entities, int rootLevel) {
        int sort = 0;
        for (T i : entities) {
            i.setSort(++sort);
            fill(i, rootLevel);
        }
    }

    private static <T extends ITreeEntity<T>> Integer getMaxSort(T entity) {
        return entity.getChildren().parallelStream().map(ITreeEntity::getSort).max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return CompareUtil.compare(o1, o2);
            }
        }).orElse(0);
    }
}
