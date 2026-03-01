package com.ranyk.vt.boot.example.satoken.api.test;

import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: ExampleApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 测试请求 API 接口
 * @date: 2026-02-28
 */
@RestController
@RequestMapping("/api/example")
public class ExampleApi {


    /**
     * 测试方法
     *
     * @return 测试结果
     */
    @GetMapping("/hello")
    @Log(operation = "示例 API 接口类 - 测试方法", type = Log.LogType.ACCESS)
    public Result<String> hello() {
        return Result.success("Hello, World! from Virtual Thread: " + Thread.currentThread());
    }
}
