package com.ranyk.vt.boot.example.multi.datasource.api;

import cn.hutool.core.bean.BeanUtil;
import com.ranyk.vt.boot.example.multi.datasource.domain.vo.AppLogVO;
import com.ranyk.vt.boot.example.multi.datasource.service.AppLogService;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/lis/db1")
    public Result<List<AppLogVO>> listDbOne(){
        return Result.success(BeanUtil.copyToList(appLogService.queryListDbOne(), AppLogVO.class));
    }

    @GetMapping("/lis/db2")
    public Result<List<AppLogVO>> listDbTwo(){
        return Result.success(BeanUtil.copyToList(appLogService.queryListDbTwo(), AppLogVO.class));
    }

    @GetMapping("/lis/db3")
    public Result<List<AppLogVO>> listDbThree(){
        return Result.success(BeanUtil.copyToList(appLogService.queryListDbThird(), AppLogVO.class));
    }
}
