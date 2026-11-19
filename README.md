# vt-boot

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?logo=springboot&logoColor=white)
![Spring Framework](https://img.shields.io/badge/Spring%20Framework-7.0.10-6DB33F?logo=spring&logoColor=white)
![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5.17-000000)
![Sa-Token](https://img.shields.io/badge/Sa--Token-1.46.0-FF6B6B)
![Maven](https://img.shields.io/badge/Maven-3.6.3%2B-C71A36?logo=apachemaven&logoColor=white)
![Spring AI](https://img.shields.io/badge/Spring%20AI-2.0.0-6DB33F?logo=spring&logoColor=white)
![License](https://img.shields.io/badge/License-Apache%202.0-green)

以 **Java 虚拟线程（Virtual Threads）** 为核心设计理念、基于 **Spring Boot 4 + JDK 21** 的现代化单体后端脚手架。

坚持 **同步编程模型**，通过全链路虚拟线程获得接近异步/Reactive 的并发能力，让你无需引入 WebFlux 的心智负担，也能构建高吞吐、高并发的后端应用。

- **作者**：ranyk
- **项目状态**：🚧 开发中
- **最后更新**：2026-11-17

---

## 📚 文档导航

> 根目录 `README.md` 仅作为总览入口，其余详细说明均位于 [`doc/`](./doc) 目录下。

### 入门

| 文档 | 说明 |
|------|------|
| [快速开始](./doc/quickstart.md) | 环境要求、构建、运行示例、验证接口 |
| [Docker 部署](../deploy/docker/README.md) | 后端/前端镜像构建、Compose 编排、环境变量与运维命令 |
| [架构设计](./doc/architecture.md) | 模块划分、分层约定、请求链路、虚拟线程模型 |
| [模块说明](./doc/modules.md) | 全部 Starter 的职责、关键类与配置 |

### 使用

| 文档 | 说明 |
|------|------|
| [配置说明](./doc/configuration.md) | 各配置文件拆分方式与全部可配置项 |
| [API 接口](./doc/api.md) | 示例工程 `vt-boot-example-admin` 的 REST 接口 |
| [数据库设计](./doc/database.md) | 示例工程涉及的数据库表结构说明 |

### 规范与维护

| 文档 | 说明 |
|------|------|
| [编码规范](./doc/CODING-STANDARDS.md) | 命名、注释、Lombok、异常、日志、自动配置等强制约束 |
| [常见问题](./doc/faq.md) | 构建、启动、依赖、配置等高频问题排查 |

---

## 📖 项目概述

`vt-boot` 是一个 **Starter 化、模块化** 的 Spring Boot 后端脚手架，由两个聚合工程组成：

- `vt-boot-starter`：一组可独立引入的能力模块（`aop`、`web`、`datasource`、`cache`、`log`、`mq`、`satoken`、`openapi` 等），遵循 **用什么引什么** 的原则，互不强耦合。
- `vt-boot-example`：示例应用集合，当前的 `vt-boot-example-admin` 演示了认证、用户、租户、登录日志等一套完整的后台能力落地方式。

框架只面向 **Java 21+ 新项目**，不追求对老系统兼容。

## ✨ 核心特性

- 🚀 **虚拟线程优先**：Web / 异步 / 定时全链路虚拟线程，官方支持、无 hack。
- 🧱 **现代化技术栈**：Spring Boot 4.1.2、Spring Framework 7.0.10、Jakarta EE、Record / Sealed Class / Pattern Matching。
- 🧠 **同步代码，异步级性能**：保持传统 MVC 编程体验，无需 Reactive 心智负担。
- 🧩 **Starter 化设计**：能力按模块拆分，按需引入，自动配置开箱即用。
- 🏗️ **单体优先、工程化拉满**：适合新项目 / 中小型系统 / 创业项目，非 Spring Cloud 微服务方案。
- 🔐 **内置后台骨架**：Sa-Token 鉴权、多租户、统一响应体、全局异常、操作日志、国际化、OpenAPI 一应俱全。
- ⏱️ **定时任务持久化**：Quartz 任务与触发器默认落库到 `QRTZ_*`，重启不丢失，可平滑切换为集群模式。
- 🤖 **AI 能力预留**：统一依赖 Spring AI 2.0.0 与 Spring AI Alibaba，便于后续接入大模型与向量检索。

## ❌ vt-boot 不做什么

- ❌ 不是 WebFlux / Reactive 框架
- ❌ 不兼容 Spring Boot 2.x / JDK 8
- ❌ 不用于老项目升级
- ❌ 不强推微服务架构

## 🛠️ 技术栈

| 类别 | 技术 / 版本 |
|------|-------------|
| JDK | Java 21（虚拟线程） |
| 框架 | Spring Boot 4.1.2、Spring Framework 7.0.10 |
| 构建 | Maven 聚合多模块（要求 ≥ 3.6.3，推荐 ≥ 3.9.9） |
| ORM | MyBatis-Plus 3.5.17 + dynamic-datasource 4.6.0 |
| 数据库 | MySQL 9.7.0 / MariaDB 3.5.7 |
| 鉴权 | Sa-Token 1.46.0 |
| 缓存 | Caffeine 3.2.3 + Spring Data Redis |
| 消息 | LMAX Disruptor 4.0.0、RabbitMQ（Spring AMQP）、Kafka 4.1.1、RocketMQ 2.3.5 |
| 定时任务 | Quartz 2.5.2（JDBC 持久化 + 执行日志 `vt_job_log`） |
| 接口文档 | springdoc-openapi 3.2.0 |
| 工具库 | Lombok 1.18.42、MapStruct 1.6.3、Hutool 5.8.43 |
| AI | Spring AI 2.0.0、Spring AI Alibaba 2.0.0-M1.1 |

## 📁 项目结构

```text
vt-boot/
├── pom.xml                          # 聚合父 POM：依赖版本统一管理、编译/资源/校验插件、Checkstyle
├── README.md                        # 项目总览入口（本文件）
├── .editorconfig                    # 编辑器格式约定
├── config/
│   └── checkstyle/checkstyle.xml    # 代码风格检查规则
├── doc/                             # 项目详细文档（快速开始/架构/模块/配置/接口/数据库/规范/FAQ）
├── vt-boot-starter/                 # 能力模块聚合
│   ├── vt-boot-base-starter         # 基础常量、上下文、请求响应、异常、工具
│   ├── vt-boot-web-starter          # Result/MultiResult、全局异常、CORS、WebMvc 配置
│   ├── vt-boot-aop-starter          # @AutoFillField 公共字段自动填充
│   ├── vt-boot-async-starter        # 虚拟线程执行器
│   ├── vt-boot-datasource-starter   # MyBatis-Plus 插件、公共字段填充、多租户
│   ├── vt-boot-cache-starter        # Caffeine + Redis 缓存工具
│   ├── vt-boot-log-starter          # @Log / @OperationRecord 注解切面 + logback 环境配置
│   ├── vt-boot-internationalization-starter  # 国际化消息工具
│   ├── vt-boot-mq-starter           # Disruptor 本地队列 + RabbitMQ
│   ├── vt-boot-openapi-starter      # OpenAPI 文档
│   ├── vt-boot-satoken-starter      # 鉴权拦截、安全工具、多租户拦截
│   ├── vt-boot-file-starter         # 文件/流/ZIP 上传工具
│   ├── vt-boot-rpc-starter          # 服务契约接口（如操作记录 SPI）
│   └── vt-boot-task-starter         # Quartz 定时任务（注解/配置声明 + JDBC 持久化 + 执行日志）
└── vt-boot-example/                 # 示例聚合
    └── vt-boot-example-admin        # AI 知识库后台示例（认证/用户/租户/登录日志）
```

## 🚀 快速开始

```bash
# 1. 确认环境
java -version        # 需 21+
mvn -v               # 需 3.6.3+

# 2. 构建
mvn clean install -DskipTests

# 3. 运行示例应用
cd vt-boot-example/vt-boot-example-admin
mvn spring-boot:run
```

启动后访问：

- Swagger UI：`http://localhost:9086/swagger-ui.html`
- OpenAPI JSON：`http://localhost:9086/v3/api-docs`

> 示例应用的数据库连接、端口、鉴权等配置见 [配置说明](./doc/configuration.md)，数据库表见 [数据库设计](./doc/database.md)。

## 📜 License

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt)

---

📅 最后更新：2026-11-17
