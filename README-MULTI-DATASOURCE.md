# 多数据源配置说明
[TOC]

## 单数据源配置
### 单数据源的配置文件配置
> 就是使用 Spring Boot 默认的标准数据源配置, 只需要通过 spring.datasource 配置进行数据配置, 配置属性如下:
```yaml
spring:
  datasource:
    dynamic:
      # 是否启用动态数据源，默认true
      enabled: false
    # 数据库驱动类名
    driverClassName: org.mariadb.jdbc.Driver
    # 数据库连接URL
    url: jdbc:mariadb://IP地址:端口/数据库名?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useLegacyDatetimeCode=false&cachePrepStmts=true&useServerPrepStmts=true
    # 数据库连接用户名
    username: 用户名
    # 数据库连接密码
    password: 密码
    # 数据库连接池配置
    hikari:
      # 数据库连接超时时间（毫秒）
      connection-timeout: 600000
      # 数据库连接池最大连接数
      maximum-pool-size: 10
      # 数据库连接池最小空闲连接数
      minimum-idle: 5
      # 数据库连接池最大空闲时间（毫秒）
      idle-timeout: 500000
```
### 在项目的启动类上添加如下注解
```java
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

```

## 多数据源配置
### 多数据源的配置文件配置
> 使用 Spring Boot 提供的动态数据源配置, 可以通过 spring.datasource.dynamic 配置进行多数据源配置, 配置属性如下:
```yaml
spring:
  datasource:
    dynamic:
      # 启用动态数据源，默认true
      enabled: true
      # 设置默认的数据源或者数据源组,默认值即为master
      primary: master
      # 严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源
      strict: false
      # 是否优雅关闭数据源，默认为false，设置为true时，关闭数据源时如果数据源中还存在活跃连接，至多等待10s后强制关闭
      grace-destroy: false
      # 数据源配置
      datasource:
        # db 名为 master 的数据源配置
        master:
          url: jdbc:mariadb://IP地址:端口/数据库名?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useLegacyDatetimeCode=false&cachePrepStmts=true&useServerPrepStmts=true
          username: root
          password: 123456
          # 3.2.0 开始支持 SPI 可省略此配置
          driver-class-name: org.mariadb.jdbc.Driver 
        # db 名为 slave_1 的数据源配置
        slave_1:
          url: jdbc:mariadb://IP地址:端口/数据库名?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useLegacyDatetimeCode=false&cachePrepStmts=true&useServerPrepStmts=true
          username: root
          password: 123456
          driver-class-name: org.mariadb.jdbc.Driver
        # db 名为 slave_2 的数据源配置
        slave_2:
          # 内置加密,使用请查看详细文档
          url: ENC(url加密字符串) 
          username: ENC(用户名加密字符串)
          password: ENC(密码加密字符串)
          driver-class-name: org.mariadb.jdbc.Driver
        #......省略
        #以上会配置一个默认库master，一个组slave下有两个子库slave_1,slave_2
```
### 在项目的启动类上添加如下注解
```java
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
// 多数据源配置需要扫描的 Mapper 接口所在的包
@MapperScan(basePackages = "com.ranyk.vt.boot.example.multi.datasource.repository")
public class VtBootExampleMultiDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VtBootExampleMultiDatasourceApplication.class, args);
    }

}
```