package com.ranyk.vt.boot.example.satoken.service.test;

import com.ranyk.vt.boot.base.context.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: ExampleService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 测试业务逻辑类
 * @date: 2026-03-04
 */
@Slf4j
@Service
public class ExampleService {

    /**
     * 测试方法
     */
    public void hello() {
        log.debug("当前会话的租户 ID : {}", TenantContext.getTenantId());
    }
}
