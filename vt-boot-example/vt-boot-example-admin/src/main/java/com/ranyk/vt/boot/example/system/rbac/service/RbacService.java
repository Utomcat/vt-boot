package com.ranyk.vt.boot.example.system.rbac.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ranyk.vt.boot.example.constant.MenuTypeEnum;
import com.ranyk.vt.boot.example.constant.RbacConstant;
import com.ranyk.vt.boot.example.constant.RoleCodeEnum;
import com.ranyk.vt.boot.example.system.menu.domain.entity.SysMenu;
import com.ranyk.vt.boot.example.system.menu.domain.vo.MenuVO;
import com.ranyk.vt.boot.example.system.menu.mapper.SysMenuMapper;
import com.ranyk.vt.boot.example.system.menu.util.MenuTreeUtils;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysRole;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysRoleMenu;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysUserRole;
import com.ranyk.vt.boot.example.system.role.mapper.SysRoleMapper;
import com.ranyk.vt.boot.example.system.role.mapper.SysRoleMenuMapper;
import com.ranyk.vt.boot.example.system.role.mapper.SysUserRoleMapper;
import com.ranyk.vt.boot.example.system.user.domain.entity.SysUser;
import com.ranyk.vt.boot.example.system.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * CLASS_NAME: RbacService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: RBAC 权限解析服务, 负责按用户解析角色编码、权限码与可见菜单树, 并提供本地缓存与失效能力
 * <p>
 * 角色来源以 t_user_role 为准; 当用户没有任何角色绑定时, 回退到 t_user.role 字段, 以兼容存量数据。
 * 超级管理员角色(ADMIN)在代码层直接视为拥有通配权限 {@code *}, 无需逐条配置按钮权限。
 * </p>
 * @date: 2026-11-20
 */
@Service
@RequiredArgsConstructor
public class RbacService {

    /**
     * 角色数据访问对象
     */
    private final SysRoleMapper sysRoleMapper;
    /**
     * 用户-角色关联数据访问对象
     */
    private final SysUserRoleMapper sysUserRoleMapper;
    /**
     * 角色-菜单关联数据访问对象
     */
    private final SysRoleMenuMapper sysRoleMenuMapper;
    /**
     * 菜单数据访问对象
     */
    private final SysMenuMapper sysMenuMapper;
    /**
     * 用户数据访问对象, 用于兼容 t_user.role 角色回退
     */
    private final SysUserMapper sysUserMapper;

    /**
     * 用户权限快照本地缓存
     * <p>
     * 仅在当前实例内生效, 角色/菜单变更时通过 {@link #evictAll()} 主动失效; 多实例部署时最长存在
     * {@link RbacConstant#CACHE_TTL_SECONDS} 秒的延迟。
     * </p>
     */
    private final Map<Long, CachedSnapshot> snapshotCache = new ConcurrentHashMap<>();

    /**
     * 查询用户拥有的角色编码集合
     *
     * @param userId 用户ID
     * @return 角色编码集合
     */
    public List<String> roleCodes(Long userId) {
        return loadSnapshot(userId).roles();
    }

    /**
     * 查询用户拥有的权限码集合; 超级管理员返回通配权限 {@code *}
     *
     * @param userId 用户ID
     * @return 权限码集合
     */
    public List<String> permissions(Long userId) {
        return loadSnapshot(userId).permissions();
    }

    /**
     * 查询用户可访问的菜单树(仅目录与页面, 不含按钮)
     *
     * @param userId 用户ID
     * @return 树形菜单列表
     */
    public List<MenuVO> menuTree(Long userId) {
        return loadSnapshot(userId).menus();
    }

    /**
     * 失效指定用户的权限缓存
     *
     * @param userId 用户ID
     */
    public void evict(Long userId) {
        if (ObjectUtil.isNotNull(userId)) {
            snapshotCache.remove(userId);
        }
    }

    /**
     * 失效全部用户的权限缓存
     */
    public void evictAll() {
        snapshotCache.clear();
    }

