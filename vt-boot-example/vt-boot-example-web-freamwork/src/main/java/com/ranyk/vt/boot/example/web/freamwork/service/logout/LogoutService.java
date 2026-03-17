package com.ranyk.vt.boot.example.web.freamwork.service.logout;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: LogoutService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 注销业务逻辑类
 * @date: 2026-03-04
 */
@Slf4j
@Service
public class LogoutService {

    /**
     * 注销业务逻辑
     */
    public void logout() {
        StpUtil.logout(StpUtil.getLoginId());
    }
}
