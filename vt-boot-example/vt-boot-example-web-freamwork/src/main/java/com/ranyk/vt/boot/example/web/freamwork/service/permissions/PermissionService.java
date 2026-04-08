package com.ranyk.vt.boot.example.web.freamwork.service.permissions;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.OperateTypeEnum;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.datasource.util.PageUtils;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountRoleConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.permissions.dto.PermissionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.permissions.entity.Permission;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.dto.RolePermissionConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.mapper.permissions.PermissionMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.permissions.PermissionRepository;
import com.ranyk.vt.boot.example.web.freamwork.service.account.AccountRoleConnectionService;
import com.ranyk.vt.boot.example.web.freamwork.service.role.RolePermissionConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 账户角色关联信息业务逻辑类对象
     */
    private final AccountRoleConnectionService accountRoleConnectionService;
    /**
     * 角色权限关联信息业务逻辑类对象
     */
    private final RolePermissionConnectionService rolePermissionConnectionService;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入权限信息数据操作接口对象
     *
     * @param permissionRepository            权限信息数据操作接口对象
     * @param permissionMapper                权限信息数据转换接口对象
     * @param accountRoleConnectionService    账户角色关联信息业务逻辑类对象
     * @param rolePermissionConnectionService 角色权限关联信息业务逻辑类对象
     */
    @Autowired
    public PermissionService(PermissionRepository permissionRepository,
                             PermissionMapper permissionMapper,
                             AccountRoleConnectionService accountRoleConnectionService,
                             RolePermissionConnectionService rolePermissionConnectionService) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
        this.accountRoleConnectionService = accountRoleConnectionService;
        this.rolePermissionConnectionService = rolePermissionConnectionService;
    }

    /**
     * 新增权限信息 - 新增单条权限信息数据
     *
     * @param permissionDTO 待保存权限数据传输对象, {@link PermissionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOnePermission(PermissionDTO permissionDTO) {
        verifyPermissionParams(permissionDTO, OperateTypeEnum.SAVE);
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq(Permission::getCode, permissionDTO.getCode()).or().eq(Permission::getName, permissionDTO.getName()));
        long count = this.count(queryWrapper);
        if (count > 0) {
            log.error("新增权限信息处 - 权限编码或权限名称已存在!");
            throw new ServiceException("新增权限信息处 - 权限编码或权限名称已存在!");
        }
        Permission permission = permissionMapper.permissionDTOToPermission(permissionDTO);
        permission.setCreateBy(StpUtil.getLoginIdAsString());
        permission.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean saveResult = saveOrUpdate(permission);
        if (!saveResult) {
            log.error("保存权限信息失败!");
            throw new ServiceException("保存权限信息失败!");
        }
    }

    /**
     * 删除权限信息 - 删除单条权限信息数据
     *
     * @param permissionDTO 待删除权限数据传输对象, {@link PermissionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOnePermission(PermissionDTO permissionDTO) {
        verifyPermissionParams(permissionDTO, OperateTypeEnum.DELETE);
        Permission permission = permissionMapper.permissionDTOToPermission(permissionDTO);
        permission.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean deleteResult = removeById(permission);
        if (!deleteResult) {
            log.error("删除权限信息失败!");
            throw new ServiceException("删除权限信息失败!");
        }
    }

    /**
     * 修改权限信息 - 修改单条权限信息数据
     *
     * @param permissionDTO 待修改权限数据传输对象, {@link PermissionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOnePermission(PermissionDTO permissionDTO) {
        verifyPermissionParams(permissionDTO, OperateTypeEnum.UPDATE);
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getId, permissionDTO.getId());
        Permission permission = this.permissionRepository.selectOne(queryWrapper);
        if (StrUtil.isNotBlank(permission.getName())) {
            Integer count = this.permissionRepository.selectCountByNameEqAndIdNotEq(permissionDTO.getName(), permission.getId());
            if (count > 0) {
                log.error("修改权限信息处 - 权限名称已存在!");
                throw new ServiceException("修改权限信息处 - 权限名称已存在!");
            }
            permission.setName(permissionDTO.getName());
        }
        if (StrUtil.isNotBlank(permission.getCode())) {
            Integer count = this.permissionRepository.selectCountByCodeEqAndIdNotEq(permissionDTO.getCode(), permission.getId());
            if (count > 0) {
                log.error("修改权限信息处 - 权限编码已存在!");
                throw new ServiceException("修改权限信息处 - 权限编码已存在!");
            }
            permission.setCode(permissionDTO.getCode());
        }
        Optional.ofNullable(permissionDTO.getType()).ifPresent(permission::setType);
        Optional.ofNullable(permissionDTO.getStatus()).ifPresent(permission::setStatus);
        Optional.ofNullable(permission.getRemark()).ifPresent(permission::setRemark);
        permission.setUpdateBy(StpUtil.getLoginIdAsString());
        Boolean updateResult = this.permissionRepository.updateByIdEq(permission);
        if (!updateResult) {
            log.error("修改权限信息失败!");
            throw new ServiceException("修改权限信息失败!");
        }
    }

    /**
     * 根据条件查询权限信息
     *
     * @param permissionDTO 待查询权限数据传输对象, {@link PermissionDTO}
     * @return 查询结果, {@link PageResponse} - {@link PermissionDTO}
     */
    public PageResponse<PermissionDTO> queryPermissionByConditions(PermissionDTO permissionDTO) {
        IPage<Permission> page = PageUtils.buildPage(permissionDTO);
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(permissionDTO.getName()), Permission::getName, permissionDTO.getName());
        queryWrapper.like(StrUtil.isNotBlank(permissionDTO.getCode()), Permission::getCode, permissionDTO.getCode());
        queryWrapper.eq(Objects.nonNull(permissionDTO.getType()), Permission::getType, permissionDTO.getType());
        queryWrapper.eq(Objects.nonNull(permissionDTO.getStatus()), Permission::getStatus, permissionDTO.getStatus());
        queryWrapper.like(StrUtil.isNotBlank(permissionDTO.getRemark()), Permission::getRemark, permissionDTO.getRemark());
        IPage<Permission> permissionIPage = permissionRepository.selectPage(page, queryWrapper);
        return PageUtils.buildPageResponse(permissionIPage, permissionMapper.permissionListToPermissionDTOList(permissionIPage.getRecords()));
    }

    /**
     * 根据账户ID查询账户拥有的权限信息
     *
     * @param permissionDTO 待查询权限数据传输对象, {@link PermissionDTO}
     * @return 查询结果, {@link List} - {@link PermissionDTO}
     */
    public List<PermissionDTO> queryPermissionByAccountId(PermissionDTO permissionDTO) {
        List<AccountRoleConnectionDTO> accountRoleConnectionDTOList = accountRoleConnectionService.queryAccountRoleConnectionByAccountId(AccountRoleConnectionDTO.builder().accountId(permissionDTO.getAccountId()).build());
        List<String> roleIds = accountRoleConnectionDTOList.stream().map(AccountRoleConnectionDTO::getRoleId).toList();
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<RolePermissionConnectionDTO> rolePermissionConnectionDTOList = rolePermissionConnectionService.queryRolePermissionConnectionByRoleIds(RolePermissionConnectionDTO.builder().roleIds(roleIds).build());
        List<String> permissionIds = rolePermissionConnectionDTOList.stream().map(RolePermissionConnectionDTO::getPermissionId).toList();
        if (CollUtil.isEmpty(permissionIds)) {
            return Collections.emptyList();
        }
        List<Permission> permissionList = this.permissionRepository.selectByIds(permissionIds);
        return permissionMapper.permissionListToPermissionDTOList(permissionList);
    }

    /**
     * 权限信息参数验证
     *
     * @param permissionDTO 权限数据传输对象, {@link PermissionDTO}
     * @param operateType   操作类型, {@link OperateTypeEnum}
     */
    private void verifyPermissionParams(PermissionDTO permissionDTO, OperateTypeEnum operateType) {
        switch (operateType) {
            case SAVE -> verifySavePermissionParams(permissionDTO);
            case DELETE -> verifyDeletePermissionParams(permissionDTO);
            case BATCH_DELETE -> verifyBatchDeletePermissionParams(permissionDTO);
            case UPDATE -> verifyUpdatePermissionParams(permissionDTO);
            default -> throw new ServiceException("不支持的操作类型!");
        }
    }

    /**
     * 权限信息参数验证 - 新增权限信息参数数据校验
     *
     * @param permissionDTO 待新增权限数据传输对象, {@link PermissionDTO}, 此处验证的参数有 {@link PermissionDTO#getName()}, {@link PermissionDTO#getCode()}, {@link PermissionDTO#getType()}
     */
    private void verifySavePermissionParams(PermissionDTO permissionDTO) {
        if (StrUtil.isBlank(permissionDTO.getName())) {
            log.error("新增权限失败，权限名称不能为空!");
            throw new ServiceException("新增权限失败，权限名称不能为空!");
        }
        if (StrUtil.isBlank(permissionDTO.getCode())) {
            log.error("新增权限失败，权限编码不能为空!");
            throw new ServiceException("新增权限失败，权限编码不能为空!");
        }
        if (Objects.isNull(permissionDTO.getType())) {
            log.error("新增权限失败，权限类型不能为空!");
            throw new ServiceException("新增权限失败，权限类型不能为空!");
        }
    }

    /**
     * 权限信息参数验证 - 删除单条权限信息参数数据校验
     *
     * @param permissionDTO 待删除权限数据传输对象, {@link PermissionDTO}, 此处验证的参数有 {@link PermissionDTO#getId()}
     */
    private void verifyDeletePermissionParams(PermissionDTO permissionDTO) {
        if (StrUtil.isBlank(permissionDTO.getId())) {
            log.error("删除权限失败，权限ID不能为空!");
            throw new ServiceException("删除权限失败，权限ID不能为空!");
        }
    }

    /**
     * 权限信息参数验证 - 批量删除权限信息参数数据校验
     *
     * @param permissionDTO 待批量删除权限数据传输对象, {@link PermissionDTO}, 此处验证的参数有 {@link PermissionDTO#getIds()}
     */
    private void verifyBatchDeletePermissionParams(PermissionDTO permissionDTO) {
        if (CollectionUtil.isEmpty(permissionDTO.getIds())) {
            log.error("批量删除权限失败，权限ID List 不能为空!");
            throw new ServiceException("批量删除权限失败，权限ID List 不能为空!");
        }
    }

    /**
     * 权限信息参数验证 - 修改单条权限信息参数数据校验
     *
     * @param permissionDTO 待修改权限数据传输对象, {@link PermissionDTO}, 此处验证的参数有 {@link PermissionDTO#getId()}, {@link PermissionDTO#getName()}, {@link PermissionDTO#getCode()}, {@link PermissionDTO#getType()}
     */
    private void verifyUpdatePermissionParams(PermissionDTO permissionDTO) {
        if (StrUtil.isBlank(permissionDTO.getId())) {
            log.error("修改权限失败，权限ID不能为空!");
            throw new ServiceException("修改权限失败，权限ID不能为空!");
        }
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
        return permissionMapper.permissionListToPermissionDTOList(Optional.of(permissionList.stream().filter(Objects::nonNull).collect(Collectors.toList())).orElse(Collections.emptyList()));
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
