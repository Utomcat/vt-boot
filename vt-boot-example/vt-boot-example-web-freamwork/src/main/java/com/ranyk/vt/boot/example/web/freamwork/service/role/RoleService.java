package com.ranyk.vt.boot.example.web.freamwork.service.role;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.OperateTypeEnum;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.datasource.util.PageUtils;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountRoleConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.dto.RoleDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.dto.RolePermissionConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.entity.Role;
import com.ranyk.vt.boot.example.web.freamwork.mapper.role.RoleMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.role.RoleRepository;
import com.ranyk.vt.boot.example.web.freamwork.service.account.AccountRoleConnectionService;
import com.ranyk.vt.boot.log.annotations.OperationRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
     * 账户角色关联业务逻辑类对象
     */
    private final AccountRoleConnectionService accountRoleConnectionService;
    /**
     * 角色信息数据转换接口对象
     */
    private final RoleMapper roleMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入角色信息数据操作接口对象
     *
     * @param roleRepository                  角色信息数据操作接口对象
     * @param rolePermissionConnectionService 角色权限关联关系业务逻辑类对象
     * @param accountRoleConnectionService    账户角色关联业务逻辑类对象
     * @param roleMapper                      角色信息数据转换接口对象
     */
    @Autowired
    public RoleService(RoleRepository roleRepository,
                       RolePermissionConnectionService rolePermissionConnectionService,
                       AccountRoleConnectionService accountRoleConnectionService,
                       RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.rolePermissionConnectionService = rolePermissionConnectionService;
        this.accountRoleConnectionService = accountRoleConnectionService;
        this.roleMapper = roleMapper;
    }

    /**
     * 新增一条角色数据
     *
     * @param roleDTO 角色信息数据传输对象 {@link RoleDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    @OperationRecord(desc = "新增一条角色数据", type = OperateTypeEnum.SAVE, isSaveOperationRecord = true)
    public void saveOneRole(RoleDTO roleDTO) {
        verifyRoleParams(roleDTO, OperateTypeEnum.SAVE);
        Role role = roleMapper.roleDTOToRoleEntity(roleDTO);
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getCode, roleDTO.getCode());
        Long count = this.roleRepository.selectCount(queryWrapper);
        if (count > 0) {
            log.error("新增角色信息失败, 角色编码 {} 已存在", roleDTO.getCode());
            throw new ServiceException("新增角色信息失败, 角色编码 【 %s 】 已存在".formatted(roleDTO.getCode()));
        }
        role.setCreateBy(StpUtil.getLoginIdAsString());
        role.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean saveResult = this.saveOrUpdate(role);
        if (!saveResult) {
            log.error("新增角色信息失败");
            throw new ServiceException("新增角色信息失败");
        }
    }

    /**
     * 删除一条角色数据
     *
     * @param roleDTO 角色信息数据传输对象 {@link RoleDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    @OperationRecord(desc = "删除一条角色数据", type = OperateTypeEnum.DELETE, isSaveOperationRecord = true)
    public void deleteOneRole(RoleDTO roleDTO) {
        verifyRoleParams(roleDTO, OperateTypeEnum.DELETE);
        Role role = roleMapper.roleDTOToRoleEntity(roleDTO);
        role.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean deleteResult = removeById(role);
        if (!deleteResult) {
            log.error("删除角色信息失败");
            throw new ServiceException("删除角色信息失败");
        }
    }

    /**
     * 修改一条角色数据
     *
     * @param roleDTO 角色信息数据传输对象 {@link RoleDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    @OperationRecord(desc = "修改一条角色数据", type = OperateTypeEnum.UPDATE, isSaveOperationRecord = true)
    public void updateOneRole(RoleDTO roleDTO) {
        verifyRoleParams(roleDTO, OperateTypeEnum.UPDATE);
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId, roleDTO.getId());
        Role role = this.roleRepository.selectOne(queryWrapper);
        if (StrUtil.isNotBlank(roleDTO.getName())) {
            Integer count = this.roleRepository.selectCountByNameEqAndIdNotEq(roleDTO.getName(), role.getId());
            if (count > 0) {
                log.error("修改角色信息失败, 角色名称 {} 已存在", roleDTO.getName());
                throw new ServiceException("修改角色信息失败, 角色名称 【 %s 】 已存在".formatted(roleDTO.getName()));
            }
            role.setName(roleDTO.getName());
        }
        if (StrUtil.isNotBlank(roleDTO.getCode())) {
            Integer count = this.roleRepository.selectCountByCodeEqAndIdNotEq(roleDTO.getCode(), role.getId());
            if (count > 0) {
                log.error("修改角色信息失败, 角色编码 {} 已存在", roleDTO.getCode());
                throw new ServiceException("修改角色信息失败, 角色编码 【 %s 】 已存在".formatted(roleDTO.getCode()));
            }
            role.setCode(roleDTO.getCode());
        }
        Optional.ofNullable(roleDTO.getStatus()).ifPresent(role::setStatus);
        Optional.ofNullable(roleDTO.getRemark()).ifPresent(role::setRemark);
        role.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean updateResult = this.saveOrUpdate(role);
        if (!updateResult) {
            log.error("修改角色信息失败!");
            throw new ServiceException("修改角色信息失败");
        }
    }

    /**
     * 根据条件查询角色信息
     *
     * @param roleDTO 角色信息数据传输对象 {@link RoleDTO}
     * @return {@link PageResponse}角色信息数据传输对象列表 {@link RoleDTO}
     */
    public PageResponse<RoleDTO> queryRoleByConditions(RoleDTO roleDTO) {
        Page<Role> page = PageUtils.buildPage(roleDTO);
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(roleDTO.getName()), Role::getName, roleDTO.getName());
        queryWrapper.like(StrUtil.isNotBlank(roleDTO.getCode()), Role::getCode, roleDTO.getCode());
        queryWrapper.eq(Objects.nonNull(roleDTO.getStatus()), Role::getStatus, roleDTO.getStatus());
        queryWrapper.like(StrUtil.isNotBlank(roleDTO.getRemark()), Role::getRemark, roleDTO.getRemark());
        Page<Role> rolePage = roleRepository.selectPage(page, queryWrapper);
        return PageUtils.buildPageResponse(rolePage, roleMapper.roleListToRoleDTOList(rolePage.getRecords()));
    }

    /**
     * 根据账户ID查询角色信息
     *
     * @param roleDTO 角色信息数据传输对象 {@link RoleDTO}
     * @return 角色信息数据传输对象列表 {@link RoleDTO}
     */
    public List<RoleDTO> queryRoleByAccountId(RoleDTO roleDTO) {
        List<AccountRoleConnectionDTO> accountRoleConnectionDTOS = accountRoleConnectionService.queryAccountRoleConnectionByAccountId(AccountRoleConnectionDTO.builder().accountId(roleDTO.getAccountId()).build());
        List<String> roleIdList = accountRoleConnectionDTOS.stream().map(AccountRoleConnectionDTO::getRoleId).toList();
        List<Role> roleList = this.roleRepository.selectByIds(roleIdList);
        return roleMapper.roleListToRoleDTOList(roleList);
    }

    /**
     * 验证角色信息数据传输对象参数
     *
     * @param roleDTO     角色信息数据传输对象 {@link RoleDTO}
     * @param operateType 操作类型 {@link OperateTypeEnum}
     */
    private void verifyRoleParams(RoleDTO roleDTO, OperateTypeEnum operateType) {
        switch (operateType) {
            case SAVE -> verifySaveRoleParams(roleDTO);
            case UPDATE -> verifyUpdateRoleParams(roleDTO);
            case DELETE -> verifyDeleteRoleParams(roleDTO);
            default -> throw new ServiceException("不支持的操作类型");
        }
    }

    /**
     * 验证角色信息数据传输对象 - 保存操作
     *
     * @param roleDTO 角色信息数据传输对象 {@link RoleDTO}
     */
    private void verifySaveRoleParams(RoleDTO roleDTO) {
        if (StrUtil.isBlank(roleDTO.getName())) {
            log.error("新增角色信息 - 角色名称不能为空");
            throw new ServiceException("新增角色信息 - 角色名称不能为空");
        }
        if (StrUtil.isBlank(roleDTO.getCode())) {
            log.error("新增角色信息 - 角色编码不能为空");
            throw new ServiceException("新增角色信息 - 角色编码不能为空");
        }
    }

    /**
     * 验证角色信息数据传输对象 - 更新操作
     *
     * @param roleDTO 角色信息数据传输对象 {@link RoleDTO}
     */
    private void verifyUpdateRoleParams(RoleDTO roleDTO) {
        if (StrUtil.isBlank(roleDTO.getId())) {
            log.error("更新角色信息 - 角色ID不能为空");
            throw new ServiceException("更新角色信息 - 角色ID不能为空");
        }
        if (StrUtil.isBlank(roleDTO.getName())) {
            log.error("更新角色信息 - 角色名称不能为空");
            throw new ServiceException("更新角色信息 - 角色名称不能为空");
        }
        if (StrUtil.isBlank(roleDTO.getCode())) {
            log.error("更新角色信息 - 角色编码不能为空");
            throw new ServiceException("更新角色信息 - 角色编码不能为空");
        }
    }

    /**
     * 验证角色信息数据传输对象 - 删除操作
     *
     * @param roleDTO 角色信息数据传输对象 {@link RoleDTO}
     */
    private void verifyDeleteRoleParams(RoleDTO roleDTO) {
        if (StrUtil.isBlank(roleDTO.getId()) && CollUtil.isEmpty(roleDTO.getIds())) {
            log.error("删除角色信息 - 角色ID或角色ID集合不能为空");
            throw new ServiceException("删除角色信息 - 角色ID或角色ID集合不能为空");
        }
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
        return roleMapper.roleListToRoleDTOList(Optional.ofNullable(roles).orElse(Collections.emptyList()));
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
