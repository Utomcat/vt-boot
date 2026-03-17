package com.ranyk.vt.boot.example.web.freamwork.api.logout;

import com.ranyk.vt.boot.example.web.freamwork.service.logout.LogoutService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: LogoutApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 系统注销接口
 * @date: 2026-03-04
 */
@RestController
@RequestMapping("/api/logout")
public class LogoutApi {

    /**
     * 注销业务逻辑类对象
     */
    private final LogoutService logoutService;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入 LogoutService 对象
     *
     * @param logoutService 注销业务逻辑类对象 {@link LogoutService}
     */
    @Autowired
    public LogoutApi(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    /**
     * 系统注销接口
     *
     * @return 注销结果
     */
    @GetMapping
    @Log(operation = "系统注销", type = Log.LogType.LOGOUT)
    public Result<Boolean> logout() {
        logoutService.logout();
        return Result.success(Boolean.TRUE);
    }
}
