package com.ranyk.vt.boot.example.system.dict.api;

import com.ranyk.vt.boot.example.system.dict.converter.SysDictDataConverter;
import com.ranyk.vt.boot.example.system.dict.domain.po.DictDataFormPO;
import com.ranyk.vt.boot.example.system.dict.domain.po.DictDataQueryPO;
import com.ranyk.vt.boot.example.system.dict.domain.vo.DictDataVO;
import com.ranyk.vt.boot.example.system.dict.service.SysDictDataService;
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
 * CLASS_NAME: SysDictDataApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典数据管理接口, 提供字典数据的分页查询、按类型查询、详情、增删改与启停操作
 * @date: 2026-11-20
 */
@RestController
@RequestMapping("/api/system/dict/data")
@RequiredArgsConstructor
public class SysDictDataApi {

    /**
     * 字典数据管理服务
     */
    private final SysDictDataService sysDictDataService;
    /**
     * 字典数据入参转换器
     */
    private final SysDictDataConverter sysDictDataConverter;

    /**
     * 分页查询字典数据列表。要求 system:dict:list 权限
     *
     * @param queryPO 分页与筛选参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public MultiResult<DictDataVO> page(@RequestBody DictDataQueryPO queryPO) {
        SecurityUtils.requirePermission("system:dict:list");
        return sysDictDataService.page(sysDictDataConverter.toQueryDTO(queryPO));
    }

    /**
     * 按字典类型编码查询全部启用的字典数据, 供业务端读取字典使用(当前无缓存)。仅要求登录。
     *
     * @param dictType 字典类型编码
     * @return 字典数据列表
     */
    @GetMapping("/type/{dictType}")
    public Result<List<DictDataVO>> listByType(@PathVariable String dictType) {
        return Result.success(sysDictDataService.listByType(dictType));
    }

    /**
     * 根据ID查询字典数据详情。要求 system:dict:list 权限
     *
     * @param id 字典数据ID
     * @return 字典数据详情
     */
    @GetMapping("/{id}")
    public Result<DictDataVO> detail(@PathVariable Long id) {
        SecurityUtils.requirePermission("system:dict:list");
        return Result.success(sysDictDataService.detail(id));
    }

    /**
     * 新增字典数据。要求 system:dict:add 权限
     *
     * @param formPO 字典数据表单参数
     * @return 新增字典数据ID
     */
    @PostMapping
    public Result<Long> create(@RequestBody DictDataFormPO formPO) {
        SecurityUtils.requirePermission("system:dict:add");
        return Result.success(sysDictDataService.create(sysDictDataConverter.toFormDTO(formPO)));
    }

    /**
     * 修改字典数据。要求 system:dict:edit 权限
     *
     * @param formPO 字典数据表单参数, 必须携带ID
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody DictDataFormPO formPO) {
        SecurityUtils.requirePermission("system:dict:edit");
        sysDictDataService.update(sysDictDataConverter.toFormDTO(formPO));
        return Result.success(null);
    }

    /**
     * 逻辑删除字典数据。要求 system:dict:delete 权限
     *
     * @param id 字典数据ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        SecurityUtils.requirePermission("system:dict:delete");
        sysDictDataService.delete(id);
        return Result.success(null);
    }

    /**
     * 启用或禁用字典数据。要求 system:dict:status 权限
     *
     * @param id     字典数据ID
     * @param status 目标状态: 1-正常 0-禁用
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        SecurityUtils.requirePermission("system:dict:status");
        sysDictDataService.changeStatus(id, status);
        return Result.success(null);
    }
}
