package com.ranyk.vt.boot.example.system.role.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.constant.RbacConstant;
import com.ranyk.vt.boot.example.system.rbac.service.RbacService;
import com.ranyk.vt.boot.example.system.role.converter.SysRoleConverter;
import com.ranyk.vt.boot.example.system.role.domain.dto.RoleFormDTO;
import com.ranyk.vt.boot.example.system.role.domain.dto.RoleQueryDTO;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysRole;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysRoleMenu;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysUserRole;
import com.ranyk.vt.boot.example.system.role.domain.vo.RoleVO;
import com.ranyk.vt.boot.example.system.role.mapper.SysRoleMapper;
import com.ranyk.vt.boot.example.system.role.mapper.SysRoleMenuMapper;
import com.ranyk.vt.boot.example.system.role.mapper.SysUserRoleMapper;
import com.ranyk.vt.boot.web.vo.MultiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CLASS_NAME: SysRoleService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色管理服务, 提供角色的分页查询、增删改、启停与菜单授权能力
 * @date: 2026-11-20
 */
@Service
@RequiredArgsConstructor
public class SysRoleService {

    /**
     * 内置角色标记: 是
     */
    private static final int BUILTIN_YES = RbacConstant.BUILTIN_YES;

    /**
     * 角色数据访问对象
     */
    private final SysRoleMapper sysRoleMapper;
    /**
     * 角色-菜单关联数据访问对象
     */
    private final SysRoleMenuMapper sysRoleMenuMapper;
    /**
     * 用户-角色关联数据访问对象
     */
    private final SysUserRoleMapper sysUserRoleMapper;
    /**
     * 角色入参转换器
     */
    private final SysRoleConverter sysRoleConverter;
    /**
     * 权限解析服务, 用于在角色变更后失效权限缓存
     */
    private final RbacService rbacService;

    /**
     * 分页查询角色。按名称、编码模糊匹配, 状态精确匹配, 结果按排序值与主键升序排列
     *
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    public MultiResult<RoleVO> page(RoleQueryDTO queryDTO) {
        Page<SysRole> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<SysRole> wrapper = Wrappers.<SysRole>lambdaQuery()
                .like(StrUtil.isNotBlank(queryDTO.getName()), SysRole::getName, queryDTO.getName())
                .like(StrUtil.isNotBlank(queryDTO.getCode()), SysRole::getCode, queryDTO.getCode())
                .eq(ObjectUtil.isNotNull(queryDTO.getStatus()), SysRole::getStatus, queryDTO.getStatus())
                .orderByAsc(SysRole::getSort)
                .orderByAsc(SysRole::getId);
        IPage<SysRole> result = sysRoleMapper.selectPage(page, wrapper);
        List<RoleVO> list = result.getRecords().stream().map(sysRoleConverter::toVO).toList();
        return MultiResult.successMulti(list, result.getTotal(), (int) result.getCurrent(), (int) result.getSize());
    }

    /**
     * 根据角色ID查询角色详情, 同时返回该角色已授权的菜单ID
     *
     * @param id 角色ID
     * @return 角色详情
     */
    public RoleVO detail(Long id) {
        SysRole role = getExistRole(id);
        RoleVO roleVO = sysRoleConverter.toVO(role);
        roleVO.setMenuIds(menuIdsOf(id));
        return roleVO;
    }

