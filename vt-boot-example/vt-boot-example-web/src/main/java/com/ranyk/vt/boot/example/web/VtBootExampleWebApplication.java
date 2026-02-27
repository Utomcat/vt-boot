package com.ranyk.vt.boot.example.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * CLASS_NAME: VtBootExampleWebApplication.java
 *
 * @author ranyk
 * @version V1.0
 * @description: WEB - 样例启动类
 * @date: 2026-02-06
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.ranyk.vt.boot")
public class VtBootExampleWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(VtBootExampleWebApplication.class, args);
    }

}
