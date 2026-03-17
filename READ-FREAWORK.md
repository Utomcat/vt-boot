# 框架使用说明文档

## 一、核心依赖引用
> 使用当前的框架开发业务系统需要引入如下的核心依赖:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.ranyk</groupId>
        <artifactId>vt-boot-example</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <artifactId>业务系统的 artifactId</artifactId>

    <name>业务系统名</name>
    <description>业务系统描述</description>

    <properties>

    </properties>

    <dependencies>
        <!-- SpringBoot 核心依赖 必须引入 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- 单元测试依赖 必须引入 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <artifactId>org.assertj</artifactId>
                    <groupId>assertj-core</groupId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- 测试依赖 必须引入 -->
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <scope>test</scope>
        </dependency>
        
        <!-- 其他当前业务系统重需要使用的依赖 Jar 包, 一般建议在根目录下的 pom.xml 中定义有关 starter 启动类的版本, 此处只进行依赖引用无需进行版本管理 -->
        
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>启动主类的全限定名,如: com.ranyk.vt.boot.example.satoken.VtBootExampleSaTokenApplication </mainClass>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
                <includes>
                    <include>*/**</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>false</filtering>
                <includes>
                    <include>*/**</include>
                </includes>
            </resource>
        </resources>
    </build>

</project>

```

## 二、框架的配置文件
> 框架的配置文件均在 `src/main/resources` 目录下, 文件名均为 `application.yaml` 或 `application.yml`, 但是对应不同插件/第三方的框架配置文件均以不同的文件命名(当下的配置文件说明如下):
1. cache.yaml: 缓存配置
2. datasource.yaml: 数据源配置
3. language.yaml: 语言配置
4. logback.yaml: 日志配置
5. mybatisplus.yaml: MyBatisPlus 配置, <font color="red">**注意: 该文件是需要进行修改的, 属性: `mybatis-plus.type-aliases-package` 必须修改为当前业务系统中所使用的实体类包名,必须配置所有的实体类的全路径包名,多个包名之间用逗号隔开** </font>
6. sa-token.yaml: Sa-Token 配置 
7. tenant.yaml: 租户配置
