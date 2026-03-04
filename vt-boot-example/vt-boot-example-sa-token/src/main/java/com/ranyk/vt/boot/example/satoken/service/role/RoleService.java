package com.ranyk.vt.boot.example.satoken.service.role;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.satoken.domain.role.dto.RoleDTO;
import com.ranyk.vt.boot.example.satoken.domain.role.dto.RolePermissionConnectionDTO;
import com.ranyk.vt.boot.example.satoken.domain.role.entity.Role;
import com.ranyk.vt.boot.example.satoken.mapper.role.RoleMapper;
import com.ranyk.vt.boot.example.satoken.repository.role.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * CLASS_NAME: RoleService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色信息业务逻辑类
 * @date: 2026-03-03
 */
@Slf4j
@Service
public class RoleService extends ServiceImpl<RoleRepository, Role> {

    /**
     * 角色信息数据操作接口对象
     */
    private final RoleRepository roleRepository;
    /**
     * 角色权限关联关系业务逻辑类对象
     */
    private final RolePermissionConnectionService rolePermissionConnectionService;
    /**
     * 角色信息数据转换接口对象
     */
    private final RoleMapper roleMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入角色信息数据操作接口对象
     *
     * @param roleRepository                  角色信息数据操作接口对象
     * @param rolePermissionConnectionService 角色权限关联关系业务逻辑类对象
     * @param roleMapper                      角色信息数据转换接口对象
     */
    @Autowired
    public RoleService(RoleRepository roleRepository, RolePermissionConnectionService rolePermissionConnectionService, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.rolePermissionConnectionService = rolePermissionConnectionService;
        this.roleMapper = roleMapper;
    }

    /**
     * 查询角色信息 - 通过角色ID集合
     *
     * @param roleDTO 角色信息数据传输对象, 当前方法使用 {@link RoleDTO#getIds()} 属性
     * @return 返回查询的角色信息数据传输对象 {@link RoleDTO}集合
     */
    public List<RoleDTO> queryRoleByRoleIds(RoleDTO roleDTO) {
        // 验证角色ID列表是否为空
        validateRoleIds(roleDTO.getIds());
        // 构建查询条件
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件 - 角色ID 在 角色ID 列表 中
        queryWrapper.in(Role::getId, roleDTO.getIds());
        // 查询角色信息查询操作
        List<Role> roles = roleRepository.selectList(queryWrapper);
        // 返回查询结果 - 角色信息数据 转换为 数据传输对象 List 集合
        return roleMapper.roleEntityListToRoleDTOList(Optional.ofNullable(roles).orElse(Collections.emptyList()));
    }

    /**
     * 查询角色权限关联关系 - 通过角色ID集合
     *
     * @param roleDTO 角色信息数据传输对象, 当前方法使用 {@link RoleDTO#getIds()} 属性
     * @return 角色权限关联关系数据传输对象 {@link RolePermissionConnectionDTO}集合
     */
    public List<RolePermissionConnectionDTO> queryRolePermissionConnectionByRoleIds(RoleDTO roleDTO) {
        // 验证角色ID列表是否为空
        validateRoleIds(roleDTO.getIds());
        // 返回查询结果 - 角色权限关联关系数据 转换为 数据传输对象 List 集合
        return rolePermissionConnectionService.queryRolePermissionConnectionByRoleIds(RolePermissionConnectionDTO.builder().roleIds(roleDTO.getIds()).build());
    }

    /**
     *
     * 验证角色ID列表是否为空
     *
     * @param roleIds 角色ID列表
     */
    private void validateRoleIds(List<String> roleIds) {
        // 验证角色ID列表是否为空
        if (CollUtil.isEmpty(roleIds)) {
            log.error("角色ID列表不能为空");
            throw new ServiceException("角色ID列表不能为空");
        }
    }

}
