package com.ranyk.vt.boot.example.satoken.service.satoken.extension;

import cn.dev33.satoken.stp.StpInterface;
import com.ranyk.vt.boot.example.satoken.domain.account.dto.AccountRoleConnectionDTO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.dto.PermissionDTO;
import com.ranyk.vt.boot.example.satoken.domain.role.dto.RoleDTO;
import com.ranyk.vt.boot.example.satoken.domain.role.dto.RolePermissionConnectionDTO;
import com.ranyk.vt.boot.example.satoken.service.account.AccountRoleConnectionService;
import com.ranyk.vt.boot.example.satoken.service.permissions.PermissionService;
import com.ranyk.vt.boot.example.satoken.service.role.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CLASS_NAME: StpInterfaceImpl.java
 *
 * @author ranyk
 * @version V1.0
 * @description: sa-token 权限验证扩展
 * @date: 2026-03-02
 */
@Slf4j
@Service
public class StpInterfaceImpl implements StpInterface {

    /**
     * 账户角色关联业务逻辑类对象
     */
    private final AccountRoleConnectionService accountRoleConnectionService;
    /**
     * 角色信息业务逻辑类对象
     */
    private final RoleService roleService;
    /**
     * 权限信息业务逻辑类对象
     */
    private final PermissionService permissionService;

    /**
     * 构造方法 - 向 Spring IOC 容器中自动注入 RoleService 和 PermissionService 对象
     *
     * @param roleService                  角色信息业务逻辑类对象
     * @param permissionService            权限信息业务逻辑类对象
     * @param accountRoleConnectionService 账户角色关联业务逻辑类对象
     */
    @Autowired
    public StpInterfaceImpl(AccountRoleConnectionService accountRoleConnectionService,
                            RoleService roleService,
                            PermissionService permissionService) {
        this.accountRoleConnectionService = accountRoleConnectionService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    /**
     * 返回指定账号id所拥有的权限码集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 查询指定 账号id 所拥有的角色信息
        List<RoleDTO> roleDTOS = queryRoleByAccountId(loginId.toString());
        // 获取角色信息集合中所有的权限关联关系
        List<RolePermissionConnectionDTO> rolePermissionConnectionDTOS = roleService.queryRolePermissionConnectionByRoleIds(RoleDTO.builder().ids(roleDTOS.stream().map(RoleDTO::getId).collect(Collectors.toList())).build());
        // 获取权限关联关系集合中所有的权限信息
        List<PermissionDTO> permissionDTOS = permissionService.queryPermissionByPermissionIds(PermissionDTO.builder().ids(rolePermissionConnectionDTOS.stream().map(RolePermissionConnectionDTO::getPermissionId).filter(Objects::nonNull).collect(Collectors.toList())).build());
        // 返回权限码集合 过滤 null 权限码
        return Optional.of(permissionDTOS.stream().map(PermissionDTO::getCode).filter(Objects::nonNull).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 查询指定 账号id 所拥有的角色信息
        List<RoleDTO> roleDTOS = queryRoleByAccountId(loginId.toString());
        // 返回角色标识集合 过滤 null 角色标识
        return Optional.of(roleDTOS.stream().map(RoleDTO::getCode).filter(Objects::nonNull).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    /**
     * 查询指定 账号id 所拥有的角色信息
     *
     * @param accountId 账号id
     * @return 返回该账户id所拥有的角色信息 {@link RoleDTO} List 集合
     */
    private List<RoleDTO> queryRoleByAccountId(String accountId) {
        // 查询指定 账号id 所拥有的 账户角色信息关联关系
        List<AccountRoleConnectionDTO> accountRoleConnectionDTOS = accountRoleConnectionService.queryAccountRoleConnectionByAccountId(AccountRoleConnectionDTO.builder().accountId(accountId).build());
        // 查询所有的角色信息
        List<RoleDTO> roleDTOS = roleService.queryRoleByRoleIds(RoleDTO.builder().ids(accountRoleConnectionDTOS.stream().map(AccountRoleConnectionDTO::getRoleId).collect(Collectors.toList())).build());
        // 返回角色信息集合 过滤 null 角色信息
        return Optional.of(roleDTOS.stream().filter(Objects::nonNull).collect(Collectors.toList())).orElse(Collections.emptyList());
    }
}
