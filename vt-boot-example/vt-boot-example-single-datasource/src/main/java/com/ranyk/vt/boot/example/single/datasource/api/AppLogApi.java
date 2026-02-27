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

    private final AppLogService appLogService;

    @Autowired
    public AppLogApi(AppLogService appLogService) {
        this.appLogService = appLogService;
    }

    @GetMapping("/list")
    public Result<List<AppLogVO>> list(){
        return Result.success(BeanUtil.copyToList(appLogService.queryList(), AppLogVO.class));
    }

    @PostMapping
    public Result<Boolean> saveOne(@RequestBody AppLogPO appLogPO){
        return Result.success(appLogService.saveOne(BeanUtil.copyProperties(appLogPO, AppLogDTO.class)));
    }
}
