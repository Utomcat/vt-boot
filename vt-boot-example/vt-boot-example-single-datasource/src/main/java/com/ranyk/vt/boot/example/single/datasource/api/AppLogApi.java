package com.ranyk.vt.boot.example.single.datasource.api;

import cn.hutool.core.bean.BeanUtil;
import com.ranyk.vt.boot.example.single.datasource.domain.dto.AppLogDTO;
import com.ranyk.vt.boot.example.single.datasource.domain.po.AppLogPO;
import com.ranyk.vt.boot.example.single.datasource.domain.vo.AppLogVO;
import com.ranyk.vt.boot.example.single.datasource.service.AppLogService;
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
     * 获取业务日志列表
     *
     * @return 业务日志列表, {@link AppLogVO} 对象 List 集合
     */
    @GetMapping("/list")
    public Result<List<AppLogVO>> list(){
        return Result.success(BeanUtil.copyToList(appLogService.queryList(), AppLogVO.class));
    }

    /**
     * 保存一条业务日志数据
     *
     * @param appLogPO 业务日志数据对象 {@link AppLogPO}
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    @PostMapping
    public Result<Boolean> saveOne(@RequestBody AppLogPO appLogPO){
        return Result.success(appLogService.saveOne(BeanUtil.copyProperties(appLogPO, AppLogDTO.class)));
    }
}
