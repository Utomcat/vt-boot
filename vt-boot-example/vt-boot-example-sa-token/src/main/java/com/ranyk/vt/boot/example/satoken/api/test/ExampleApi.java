package com.ranyk.vt.boot.example.satoken.api.test;

import cn.dev33.satoken.stp.StpUtil;
import com.ranyk.vt.boot.base.constant.TenantEnum;
import com.ranyk.vt.boot.example.satoken.service.test.ExampleService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 测试业务逻辑类对象
     */
    private final ExampleService exampleService;

    /**
     * 构造方法 - 向 Spring IOC 容器中注入测试业务逻辑类对象
     *
     * @param exampleService 测试业务逻辑类对象
     */
    @Autowired
    public ExampleApi(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    /**
     * 测试方法
     *
     * @return 测试结果
     */
    @GetMapping("/hello")
    @Log(operation = "示例 API 接口类 - 测试方法", type = Log.LogType.ACCESS)
    public Result<String> hello() {
        exampleService.hello();
        return Result.success("Hello, World! from Virtual Thread: " + Thread.currentThread());
    }

    /**
     * 测试获取租户 ID
     *
     * @return 租户 ID 信息
     */
    @GetMapping("/tenant-info")
    public Result<Object> testTenantInfo() {
        // 从 Session 中获取租户 ID
        Object tenantId = StpUtil.getSession().get(TenantEnum.TENANT_ID.getValue());
        System.out.println("========== Test Tenant Info ==========");
        System.out.println("Tenant ID from Session: " + tenantId);
        System.out.println("Is Login: " + StpUtil.isLogin());
        System.out.println("Login ID: " + StpUtil.getLoginId());
        System.out.println("======================================");
        return Result.success(tenantId);
    }
}
