package com.ranyk.vt.boot.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * CLASS_NAME: VtBootExampleApplication.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 样例启动类
 * @date: 2026-02-06
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.ranyk.vt.boot")
public class VtBootExampleApplication {

    /**
     * 启动方法
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(VtBootExampleApplication.class, args);
    }

}
