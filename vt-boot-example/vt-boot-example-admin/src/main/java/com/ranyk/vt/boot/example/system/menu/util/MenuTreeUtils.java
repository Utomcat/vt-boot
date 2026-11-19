package com.ranyk.vt.boot.example.system.menu.util;

import cn.hutool.core.collection.CollUtil;
import com.ranyk.vt.boot.example.system.menu.domain.vo.MenuVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CLASS_NAME: MenuTreeUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 菜单树构建工具类
 * @date: 2026-11-20
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MenuTreeUtils {

    /**
     * 将平铺的菜单列表构建为树形结构
     * <p>
     * 入参列表需已按排序值排序, 构建结果会保持入参顺序; 出现环形父子关系时会中断该分支, 避免死循环。
     * </p>
     *
     * @param nodes        平铺的菜单视图对象列表
     * @param rootParentId 顶级菜单的父级 ID, 顶级菜单的 parentId 需与之一致
     * @return 树形菜单列表
     */
    public static List<MenuVO> build(List<MenuVO> nodes, Long rootParentId) {
        if (CollUtil.isEmpty(nodes) || rootParentId == null) {
            return new ArrayList<>();
        }
        Map<Long, List<MenuVO>> childrenMap = nodes.stream()
                .filter(node -> node.getParentId() != null)
                .collect(Collectors.groupingBy(MenuVO::getParentId));
        List<MenuVO> roots = childrenMap.getOrDefault(rootParentId, new ArrayList<>());
        for (MenuVO root : roots) {
            fillChildren(root, childrenMap, new HashSet<>());
        }
        return roots;
    }

    /**
     * 递归填充下级菜单
     *
     * @param node        当前菜单
     * @param childrenMap 父级 ID 到下级菜单列表的映射
     * @param visited     当前递归路径上已访问的菜单 ID, 用于防止环形引用
     */
    private static void fillChildren(MenuVO node, Map<Long, List<MenuVO>> childrenMap, Set<Long> visited) {
        if (node.getId() == null || !visited.add(node.getId())) {
            return;
        }
        List<MenuVO> children = childrenMap.get(node.getId());
        if (CollUtil.isNotEmpty(children)) {
            node.setChildren(children);
            for (MenuVO child : children) {
                fillChildren(child, childrenMap, visited);
            }
        }
    }
}
