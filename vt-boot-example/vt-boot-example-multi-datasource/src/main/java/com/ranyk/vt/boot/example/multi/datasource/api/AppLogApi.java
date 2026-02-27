package com.ranyk.vt.boot.example.multi.datasource.api;

import cn.hutool.core.bean.BeanUtil;
import com.ranyk.vt.boot.example.multi.datasource.domain.dto.AppLogDTO;
import com.ranyk.vt.boot.example.multi.datasource.domain.po.AppLogPO;
import com.ranyk.vt.boot.example.multi.datasource.domain.vo.AppLogVO;
import com.ranyk.vt.boot.example.multi.datasource.service.AppLogService;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CLASS_NAME: AppLogApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务日志 API 接口类
 * @date: 2026-02-26
 */
@RestController
@RequestMapping("/api/log")
public class AppLogApi {

    /**
     * 业务日志业务逻辑处理对象
     */
    private final AppLogService appLogService;

    /**
     * 构造方法 - 向 Spring IOC 容器中自动注入业务逻辑处理对象
     *
     * @param appLogService 业务日志业务逻辑处理对象
     */
    @Autowired
    public AppLogApi(AppLogService appLogService) {
        this.appLogService = appLogService;
    }

    /**
     * 保存一条业务日志数据 - 从数据源一中保存数据
     *
     * @param appLogPO 业务日志数据对象 {@link AppLogPO}
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    @PostMapping("/save/one/data/db1")
    public Result<Boolean> saveOneDataDbOne(@RequestBody AppLogPO appLogPO) {
        return Result.success(appLogService.saveOneDataDbOne(BeanUtil.copyProperties(appLogPO, AppLogDTO.class)));
    }

    /**
     * 获取业务日志列表 - 从数据源二中保存数据
     *
     * @param appLogPO 业务日志数据对象 {@link AppLogPO}
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    @PostMapping("/save/one/data/db2")
    public Result<Boolean> saveOneDataDbTwo(@RequestBody AppLogPO appLogPO) {
        return Result.success(appLogService.saveOneDataDbTwo(BeanUtil.copyProperties(appLogPO, AppLogDTO.class)));
    }

    /**
     * 获取业务日志列表 - 从数据源三中保存数据
     *
     * @param appLogPO 业务日志数据对象 {@link AppLogPO}
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    @PostMapping("/save/one/data/db3")
    public Result<Boolean> saveOneDataDbThree(@RequestBody AppLogPO appLogPO) {
        return Result.success(appLogService.saveOneDataDbThree(BeanUtil.copyProperties(appLogPO, AppLogDTO.class)));
    }

    /**
     * 保存一条业务日志数据异常测试, 测试 @DSTransactional 注解的回滚功能
     *
     * @param appLogPO 业务日志数据对象 {@link AppLogPO}
     * @return 批量保存结果, true: 批量保存成功; false: 批量保存失败;
     */
    @PostMapping("/save/one/data/exception/one/db1")
    public Result<Boolean> saveOneDataExceptionOneDbOne(@RequestBody AppLogPO appLogPO) {
        return Result.success(appLogService.saveOneDataExceptionOneDbOne(BeanUtil.copyProperties(appLogPO, AppLogDTO.class)));
    }

    /**
     * 保存一条业务日志数据异常测试, 测试 @Transactional 注解的回滚功能
     *
     * @param appLogPO 业务日志数据对象 {@link AppLogPO}
     * @return 批量保存结果, true: 批量保存成功; false: 批量保存失败;
     */
    @PostMapping("/save/one/data/exception/one/db2")
    public Result<Boolean> saveOneDataExceptionOneDbTwo(@RequestBody AppLogPO appLogPO) {
        return Result.success(appLogService.saveOneDataExceptionOneDbTwo(BeanUtil.copyProperties(appLogPO, AppLogDTO.class)));
    }

    /**
     * 获取业务日志列表 - 从数据源一中查询数据
     *
     * @return 业务日志列表, {@link AppLogVO} 对象 List 集合
     */
    @GetMapping("/lis/db1")
    public Result<List<AppLogVO>> listDbOne() {
        return Result.success(BeanUtil.copyToList(appLogService.queryListDbOne(), AppLogVO.class));
    }

    /**
     * 获取业务日志列表 - 从数据源二中查询数据
     *
     * @return 业务日志列表, {@link AppLogVO} 对象 List 集合
     */
    @GetMapping("/lis/db2")
    public Result<List<AppLogVO>> listDbTwo() {
        return Result.success(BeanUtil.copyToList(appLogService.queryListDbTwo(), AppLogVO.class));
    }

    /**
     * 获取业务日志列表 - 从数据源三中查询数据
     *
     * @return 业务日志列表, {@link AppLogVO} 对象 List 集合
     */
    @GetMapping("/lis/db3")
    public Result<List<AppLogVO>> listDbThree() {
        return Result.success(BeanUtil.copyToList(appLogService.queryListDbThird(), AppLogVO.class));
    }
}
