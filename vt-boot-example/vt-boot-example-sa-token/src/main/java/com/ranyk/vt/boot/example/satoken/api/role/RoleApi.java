package com.ranyk.vt.boot.example.satoken.api.role;

import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.example.satoken.domain.role.dto.RoleDTO;
import com.ranyk.vt.boot.example.satoken.domain.role.po.DeleteRolePO;
import com.ranyk.vt.boot.example.satoken.domain.role.po.QueryRolePO;
import com.ranyk.vt.boot.example.satoken.domain.role.po.SaveRolePO;
import com.ranyk.vt.boot.example.satoken.domain.role.po.UpdateRolePO;
import com.ranyk.vt.boot.example.satoken.domain.role.vo.QueryRoleVO;
import com.ranyk.vt.boot.example.satoken.mapper.role.RoleMapper;
import com.ranyk.vt.boot.example.satoken.service.role.RoleService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 角色信息数据转换接口对象
     */
    private final RoleMapper roleMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入角色信息业务逻辑类对象
     *
     * @param roleService 角色信息业务逻辑类对象
     * @param roleMapper  角色信息数据转换接口对象
     */
    @Autowired
    public RoleApi(RoleService roleService, RoleMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    /**
     * 新增一个角色
     *
     * @param saveRolePO 新增角色请求参数封装 PO 类, {@link SaveRolePO}
     * @return 新增结果 {@link Boolean} , true: 新增成功; false: 新增失败;
     */
    @PostMapping
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
    @Log(operation = "更新一个角色", type = Log.LogType.UPDATE)
    public Result<Boolean> updateRole(@RequestBody UpdateRolePO updateRolePO) {
        roleService.updateOneRole(roleMapper.updateRolePOToRoleDTO(updateRolePO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 查询角色信息
     *
     * @param queryRolePO 查询角色信息请求参数封装 PO 类, {@link QueryRolePO}
     * @return 查询结果 {@link MultiResult} - 查询结果视图对象 {@link QueryRoleVO}
     */
    @GetMapping
    @Log(operation = "查询角色信息", type = Log.LogType.SELECT)
    public MultiResult<QueryRoleVO> queryRole(QueryRolePO queryRolePO) {
        PageResponse<RoleDTO> roleDTOPageResponse = roleService.queryRoleByConditions(roleMapper.queryRolePOToRoleDTO(queryRolePO));
        return MultiResult.successMulti(roleMapper.roleDTOListToQueryRoleVOList(roleDTOPageResponse.getData()),
                Long.parseLong(String.valueOf(roleDTOPageResponse.getTotal())),
                roleDTOPageResponse.getCurrentPage(),
                roleDTOPageResponse.getPageSize());
    }
}
