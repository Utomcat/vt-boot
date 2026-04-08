package com.ranyk.vt.boot.example.web.freamwork.api.role;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.dto.RoleDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.po.*;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.vo.QueryAccountRoleVO;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.vo.QueryRoleVO;
import com.ranyk.vt.boot.example.web.freamwork.mapper.role.RoleMapper;
import com.ranyk.vt.boot.example.web.freamwork.service.role.RolePermissionConnectionService;
import com.ranyk.vt.boot.example.web.freamwork.service.role.RoleService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CLASS_NAME: RoleApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色接口API
 * @date: 2026-03-05
 */
@RestController
@RequestMapping("/api/role")
public class RoleApi {

    /**
     * 角色信息业务逻辑类对象
     */
    private final RoleService roleService;
    /**
     * 角色权限关联信息业务逻辑类对象
     */
    private final RolePermissionConnectionService rolePermissionConnectionService;
    /**
     * 角色信息数据转换接口对象
     */
    private final RoleMapper roleMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入角色信息业务逻辑类对象
     *
     * @param roleService                     角色信息业务逻辑类对象
     * @param rolePermissionConnectionService 角色权限关联信息业务逻辑类对象
     * @param roleMapper                      角色信息数据转换接口对象
     */
    @Autowired
    public RoleApi(RoleService roleService,
                   RolePermissionConnectionService rolePermissionConnectionService,
                   RoleMapper roleMapper) {
        this.roleService = roleService;
        this.rolePermissionConnectionService = rolePermissionConnectionService;
        this.roleMapper = roleMapper;
    }

    /**
     * 新增一个角色
     *
     * @param saveRolePO 新增角色请求参数封装 PO 类, {@link SaveRolePO}
     * @return 新增结果 {@link Boolean} , true: 新增成功; false: 新增失败;
     */
    @PostMapping
    @SaCheckPermission(value = {"add:role"})
    @Log(operation = "新增一个角色", type = Log.LogType.INSERT)
    public Result<Boolean> saveRole(@RequestBody SaveRolePO saveRolePO) {
        roleService.saveOneRole(roleMapper.saveRolePOToRoleDTO(saveRolePO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 删除一个角色
     *
     * @param deleteRolePO 删除角色请求参数封装 PO 类, {@link DeleteRolePO}
     * @return 删除结果 {@link Boolean} , true: 删除成功; false: 删除失败;
     */
    @DeleteMapping
    @SaCheckPermission(value = {"delete:role"})
    @Log(operation = "删除一个角色", type = Log.LogType.DELETE)
    public Result<Boolean> deleteRole(@RequestBody DeleteRolePO deleteRolePO) {
        roleService.deleteOneRole(roleMapper.deleteRolePOToRoleDTO(deleteRolePO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 更新一个角色
     *
     * @param updateRolePO 更新角色请求参数封装 PO 类, {@link UpdateRolePO}
     * @return 更新结果 {@link Boolean} , true: 更新成功; false: 更新失败;
     */
    @PutMapping
    @SaCheckPermission(value = {"update:role"})
    @Log(operation = "更新一个角色", type = Log.LogType.UPDATE)
    public Result<Boolean> updateRole(@RequestBody UpdateRolePO updateRolePO) {
        roleService.updateOneRole(roleMapper.updateRolePOToRoleDTO(updateRolePO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 查询角色信息 - 分页
     *
     * @param queryRolePO 查询角色信息请求参数封装 PO 类, {@link QueryRolePO}
     * @return 查询结果 {@link MultiResult} - 查询结果视图对象 {@link QueryRoleVO}
     */
    @GetMapping
    @SaCheckPermission(value = {"query:role"})
    @Log(operation = "查询角色信息 - 分页", type = Log.LogType.SELECT)
    public MultiResult<QueryRoleVO> queryRole(QueryRolePO queryRolePO) {
        PageResponse<RoleDTO> roleDTOPageResponse = roleService.queryRoleByConditions(roleMapper.queryRolePOToRoleDTO(queryRolePO));
        return MultiResult.successMulti(roleMapper.roleDTOListToQueryRoleVOList(roleDTOPageResponse.getData()),
                Long.parseLong(String.valueOf(roleDTOPageResponse.getTotal())),
                roleDTOPageResponse.getCurrentPage(),
                roleDTOPageResponse.getPageSize());
    }

    /**
     * 为角色绑定权限
     *
     * @param roleBundledPermissionPO 角色权限关联请求参数封装 PO 类, {@link RoleBundledPermissionPO}
     * @return 批量新增结果 {@link Boolean} , true: 批量新增成功; false: 批量新增失败;
     */
    @PostMapping("/bundled/permissions")
    @SaCheckPermission(value = {"bundled:role:permission"})
    @Log(operation = "为角色绑定权限", type = Log.LogType.INSERT)
    public Result<Boolean> bundledPermissions(@RequestBody RoleBundledPermissionPO roleBundledPermissionPO) {
        rolePermissionConnectionService.batchSaveRolePermissionConnection(roleMapper.roleBundledPermissionPOToRolePermissionConnectionDTO(roleBundledPermissionPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 依据账户ID - 查询账户所绑定的角色信息
     *
     * @param queryRoleByAccountIdPO 查询账户角色信息请求参数封装 PO 类, {@link QueryRoleByAccountIdPO}
     * @return 查询结果 {@link Result} - 查询结果视图对象 {@link QueryAccountRoleVO}
     */
    @GetMapping("/by/account/id")
    @SaCheckPermission(value = {"query:role"})
    @Log(operation = "查询账户角色信息", type = Log.LogType.SELECT)
    public Result<List<QueryAccountRoleVO>> queryRoleByAccountId(QueryRoleByAccountIdPO queryRoleByAccountIdPO) {
        List<RoleDTO> roleDTOList = roleService.queryRoleByAccountId(roleMapper.queryRoleByAccountIdPOToRoleDTO(queryRoleByAccountIdPO));
        return Result.success(roleMapper.roleDTOListToQueryAccountRoleVOList(roleDTOList));
    }
}
