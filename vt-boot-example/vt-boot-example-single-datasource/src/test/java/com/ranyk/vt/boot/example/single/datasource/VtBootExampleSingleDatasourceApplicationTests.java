package com.ranyk.vt.boot.example.single.datasource;

import com.baomidou.mybatisplus.core.toolkit.AES;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * CLASS_NAME: VtBootExampleSingleDatasourceApplicationTests.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 单数据源 - 样例测试启动类
 * @date: 2026-02-06
 */
@Slf4j
@SpringBootTest
class VtBootExampleSingleDatasourceApplicationTests {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    /**
     * 测试单元测试是否成功
     */
    @Test
    void contextLoads() {
        System.out.println("Hello World - Single Datasource");
    }

    /**
     * 使用  com.baomidou.mybatisplus.core.toolkit.AES 加密 数据库连接URL, 用户名, 密码
     */
    @Test
    void test0(){
        // 生成16位随机AES密钥
        String randomKey = AES.generateRandomKey();
        // 使用随机密钥加密 数据库连接URL
        String url = AES.encrypt(dataSourceProperties.getUrl(), randomKey);
        String username = AES.encrypt(dataSourceProperties.getUsername(), randomKey);
        String password = AES.encrypt(dataSourceProperties.getPassword(), randomKey);
        // --mpw.key=QTyyMQjUUQ21zmlQ
        log.debug("数据库连接密钥: {}", randomKey);
        // mpw:m5K8pL8AKhiWJ6VokE1QKcd4ljLJ7W3GpSS3ncOkczDl3gip2OeSCB3inQpXTb6RIf/OhXr1/U4/kkZWI0aaEDEWqsd1EeI38dnJkzH0ofoOizKtc3WN1RP0Q7ponZXdd4s/mjabHjCAiGZuL0MYav1q+vDuJ7/kqOmr84NyzsEb+6soASKDL7tdKn7xBMS0xoSoigSBxRwOjnFFrVGTQkBfB7aGHjF9IpfKN7j8vTM=
        log.debug("数据库连接URL: {}", url);
        // mpw:n/TcDVePDPzY9AL6tDQKcw==
        log.debug("数据库用户名: {}", username);
        // mpw:brbYC5TFDvfJ6CL94F2xEw==
        log.debug("数据库密码: {}", password);

    }

    /**
     * 使用 com.baomidou.mybatisplus.core.toolkit.AES 解密 数据库连接 URL
     */
    @Test
    void test1(){
        String url = AES.decrypt("m5K8pL8AKhiWJ6VokE1QKcd4ljLJ7W3GpSS3ncOkczDl3gip2OeSCB3inQpXTb6RIf/OhXr1/U4/kkZWI0aaEDEWqsd1EeI38dnJkzH0ofoOizKtc3WN1RP0Q7ponZXdd4s/mjabHjCAiGZuL0MYav1q+vDuJ7/kqOmr84NyzsEb+6soASKDL7tdKn7xBMS0xoSoigSBxRwOjnFFrVGTQkBfB7aGHjF9IpfKN7j8vTM=", "QTyyMQjUUQ21zmlQ");
        log.debug("解密后的数据库连接URL: {}", url);
    }
}
