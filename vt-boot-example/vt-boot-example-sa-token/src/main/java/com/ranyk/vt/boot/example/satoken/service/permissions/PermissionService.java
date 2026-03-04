package com.ranyk.vt.boot.example.satoken.service.permissions;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.satoken.domain.permissions.dto.PermissionDTO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.entity.Permission;
import com.ranyk.vt.boot.example.satoken.mapper.permissions.PermissionMapper;
import com.ranyk.vt.boot.example.satoken.repository.permissions.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CLASS_NAME: PermissionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 权限信息业务逻辑类
 * @date: 2026-03-03
 */
@Slf4j
@Service
public class PermissionService extends ServiceImpl<PermissionRepository, Permission> {

    /**
     * 权限信息数据操作接口对象
     */
    private final PermissionRepository permissionRepository;
    /**
     * 权限信息数据转换接口对象
     */
    private final PermissionMapper permissionMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入权限信息数据操作接口对象
     *
     * @param permissionRepository 权限信息数据操作接口对象
     * @param permissionMapper     权限信息数据转换接口对象
     */
    @Autowired
    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    /**
     * 查询权限信息 - 通过权限ID集合
     *
     * @param permissionDTO 权限信息数据传输对象, 当前方法使用 {@link PermissionDTO#getIds()} 属性
     * @return 权限信息数据传输对象 {@link PermissionDTO}集合
     */
    public List<PermissionDTO> queryPermissionByPermissionIds(PermissionDTO permissionDTO) {
        // 验证权限ID列表
        validatePermissionIds(permissionDTO.getIds());
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permission::getId, permissionDTO.getIds());
        List<Permission> permissionList = permissionRepository.selectList(queryWrapper);
        return permissionMapper.permissionEntityToPermissionDTO(Optional.of(permissionList.stream().filter(Objects::nonNull).collect(Collectors.toList())).orElse(Collections.emptyList()));
    }

    /**
     * 验证权限ID列表
     *
     * @param permissionIds 权限ID列表
     */
    private void validatePermissionIds(List<String> permissionIds) {
        if (CollUtil.isEmpty(permissionIds)) {
            log.error("权限ID 列表不能为空!");
            throw new ServiceException("权限ID 列表不能为空!");
        }
    }

}
