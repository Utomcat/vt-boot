package com.ranyk.vt.boot.example.system.role.api;

import com.ranyk.vt.boot.example.system.role.converter.SysRoleConverter;
import com.ranyk.vt.boot.example.system.role.domain.po.RoleFormPO;
import com.ranyk.vt.boot.example.system.role.domain.po.RoleQueryPO;
import com.ranyk.vt.boot.example.system.role.domain.vo.RoleVO;
import com.ranyk.vt.boot.example.system.role.service.SysRoleService;
import com.ranyk.vt.boot.satoken.util.SecurityUtils;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CLASS_NAME: SysRoleApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色管理接口, 提供角色的分页查询、详情、增删改、启停与菜单授权能力
 * @date: 2026-11-20
 */
@RestController
@RequestMapping("/api/system/role")
@RequiredArgsConstructor
public class SysRoleApi {

    /**
     * 角色管理服务
     */
    private final SysRoleService sysRoleService;
    /**
     * 角色入参转换器
     */
    private final SysRoleConverter sysRoleConverter;

    /**
     * 分页查询角色列表。要求 system:role:list 权限
     *
     * @param queryPO 分页与筛选参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public MultiResult<RoleVO> page(@RequestBody RoleQueryPO queryPO) {
        SecurityUtils.requirePermission("system:role:list");
        return sysRoleService.page(sysRoleConverter.toQueryDTO(queryPO));
    }

    /**
     * 根据角色ID查询角色详情, 同时返回已授权的菜单ID。要求 system:role:list 权限
     *
     * @param id 角色ID
     * @return 角色详情
     */
    /**
     * 查询全部启用角色列表, 供用户管理分配角色时选择。要求 system:user:list 权限
     *
     * @return 角色列表
     */
    @GetMapping("/options")
    public Result<List<RoleVO>> options() {
        SecurityUtils.requirePermission("system:user:list");
        return Result.success(sysRoleService.options());
    }

    @GetMapping("/{id}")
    public Result<RoleVO> detail(@PathVariable Long id) {
        SecurityUtils.requirePermission("system:role:list");
        return Result.success(sysRoleService.detail(id));
    }

    /**
     * 新增角色。要求 system:role:add 权限
     *
     * @param formPO 角色表单参数
     * @return 新增角色的ID
     */
    @PostMapping
    public Result<Long> create(@RequestBody RoleFormPO formPO) {
        SecurityUtils.requirePermission("system:role:add");
        return Result.success(sysRoleService.create(sysRoleConverter.toFormDTO(formPO)));
    }

    /**
     * 修改角色。要求 system:role:edit 权限
     *
     * @param formPO 角色表单参数, 必须携带角色ID
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody RoleFormPO formPO) {
        SecurityUtils.requirePermission("system:role:edit");
        sysRoleService.update(sysRoleConverter.toFormDTO(formPO));
        return Result.success(null);
    }

    /**
     * 逻辑删除角色。要求 system:role:delete 权限
     *
     * @param id 角色ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        SecurityUtils.requirePermission("system:role:delete");
        sysRoleService.delete(id);
        return Result.success(null);
    }

    /**
     * 启用或禁用角色。要求 system:role:edit 权限
     *
     * @param id     角色ID
     * @param status 目标状态: 1-正常 0-禁用
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        SecurityUtils.requirePermission("system:role:edit");
        sysRoleService.changeStatus(id, status);
        return Result.success(null);
    }

    /**
     * 为角色重新分配菜单权限。要求 system:role:assign 权限
     *
     * @param id      角色ID
     * @param menuIds 菜单ID集合, 为空表示清空授权
     * @return 操作结果
     */
    @PutMapping("/{id}/menus")
    public Result<Void> assignMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        SecurityUtils.requirePermission("system:role:assign");
        sysRoleService.assignMenus(id, menuIds);
        return Result.success(null);
    }
}
