package com.ranyk.vt.boot.example.satoken.api.permissions;

import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.example.satoken.domain.permissions.dto.PermissionDTO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.po.DeletePermissionPO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.po.QueryPermissionPO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.po.SavePermissionPO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.po.UpdatePermissionPO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.vo.QueryPermissionVO;
import com.ranyk.vt.boot.example.satoken.mapper.permissions.PermissionMapper;
import com.ranyk.vt.boot.example.satoken.service.permissions.PermissionService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CLASS_NAME: PermissionApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 权限请求接口API
 * @date: 2026-03-05
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionApi {

    /**
     * 权限信息业务逻辑类对象
     */
    private final PermissionService permissionService;
    /**
     * 权限信息数据转换接口对象
     */
    private final PermissionMapper permissionMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入权限信息业务逻辑类对象
     *
     * @param permissionService 权限信息业务逻辑类对象
     * @param permissionMapper  权限信息数据转换接口对象
     */
    @Autowired
    public PermissionApi(PermissionService permissionService, PermissionMapper permissionMapper) {
        this.permissionService = permissionService;
        this.permissionMapper = permissionMapper;
    }

    /**
     * 新增一条权限信息
     *
     * @param savePermissionPO 新增权限请求参数封装 PO 类对象 {@link SavePermissionPO}
     * @return 返回新增结果 {@link Boolean} , true: 表示新增成功; false: 表示新增失败;
     */
    @PostMapping
    @Log(operation = "新增一条权限信息", type = Log.LogType.INSERT)
    public Result<Boolean> savePermission(@RequestBody SavePermissionPO savePermissionPO) {
        permissionService.saveOnePermission(permissionMapper.savePermissionPOToPermissionDTO(savePermissionPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 删除一条权限信息
     *
     * @param deletePermissionPO 删除权限请求参数封装 PO 类对象 {@link DeletePermissionPO}
     * @return 删除结果 {@link Boolean} , true: 删除成功; false: 删除失败;
     */
    @DeleteMapping
    @Log(operation = "删除一条权限信息", type = Log.LogType.DELETE)
    public Result<Boolean> deletePermission(@RequestBody DeletePermissionPO deletePermissionPO) {
        permissionService.deleteOnePermission(permissionMapper.deletePermissionPOToPermissionDTO(deletePermissionPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 修改一条权限信息
     *
     * @param updatePermissionPO 修改权限请求参数封装 PO 类对象 {@link UpdatePermissionPO}
     * @return 修改结果 {@link Boolean} , true: 修改成功; false: 修改失败;
     */
    @PutMapping
    @Log(operation = "修改一条权限信息", type = Log.LogType.UPDATE)
    public Result<Boolean> updatePermission(@RequestBody UpdatePermissionPO updatePermissionPO) {
        permissionService.updateOnePermission(permissionMapper.updatePermissionPOToPermissionDTO(updatePermissionPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 查询权限信息 - 分页
     *
     * @param queryPermissionPO 查询权限请求参数封装 PO 类对象 {@link QueryPermissionPO}
     * @return 查询结果 {@link MultiResult} - 权限信息数据转换后的 VO 类对象 {@link QueryPermissionVO}
     */
    @GetMapping
    @Log(operation = "查询权限信息 - 分页", type = Log.LogType.SELECT)
    public MultiResult<QueryPermissionVO> queryPermission(QueryPermissionPO queryPermissionPO) {
        PageResponse<PermissionDTO> pageResponse = permissionService.queryPermissionByConditions(permissionMapper.queryPermissionPoToPermissionDTO(queryPermissionPO));
        return MultiResult.successMulti(permissionMapper.permissionDTOListToQueryPermissionVOList(pageResponse.getData()),
                Long.parseLong(String.valueOf(pageResponse.getTotal())),
                pageResponse.getCurrentPage(),
                pageResponse.getPageSize());
    }
}