    /**
     * 查询全部启用角色的精简列表(用于用户管理分配角色), 按排序值与主键升序
     *
     * @return 角色列表
     */
    public List<RoleVO> options() {
        return sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
                        .eq(SysRole::getStatus, RbacConstant.STATUS_NORMAL)
                        .orderByAsc(SysRole::getSort)
                        .orderByAsc(SysRole::getId))
                .stream()
                .map(sysRoleConverter::toVO)
                .collect(Collectors.toList());
    }

    /**
     * 新增角色。会校验名称与编码必填、编码唯一(统一转为大写), 并支持在新增时直接授权菜单
     *
     * @param formDTO 角色表单参数
     * @return 新增角色的ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long create(RoleFormDTO formDTO) {
        if (StrUtil.isBlank(formDTO.getName())) {
            throw new ServiceException("角色名称不能为空");
        }
        if (StrUtil.isBlank(formDTO.getCode())) {
            throw new ServiceException("角色编码不能为空");
        }
        String code = StrUtil.trim(formDTO.getCode()).toUpperCase();
        Long count = sysRoleMapper.selectCount(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getCode, code));
        if (count > 0) {
            throw new ServiceException("角色编码已存在");
        }
        SysRole role = sysRoleConverter.toEntity(formDTO);
        role.setCode(code);
        role.setBuiltin(0);
        role.setSort(ObjectUtil.isNull(formDTO.getSort()) ? 0 : formDTO.getSort());
        role.setStatus(ObjectUtil.isNull(formDTO.getStatus()) ? RbacConstant.STATUS_NORMAL : formDTO.getStatus());
        sysRoleMapper.insert(role);
        if (CollUtil.isNotEmpty(formDTO.getMenuIds())) {
            replaceMenus(role.getId(), formDTO.getMenuIds());
        }
        rbacService.evictAll();
        return role.getId();
    }

    /**
     * 修改角色。仅更新提交的非空字段; 内置角色不允许停用, 角色编码不允许修改
     *
     * @param formDTO 角色表单参数, 必须携带角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleFormDTO formDTO) {
        if (ObjectUtil.isNull(formDTO.getId())) {
            throw new ServiceException("角色ID不能为空");
        }
        SysRole role = getExistRole(formDTO.getId());
        boolean builtin = ObjectUtil.equal(BUILTIN_YES, role.getBuiltin());
        if (builtin && ObjectUtil.equal(0, formDTO.getStatus())) {
            throw new ServiceException("内置角色不允许停用");
        }
        sysRoleConverter.updateEntity(formDTO, role);
        sysRoleMapper.updateById(role);
        if (ObjectUtil.isNotNull(formDTO.getMenuIds())) {
            replaceMenus(role.getId(), formDTO.getMenuIds());
        }
        rbacService.evictAll();
    }

    /**
     * 逻辑删除角色。内置角色不允许删除; 删除前需确保没有用户仍绑定该角色,
     * 删除后同步清理该角色的菜单授权关系
     *
     * @param id 角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysRole role = getExistRole(id);
        if (ObjectUtil.equal(BUILTIN_YES, role.getBuiltin())) {
            throw new ServiceException("内置角色不允许删除");
        }
        Long userCount = sysUserRoleMapper.selectCount(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getRoleId, id));
        if (userCount > 0) {
            throw new ServiceException("仍有用户绑定该角色, 请先解除绑定");
        }
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, id));
        sysRoleMapper.deleteById(id);
        rbacService.evictAll();
    }

    /**
     * 修改角色状态(启用/禁用)。内置角色不允许停用
     *
     * @param id     角色ID
     * @param status 目标状态: 1-正常 0-禁用
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        if (ObjectUtil.isNull(status)) {
            throw new ServiceException("状态不能为空");
        }
        SysRole role = getExistRole(id);
        if (ObjectUtil.equal(BUILTIN_YES, role.getBuiltin()) && ObjectUtil.equal(0, status)) {
            throw new ServiceException("内置角色不允许停用");
        }
        role.setStatus(status);
        sysRoleMapper.updateById(role);
        rbacService.evictAll();
    }

    /**
     * 为角色重新分配菜单权限(先删后增)
     *
     * @param id      角色ID
     * @param menuIds 菜单ID集合, 允许为空表示清空授权
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(Long id, List<Long> menuIds) {
        getExistRole(id);
        replaceMenus(id, menuIds);
        rbacService.evictAll();
    }

    /**
     * 查询角色已授权的菜单ID集合
     *
     * @param roleId 角色ID
     * @return 菜单ID集合
     */
    public List<Long> menuIdsOf(Long roleId) {
        return sysRoleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
                        .eq(SysRoleMenu::getRoleId, roleId))
                .stream()
                .map(SysRoleMenu::getMenuId)
                .filter(ObjectUtil::isNotNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 替换角色的菜单授权: 先清空原有授权, 再写入新的授权集合
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID集合
     */
    private void replaceMenus(Long roleId, List<Long> menuIds) {
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, roleId));
        if (CollUtil.isEmpty(menuIds)) {
            return;
        }
        Set<Long> distinctIds = new LinkedHashSet<>(menuIds);
        for (Long menuId : distinctIds) {
            if (ObjectUtil.isNotNull(menuId)) {
                sysRoleMenuMapper.insert(new SysRoleMenu(roleId, menuId));
            }
        }
    }

    /**
     * 获取存在的角色
     *
     * @param id 角色ID
     * @return 角色实体 {@link SysRole}
     */
    private SysRole getExistRole(Long id) {
        SysRole role = sysRoleMapper.selectById(id);
        if (ObjectUtil.isNull(role)) {
            throw new ServiceException("角色不存在");
        }
        return role;
    }
}
