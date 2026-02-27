package com.ranyk.vt.boot.example.single.datasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * CLASS_NAME: VtBootExampleSingleDatasourceApplication.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 单数据源样例启动类
 * @date: 2026-02-06
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.ranyk.vt.boot")
@MapperScan(basePackages = "com.ranyk.vt.boot.example.single.datasource.repository")
public class VtBootExampleSingleDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VtBootExampleSingleDatasourceApplication.class, args);
    }

}
