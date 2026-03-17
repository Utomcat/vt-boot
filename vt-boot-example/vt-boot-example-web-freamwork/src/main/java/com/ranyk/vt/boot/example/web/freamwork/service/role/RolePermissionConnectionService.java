package com.ranyk.vt.boot.example.web.freamwork.service.role;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.dto.RolePermissionConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.entity.RolePermissionConnection;
import com.ranyk.vt.boot.example.web.freamwork.mapper.role.RoleMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.role.RolePermissionConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CLASS_NAME: RolePermissionConnectionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色权限关联关系业务逻辑类
 * @date: 2026-03-03
 */
@Slf4j
@Service
public class RolePermissionConnectionService extends ServiceImpl<RolePermissionConnectionRepository, RolePermissionConnection> {

    /**
     * 角色权限关联关系数据操作接口对象
     */
    private final RolePermissionConnectionRepository rolePermissionConnectionRepository;
    /**
     * 角色信息数据转换接口对象
     */
    private final RoleMapper roleMapper;

    /**
     *
     * 构造函数 - 向 Spring IOC 容器中注入角色权限关联关系数据操作接口对象
     *
     * @param rolePermissionConnectionRepository 角色权限关联关系数据操作接口对象
     * @param roleMapper                         角色信息数据转换接口对象
     */
    @Autowired
    public RolePermissionConnectionService(RolePermissionConnectionRepository rolePermissionConnectionRepository, RoleMapper roleMapper) {
        this.rolePermissionConnectionRepository = rolePermissionConnectionRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * 查询角色权限关联关系 - 通过角色ID集合
     *
     * @param rolePermissionConnectionDTO 角色权限关联关系数据传输对象, 当前方法使用 {@link RolePermissionConnectionDTO#getRoleIds()} 属性
     * @return 角色权限关联关系数据传输对象 {@link RolePermissionConnectionDTO}集合
     */
    public List<RolePermissionConnectionDTO> queryRolePermissionConnectionByRoleIds(RolePermissionConnectionDTO rolePermissionConnectionDTO) {
        // 验证角色ID列表是否为空
        validateRoleIds(rolePermissionConnectionDTO.getRoleIds());
        // 创建查询条件
        LambdaQueryWrapper<RolePermissionConnection> queryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件 - 角色ID 在 角色ID 列表 中
        queryWrapper.in(RolePermissionConnection::getRoleId, rolePermissionConnectionDTO.getRoleIds());
        // 执行查询操作
        List<RolePermissionConnection> rolePermissionConnections = rolePermissionConnectionRepository.selectList(queryWrapper);
        // 角色权限关联关系数据 转换为 数据传输对象 List 集合
        return roleMapper.rolePermissionConnectionListToRolePermissionConnectionDTOList(Optional.of(rolePermissionConnections.stream().filter(Objects::nonNull).collect(Collectors.toList())).orElse(Collections.emptyList()));
    }

    /**
     * 验证角色ID列表
     *
     * @param roleIds 角色ID列表
     */
    private void validateRoleIds(List<String> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            log.error("角色ID 列表不能为空!");
            throw new ServiceException("角色ID 列表不能为空!");
        }
    }
}
