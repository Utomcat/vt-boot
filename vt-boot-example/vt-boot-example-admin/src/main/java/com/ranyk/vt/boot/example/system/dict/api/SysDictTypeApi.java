package com.ranyk.vt.boot.example.system.dict.api;

import com.ranyk.vt.boot.example.system.dict.converter.SysDictTypeConverter;
import com.ranyk.vt.boot.example.system.dict.domain.po.DictTypeFormPO;
import com.ranyk.vt.boot.example.system.dict.domain.po.DictTypeQueryPO;
import com.ranyk.vt.boot.example.system.dict.domain.vo.DictTypeVO;
import com.ranyk.vt.boot.example.system.dict.service.SysDictTypeService;
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
 * CLASS_NAME: SysDictTypeApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型管理接口, 提供字典类型的分页查询、详情、增删改与启停操作
 * @date: 2026-11-20
 */
@RestController
@RequestMapping("/api/system/dict/type")
@RequiredArgsConstructor
public class SysDictTypeApi {

    /**
     * 字典类型管理服务
     */
    private final SysDictTypeService sysDictTypeService;
    /**
     * 字典类型入参转换器
     */
    private final SysDictTypeConverter sysDictTypeConverter;

    /**
     * 分页查询字典类型列表。要求 system:dict:list 权限
     *
     * @param queryPO 分页与筛选参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public MultiResult<DictTypeVO> page(@RequestBody DictTypeQueryPO queryPO) {
        SecurityUtils.requirePermission("system:dict:list");
        return sysDictTypeService.page(sysDictTypeConverter.toQueryDTO(queryPO));
    }

    /**
     * 查询全部启用字典类型的精简列表, 供字典数据页面选择所属类型使用。仅要求登录。
     *
     * @return 字典类型列表
     */
    @GetMapping("/options")
    public Result<List<DictTypeVO>> options() {
        return Result.success(sysDictTypeService.options());
    }

    /**
     * 根据ID查询字典类型详情。要求 system:dict:list 权限
     *
     * @param id 字典类型ID
     * @return 字典类型详情
     */
    @GetMapping("/{id}")
    public Result<DictTypeVO> detail(@PathVariable Long id) {
        SecurityUtils.requirePermission("system:dict:list");
        return Result.success(sysDictTypeService.detail(id));
    }

    /**
     * 新增字典类型。要求 system:dict:add 权限
     *
     * @param formPO 字典类型表单参数
     * @return 新增字典类型ID
     */
    @PostMapping
    public Result<Long> create(@RequestBody DictTypeFormPO formPO) {
        SecurityUtils.requirePermission("system:dict:add");
        return Result.success(sysDictTypeService.create(sysDictTypeConverter.toFormDTO(formPO)));
    }

    /**
     * 修改字典类型。要求 system:dict:edit 权限
     *
     * @param formPO 字典类型表单参数, 必须携带ID
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody DictTypeFormPO formPO) {
        SecurityUtils.requirePermission("system:dict:edit");
        sysDictTypeService.update(sysDictTypeConverter.toFormDTO(formPO));
        return Result.success(null);
    }

    /**
     * 逻辑删除字典类型。要求 system:dict:delete 权限
     *
     * @param id 字典类型ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        SecurityUtils.requirePermission("system:dict:delete");
        sysDictTypeService.delete(id);
        return Result.success(null);
    }

    /**
     * 启用或禁用字典类型。要求 system:dict:status 权限
     *
     * @param id     字典类型ID
     * @param status 目标状态: 1-正常 0-禁用
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        SecurityUtils.requirePermission("system:dict:status");
        sysDictTypeService.changeStatus(id, status);
        return Result.success(null);
    }
}
