package com.ranyk.vt.boot.example.web.freamwork;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ClassName: VtBootExampleWebFrameworkApplicationTests.java
 *
 * @author ranyk
 * @version v1.0
 * @description: 测试 Web 框架应用, 单元测试类
 * @date: 2026-04-06
 */
@Slf4j
@SpringBootTest
class VtBootExampleWebFrameworkApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 测试使用 Hutool 工具生成 UUID
     */
    @Test
    @DisplayName("测试使用 Hutool 工具生成 UUID")
    void generateUUID() {
        // 使用 hutool 生成 UUID
        log.info("使用 fastSimpleUUID 方式生成 UUID: {}", IdUtil.fastSimpleUUID());
        // 使用 snowflake 生成 UUID
        log.info("使用 snowflake 方式生成 UUID: {}", IdUtil.getSnowflake().nextIdStr());
    }

}
