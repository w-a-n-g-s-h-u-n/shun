package org.wangshun.shun.core.util;

import org.wangshun.shun.core.entity.ITreeEntity;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;

public class TreeUtils {
    /**
     * 
     * 从集合转换为树结构
     */
    public static <T extends ITreeEntity<T>> List<T> converToTree(Collection<T> nodes) {
        List<T> tree = CollUtil.newLinkedList();
        Map<Long, T> allEntityMap = nodes.parallelStream().collect(Collectors.toMap(T::getId, Function.identity()));
        nodes.forEach(node -> {
            T parent = node.getParent();
            if (null == parent) {
                tree.add(allEntityMap.get(node.getId()));
            } else {
                allEntityMap.get(parent.getId()).getChildren().add(node);
            }
        });
        return tree;
    }

    /**
     * 
     * 获取所有子节点 不加上自己
     */
    private static <T extends ITreeEntity<T>> List<T> getAllChildren(Collection<T> nodes) {
        return nodes.parallelStream().flatMap(node -> getAllChildren(node).stream()).collect(Collectors.toList());
    }

    /**
     * 
     * 获取所有子节点 不加上自己
     */

    public static <T extends ITreeEntity<T>> List<T> getAllChildren(T node) {
        List<T> children = getAllChildren(node.getChildren());
        children.addAll(node.getChildren());
        return children;
    }

    /**
     * 
     * 获取所有子节点 加上自己
     */
    public static <T extends ITreeEntity<T>> List<T> getAllNode(Collection<T> nodes) {
        return nodes.parallelStream().flatMap(node -> getAllNode(node).stream()).collect(Collectors.toList());
    }

    /**
     * 
     * 获取所有子节点 加上自己
     */
    public static <T extends ITreeEntity<T>> List<T> getAllNode(T node) {
        List<T> allNode = getAllNode(node.getChildren());
        allNode.add(node);
        return allNode;
    }

    /**
     * 
     * 填充子节点的level、sort、isLeaf属性
     */
    public static <T extends ITreeEntity<T>> void fill(T node) {
        if (null == node.getIsLeaf()) {
            node.setIsLeaf(false);
        }
        node.getChildren().stream().forEach(e -> {
            fill(e);
            e.setSort(getMaxSort(node) + 1);
            e.setParent(node);
            node.setIsLeaf(false);
        });
    }

    /**
     * 
     * 填充子节点的level、sort、isLeaf属性
     */
    public static <T extends ITreeEntity<T>> void fill(List<T> nodes) {
        int sort = 0;
        for (T i : nodes) {
            i.setSort(++sort);
            fill(i);
        }
    }

    private static <T extends ITreeEntity<T>> Integer getMaxSort(T node) {
        return node.getChildren().parallelStream().map(ITreeEntity::getSort)
            .max(Comparator.nullsFirst(Comparator.naturalOrder()))
            .orElse(0);
    }
}
