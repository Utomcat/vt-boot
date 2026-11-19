package com.ranyk.vt.boot.example.system.user.api;

import com.ranyk.vt.boot.satoken.util.SecurityUtils;
import com.ranyk.vt.boot.example.system.user.converter.SysUserConverter;
import com.ranyk.vt.boot.example.system.user.domain.po.UserFormPO;
import com.ranyk.vt.boot.example.system.user.domain.po.UserPasswordPO;
import com.ranyk.vt.boot.example.system.user.domain.po.UserQueryPO;
import com.ranyk.vt.boot.example.system.user.domain.vo.UserVO;
import com.ranyk.vt.boot.example.system.user.service.SysUserService;
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

/**
 * CLASS_NAME: SysUserApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户管理接口, 提供用户的分页查询、详情、增删改、重置密码与启停操作, 全部要求管理员权限
 * @date: 2026-11-16
 */
@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
public class SysUserApi {

    /**
     * 用户管理服务
     */
    private final SysUserService sysUserService;
    /**
     * 用户入参转换器
     */
    private final SysUserConverter sysUserConverter;

    /**
     * 分页查询用户列表。先校验当前登录用户具备管理员角色, 再根据用户名、状态、角色等条件
     * 过滤并分页返回用户信息, 结果按创建时间倒序排列。
     *
     * @param queryPO 分页与筛选参数, 包含页码、每页条数以及可选的用户名、状态、角色条件
     * @return 分页结果, 包含当前页用户列表、总条数、页码与页大小
     */
    @PostMapping("/page")
    public MultiResult<UserVO> page(@RequestBody UserQueryPO queryPO) {
        SecurityUtils.requirePermission("system:user:list");
        return sysUserService.page(sysUserConverter.toQueryDTO(queryPO));
    }

    /**
     * 根据用户ID查询用户详情。先校验管理员权限, 再返回指定用户的完整资料, 用户不存在时返回业务错误。
     *
     * @param id 用户ID
     * @return 用户详情结果
     */
    @GetMapping("/{id}")
    public Result<UserVO> detail(@PathVariable Long id) {
        SecurityUtils.requirePermission("system:user:list");
        return Result.success(sysUserService.detail(id));
    }

    /**
     * 新增用户。先校验管理员权限, 然后校验用户名、密码并加密存储密码, 返回新增用户的ID。
     *
     * @param formPO 用户表单参数, 包含用户名、密码、昵称、角色、状态等信息
     * @return 新增用户的ID
     */
    @PostMapping
    public Result<Long> create(@RequestBody UserFormPO formPO) {
        SecurityUtils.requirePermission("system:user:add");
        return Result.success(sysUserService.create(sysUserConverter.toFormDTO(formPO)));
    }

    /**
     * 修改用户资料。先校验管理员权限, 仅更新提交的非空字段。密码不在此处修改, 统一通过重置密码接口处理。
     *
     * @param formPO 用户表单参数, 必须携带用户ID
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody UserFormPO formPO) {
        SecurityUtils.requirePermission("system:user:edit");
        sysUserService.update(sysUserConverter.toFormDTO(formPO));
        return Result.success(null);
    }

    /**
     * 逻辑删除用户。先校验管理员权限, 仅将用户标记为已删除, 保留历史数据。
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        SecurityUtils.requirePermission("system:user:delete");
        sysUserService.delete(id);
        return Result.success(null);
    }

    /**
     * 重置用户密码。先校验管理员权限, 将新密码加密后覆盖用户原密码。新密码通过请求体传递,
     * 避免明文出现在 URL 查询串与被访问日志记录。
     *
     * @param id         用户ID
     * @param passwordPO 重置密码请求体, 包含新密码明文
     * @return 操作结果
     */
    @PutMapping("/{id}/password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestBody UserPasswordPO passwordPO) {
        SecurityUtils.requirePermission("system:user:reset");
        sysUserService.resetPassword(id, passwordPO.getNewPassword());
        return Result.success(null);
    }

    /**
     * 启用或禁用用户。先校验管理员权限, 将用户状态修改为指定值。
     *
     * @param id     用户ID
     * @param status 目标状态: 1-正常 0-禁用
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        SecurityUtils.requirePermission("system:user:status");
        sysUserService.changeStatus(id, status);
        return Result.success(null);
    }
}
