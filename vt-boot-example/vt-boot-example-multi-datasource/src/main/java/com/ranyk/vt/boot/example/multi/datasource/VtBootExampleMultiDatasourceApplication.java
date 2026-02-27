package com.ranyk.vt.boot.example.multi.datasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * CLASS_NAME: VtBootExampleMultiDatasourceApplication.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 多数据源样例启动类
 * @date: 2026-02-06
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.ranyk.vt.boot")
@MapperScan(basePackages = "com.ranyk.vt.boot.example.multi.datasource.repository")
public class VtBootExampleMultiDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VtBootExampleMultiDatasourceApplication.class, args);
    }

}
