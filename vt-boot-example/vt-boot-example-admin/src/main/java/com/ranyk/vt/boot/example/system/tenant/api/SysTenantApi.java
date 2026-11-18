package com.ranyk.vt.boot.example.system.tenant.api;

import com.ranyk.vt.boot.satoken.util.SecurityUtils;
import com.ranyk.vt.boot.example.system.tenant.converter.SysTenantConverter;
import com.ranyk.vt.boot.example.system.tenant.domain.po.TenantFormPO;
import com.ranyk.vt.boot.example.system.tenant.domain.po.TenantQueryPO;
import com.ranyk.vt.boot.example.system.tenant.domain.vo.TenantVO;
import com.ranyk.vt.boot.example.system.tenant.service.SysTenantService;
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
 * CLASS_NAME: SysTenantApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户管理接口, 提供租户的分页查询、详情、增删改与启停操作, 全部要求管理员权限
 * @date: 2026-11-16
 */
@RestController
@RequestMapping("/api/system/tenant")
@RequiredArgsConstructor
public class SysTenantApi {

    /**
     * 租户管理服务
     */
    private final SysTenantService sysTenantService;
    /**
     * 租户入参转换器
     */
    private final SysTenantConverter sysTenantConverter;

    /**
     * 分页查询租户列表。先校验管理员权限, 再按名称、编码、套餐、状态等条件过滤并分页返回租户信息,
     * 结果按创建时间倒序排列。
     *
     * @param queryPO 分页与筛选参数, 包含页码、每页条数以及可选的名称、编码、套餐、状态条件
     * @return 分页结果, 包含当前页租户列表、总条数、页码与页大小
     */
    @PostMapping("/page")
    public MultiResult<TenantVO> page(@RequestBody TenantQueryPO queryPO) {
        SecurityUtils.requireAdmin();
        return sysTenantService.page(sysTenantConverter.toQueryDTO(queryPO));
    }

    /**
     * 根据租户ID查询租户详情。先校验管理员权限, 再返回指定租户的完整资料, 租户不存在时返回业务错误。
     *
     * @param id 租户ID
     * @return 租户详情结果
     */
    @GetMapping("/{id}")
    public Result<TenantVO> detail(@PathVariable Long id) {
        SecurityUtils.requireAdmin();
        return Result.success(sysTenantService.detail(id));
    }

    /**
     * 新增租户。先校验管理员权限, 然后校验名称与编码并保证编码唯一, 返回新增租户的ID。
     *
     * @param formPO 租户表单参数, 包含名称、编码、套餐、配额、到期时间等信息
     * @return 新增租户的ID
     */
    @PostMapping
    public Result<Long> create(@RequestBody TenantFormPO formPO) {
        SecurityUtils.requireAdmin();
        return Result.success(sysTenantService.create(sysTenantConverter.toFormDTO(formPO)));
    }

    /**
     * 修改租户资料。先校验管理员权限, 仅更新提交的非空字段, 租户编码作为唯一标识不在此修改。
     *
     * @param formPO 租户表单参数, 必须携带租户ID
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody TenantFormPO formPO) {
        SecurityUtils.requireAdmin();
        sysTenantService.update(sysTenantConverter.toFormDTO(formPO));
        return Result.success(null);
    }

    /**
     * 逻辑删除租户。先校验管理员权限, 仅将租户标记为已删除, 保留租户及其关联数据用于审计。
     *
     * @param id 租户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        SecurityUtils.requireAdmin();
        sysTenantService.delete(id);
        return Result.success(null);
    }

    /**
     * 启用或禁用租户。先校验管理员权限, 将租户状态修改为指定值。
     *
     * @param id     租户ID
     * @param status 目标状态: 1-正常 0-禁用
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        SecurityUtils.requireAdmin();
        sysTenantService.changeStatus(id, status);
        return Result.success(null);
    }
}
