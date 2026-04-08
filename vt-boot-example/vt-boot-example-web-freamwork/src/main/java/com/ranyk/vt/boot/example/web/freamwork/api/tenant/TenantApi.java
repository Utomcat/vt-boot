package com.ranyk.vt.boot.example.web.freamwork.api.tenant;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.dto.TenantDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po.DeleteTenantPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po.QueryTenantPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po.SaveTenantPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po.UpdateTenantPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.vo.QueryTenantVO;
import com.ranyk.vt.boot.example.web.freamwork.mapper.tenant.TenantMapper;
import com.ranyk.vt.boot.example.web.freamwork.service.tenant.TenantService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CLASS_NAME: TenantApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户信息API
 * @date: 2026-04-06
 */
@RestController
@RequestMapping("/api/tenant")
public class TenantApi {

    /**
     * 租户信息业务逻辑类对象
     */
    private final TenantService tenantService;
    /**
     * 租户信息数据转换接口对象
     */
    private final TenantMapper tenantMapper;

    /**
     * 构造函数 - 向 Spring 容器中注册租户信息业务逻辑类对象
     *
     * @param tenantService 租户信息业务逻辑类对象
     * @param tenantMapper  租户信息数据转换接口对象
     */
    @Autowired
    public TenantApi(TenantService tenantService, TenantMapper tenantMapper) {
        this.tenantService = tenantService;
        this.tenantMapper = tenantMapper;
    }

    /**
     * 新增一个租户
     *
     * @param saveTenantPO 新增租户请求参数封装 PO 类, {@link SaveTenantPO}
     * @return 新增结果 {@link Boolean} , true: 新增成功; false: 新增失败;
     */
    @PostMapping
    @SaCheckRole(value = {"super:admin"})
    @Log(operation = "新增一个租户", type = Log.LogType.INSERT)
    public Result<Boolean> saveTenant(@RequestBody SaveTenantPO saveTenantPO) {
        tenantService.saveOneTenant(tenantMapper.saveTenantPOToTenantDTO(saveTenantPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 删除一个租户
     *
     * @param deleteTenantPO 删除租户请求参数封装 PO 类, {@link DeleteTenantPO}
     * @return 删除结果 {@link Boolean} , true: 删除成功; false: 删除失败;
     */
    @DeleteMapping
    @SaCheckRole(value = {"super:admin"})
    @Log(operation = "删除一个租户", type = Log.LogType.DELETE)
    public Result<Boolean> deleteTenant(@RequestBody DeleteTenantPO deleteTenantPO) {
        tenantService.deleteOneTenant(tenantMapper.deleteTenantPOToTenantDTO(deleteTenantPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 更新一个租户
     *
     * @param updateTenantPO 更新租户请求参数封装 PO 类, {@link UpdateTenantPO}
     * @return 更新结果 {@link Boolean} , true: 更新成功; false: 更新失败;
     */
    @PutMapping
    @SaCheckRole(value = {"super:admin"})
    @Log(operation = "更新一个租户", type = Log.LogType.UPDATE)
    public Result<Boolean> updateTenant(@RequestBody UpdateTenantPO updateTenantPO) {
        tenantService.updateOneTenant(tenantMapper.updateTenantPOToTenantDTO(updateTenantPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 查询租户信息 - 分页
     *
     * @param queryTenantPO 查询租户信息请求参数封装 PO 类, {@link QueryTenantPO}
     * @return 查询结果 {@link MultiResult} - 查询结果视图对象 {@link QueryTenantVO}
     */
    @GetMapping
    @SaCheckRole(value = {"super:admin"})
    @Log(operation = "查询租户信息 - 分页", type = Log.LogType.SELECT)
    public MultiResult<QueryTenantVO> queryTenant(QueryTenantPO queryTenantPO) {
        PageResponse<TenantDTO> tenantDTOPageResponse = tenantService.queryTenantByConditions(tenantMapper.queryTenantPOToTenantDTO(queryTenantPO));
        return MultiResult.successMulti(tenantMapper.tenantDTOListToQueryTenantVOList(tenantDTOPageResponse.getData()),
                Long.parseLong(String.valueOf(tenantDTOPageResponse.getTotal())),
                tenantDTOPageResponse.getCurrentPage(),
                tenantDTOPageResponse.getPageSize());
    }



}