    /**
     * 加载用户权限快照, 命中缓存且未过期时直接返回
     *
     * @param userId 用户ID
     * @return 权限快照 {@link Snapshot}
     */
    private Snapshot loadSnapshot(Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return Snapshot.empty();
        }
        CachedSnapshot cached = snapshotCache.get(userId);
        if (ObjectUtil.isNotNull(cached) && cached.expireAt().isAfter(LocalDateTime.now())) {
            return cached.snapshot();
        }
        Snapshot snapshot = buildSnapshot(userId);
        snapshotCache.put(userId, new CachedSnapshot(snapshot,
                LocalDateTime.now().plusSeconds(RbacConstant.CACHE_TTL_SECONDS)));
        return snapshot;
    }

    /**
     * 从数据库构建用户权限快照
     *
     * @param userId 用户ID
     * @return 权限快照 {@link Snapshot}
     */
    private Snapshot buildSnapshot(Long userId) {
        List<String> roles = resolveRoleCodes(userId);
        if (roles.contains(RoleCodeEnum.ADMIN.getValue())) {
            return new Snapshot(roles, List.of(RbacConstant.SUPER_PERMISSION), buildSuperAdminMenus());
        }
        Set<Long> menuIds = resolveGrantedMenuIds(roles);
        List<SysMenu> grantedMenus = selectMenusByIds(menuIds);
        List<String> permissions = grantedMenus.stream()
                .map(SysMenu::getPermission)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        return new Snapshot(roles, permissions, buildVisibleMenuTree(grantedMenus));
    }

    /**
     * 解析用户的角色编码, 优先取 t_user_role, 为空时回退到 t_user.role
     *
     * @param userId 用户ID
     * @return 角色编码集合
     */
    private List<String> resolveRoleCodes(Long userId) {
        List<Long> roleIds = sysUserRoleMapper.selectList(
                        Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId))
                .stream()
                .map(SysUserRole::getRoleId)
                .filter(ObjectUtil::isNotNull)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(roleIds)) {
            List<SysRole> roles = sysRoleMapper.selectByIds(roleIds).stream()
                    .filter(role -> ObjectUtil.equal(RbacConstant.STATUS_NORMAL, role.getStatus()))
                    .sorted(Comparator.comparing(SysRole::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(roles)) {
                return roles.stream().map(SysRole::getCode).distinct().collect(Collectors.toList());
            }
        }
        SysUser user = sysUserMapper.selectById(userId);
        if (ObjectUtil.isNull(user) || StrUtil.isBlank(user.getRole())) {
            return new ArrayList<>();
        }
        return List.of(user.getRole());
    }

    /**
     * 汇总角色已授权的菜单ID
     *
     * @param roles 角色编码集合
     * @return 菜单ID集合
     */
    private Set<Long> resolveGrantedMenuIds(List<String> roles) {
        if (CollUtil.isEmpty(roles)) {
            return new LinkedHashSet<>();
        }
        List<Long> roleIds = sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
                        .in(SysRole::getCode, roles)
                        .eq(SysRole::getStatus, RbacConstant.STATUS_NORMAL))
                .stream()
                .map(SysRole::getId)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(roleIds)) {
            return new LinkedHashSet<>();
        }
        return sysRoleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
                        .in(SysRoleMenu::getRoleId, roleIds))
                .stream()
                .map(SysRoleMenu::getMenuId)
                .filter(ObjectUtil::isNotNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * 按菜单ID查询可用菜单, 并补齐被授权菜单的祖先节点, 避免出现孤儿子菜单
     *
     * @param menuIds 已授权的菜单ID
     * @return 菜单实体列表
     */
    private List<SysMenu> selectMenusByIds(Set<Long> menuIds) {
        List<SysMenu> allMenus = sysMenuMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getStatus, RbacConstant.STATUS_NORMAL)
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getSort)
                .orderByAsc(SysMenu::getId));
        if (CollUtil.isEmpty(menuIds)) {
            return new ArrayList<>();
        }
        Map<Long, SysMenu> allMenuMap = new HashMap<>(allMenus.size());
        for (SysMenu menu : allMenus) {
            allMenuMap.put(menu.getId(), menu);
        }
        Set<Long> effectiveIds = new LinkedHashSet<>(menuIds);
        for (Long menuId : menuIds) {
            SysMenu current = allMenuMap.get(menuId);
            int guard = 0;
            while (ObjectUtil.isNotNull(current) && ObjectUtil.isNotNull(current.getParentId())
                    && current.getParentId() != RbacConstant.ROOT_PARENT_ID && guard++ < 32) {
                effectiveIds.add(current.getParentId());
                current = allMenuMap.get(current.getParentId());
            }
        }
        return allMenus.stream()
                .filter(menu -> effectiveIds.contains(menu.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 将菜单实体过滤为侧边栏可见的目录/页面树
     *
     * @param menus 菜单实体列表
     * @return 树形菜单列表
     */
    private List<MenuVO> buildVisibleMenuTree(List<SysMenu> menus) {
        List<MenuVO> nodes = new LinkedList<>();
        for (SysMenu menu : menus) {
            if (MenuTypeEnum.BUTTON.getValue().equalsIgnoreCase(menu.getType())) {
                continue;
            }
            if (!ObjectUtil.equal(RbacConstant.VISIBLE_YES, menu.getVisible())) {
                continue;
            }
            MenuVO vo = new MenuVO();
            vo.setId(menu.getId());
            vo.setParentId(menu.getParentId());
            vo.setName(menu.getName());
            vo.setType(menu.getType());
            vo.setPath(menu.getPath());
            vo.setComponent(menu.getComponent());
            vo.setIcon(menu.getIcon());
            vo.setPermission(menu.getPermission());
            vo.setSort(menu.getSort());
            vo.setVisible(menu.getVisible());
            vo.setStatus(menu.getStatus());
            nodes.add(vo);
        }
        return MenuTreeUtils.build(nodes, RbacConstant.ROOT_PARENT_ID);
    }

    /**
     * 构建超级管理员的完整菜单树(全部可见目录与页面)
     *
     * @return 树形菜单列表
     */
    private List<MenuVO> buildSuperAdminMenus() {
        List<SysMenu> menus = sysMenuMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                .eq(SysMenu::getStatus, RbacConstant.STATUS_NORMAL)
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getSort)
                .orderByAsc(SysMenu::getId));
        return buildVisibleMenuTree(menus);
    }

    /**
     * 用户权限快照
     *
     * @param roles       角色编码
     * @param permissions 权限码
     * @param menus       可见菜单树
     */
    public record Snapshot(List<String> roles, List<String> permissions, List<MenuVO> menus) {

        /**
         * 构造空快照
         *
         * @return 空快照
         */
        public static Snapshot empty() {
            return new Snapshot(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
    }

    /**
     * 带过期时间的权限快照缓存项
     *
     * @param snapshot 权限快照
     * @param expireAt 过期时间
     */
    private record CachedSnapshot(Snapshot snapshot, LocalDateTime expireAt) {
    }
}
