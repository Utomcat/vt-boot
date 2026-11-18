# 快速开始

本文介绍如何在本地完成 `vt-boot` 的环境准备、构建、运行与验证。

## 一、环境要求

| 项目 | 要求 | 说明 |
|------|------|------|
| JDK | **21 及以上** | 框架依赖虚拟线程 API（`Executors.newVirtualThreadPerTaskExecutor` / `Thread.ofVirtual`），不可降级为 17 |
| Maven | **3.6.3 及以上**（推荐 3.9.9+） | 父 POM 通过 `maven-enforcer-plugin` 强制校验，环境不符会直接构建失败 |
| 数据库 | MySQL 8+ / MariaDB | 示例工程默认使用 MySQL，需可访问的实例 |
| 操作系统 | 不限 | 已在 Windows 环境验证 |

> 若构建时出现 `本框架依赖 JDK 21 虚拟线程, 请使用 JDK 21 或更高版本进行构建`，说明当前 `JAVA_HOME` 指向的 JDK 版本过低，请切换后重试。

## 二、获取源码

```bash
git clone git@github.com:Utomcat/vt-boot.git
cd vt-boot
```

## 三、构建

项目为 Maven 聚合多模块工程，需先在根目录执行安装，将各 Starter 安装到本地仓库后再运行示例应用。

```bash
# 全量构建并安装到本地仓库（跳过测试）
mvn clean install -DskipTests
```

如需一并执行代码风格检查：

```bash
mvn clean install -DskipTests && mvn checkstyle:check -Pcheckstyle
```

> Checkstyle 通过 `-Pcheckstyle` Profile 触发，默认构建不会执行，命中规则只记录不阻断（`failOnViolation=false`）。

## 四、准备数据库

示例应用 `vt-boot-example-admin` 需要存在数据库及对应表。建表语句与字段说明见 [数据库设计](./database.md)。

## 五、配置数据源

示例应用的数据源配置位于 `vt-boot-example/vt-boot-example-admin/src/main/resources/datasource.yaml`，使用 `dynamic-datasource` 结构，默认数据源名为 `master`：

```yaml
spring:
  datasource:
    dynamic:
      primary: master
      strict: false
      datasource:
        master:
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://127.0.0.1:3306/vt_boot_4?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true
          username: your_username
          password: your_password
```

请将 `url` / `username` / `password` 修改为本地环境的真实值。完整配置项说明见 [配置说明](./configuration.md)。

## 六、运行

### 方式一：Maven 直接运行

```bash
cd vt-boot-example/vt-boot-example-admin
mvn spring-boot:run
```

### 方式二：运行打包产物

```bash
# 在根目录或示例模块目录执行打包
mvn -pl vt-boot-example/vt-boot-example-admin -am package -DskipTests

java -jar vt-boot-example/vt-boot-example-admin/target/vt-boot-example-admin-0.0.1-SNAPSHOT.jar
```

示例应用默认端口为 **9086**，默认激活 `local` Profile。

## 七、验证

| 目标 | 地址 |
|------|------|
| Swagger UI | `http://localhost:9086/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:9086/v3/api-docs` |
| 登录接口 | `POST http://localhost:9086/api/auth/login` |

调用登录接口示例（登录、刷新接口已在 `sa-token.yaml` 的排除路径中，无需鉴权）：

```bash
curl -X POST http://localhost:9086/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

返回的 `token` 即访问令牌，后续请求需在请求头携带（默认头名 `Authorization`）：

```bash
curl http://localhost:9086/api/auth/me \
  -H "Authorization: <上一步返回的 token>"
```

完整接口清单见 [API 接口](./api.md)。

## 八、在自己的项目中使用

`vt-boot` 的 Starter 已安装到本地仓库后，在业务工程中按需引入即可：

```xml
<dependency>
    <groupId>com.ranyk</groupId>
    <artifactId>vt-boot-web-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.ranyk</groupId>
    <artifactId>vt-boot-satoken-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

各模块职责与关键能力见 [模块说明](./modules.md)。

## 九、常见问题

构建失败、启动报错、依赖冲突等高频问题见 [常见问题](./faq.md)。
