package com.ranyk.vt.boot.example.system.menu.api;

import com.ranyk.vt.boot.example.system.menu.converter.SysMenuConverter;
import com.ranyk.vt.boot.example.system.menu.domain.po.MenuFormPO;
import com.ranyk.vt.boot.example.system.menu.domain.vo.MenuVO;
import com.ranyk.vt.boot.example.system.menu.service.SysMenuService;
import com.ranyk.vt.boot.satoken.util.SecurityUtils;
import com.ranyk.vt.boot.web.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CLASS_NAME: SysMenuApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 菜单管理接口, 提供菜单树查询与菜单的增删改能力
 * @date: 2026-11-20
 */
@RestController
@RequestMapping("/api/system/menu")
@RequiredArgsConstructor
public class SysMenuApi {

    /**
     * 菜单管理服务
     */
    private final SysMenuService sysMenuService;
    /**
     * 菜单入参转换器
     */
    private final SysMenuConverter sysMenuConverter;

    /**
     * 查询全部菜单树(含目录、页面与按钮)。该接口仅返回菜单元数据, 供菜单管理与角色授权共同使用,
     * 因此只要求登录, 不做额外权限校验
     *
     * @return 树形菜单列表
     */
    @GetMapping("/tree")
    public Result<List<MenuVO>> tree() {
        return Result.success(sysMenuService.tree());
    }

    /**
     * 新增菜单。要求 system:menu:add 权限
     *
     * @param formPO 菜单表单参数
     * @return 新增菜单的ID
     */
    @PostMapping
    public Result<Long> create(@RequestBody MenuFormPO formPO) {
        SecurityUtils.requirePermission("system:menu:add");
        return Result.success(sysMenuService.create(sysMenuConverter.toFormDTO(formPO)));
    }

    /**
     * 修改菜单。要求 system:menu:edit 权限
     *
     * @param formPO 菜单表单参数, 必须携带菜单ID
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody MenuFormPO formPO) {
        SecurityUtils.requirePermission("system:menu:edit");
        sysMenuService.update(sysMenuConverter.toFormDTO(formPO));
        return Result.success(null);
    }

    /**
     * 逻辑删除菜单。要求 system:menu:delete 权限
     *
     * @param id 菜单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        SecurityUtils.requirePermission("system:menu:delete");
        sysMenuService.delete(id);
        return Result.success(null);
    }
}
