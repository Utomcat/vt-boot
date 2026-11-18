# 模块说明

本文对 `vt-boot-starter` 下全部能力模块进行逐一说明，包括模块职责、关键入口与使用边界。各模块依赖 `vt-boot-base`（基础常量、上下文、请求响应、异常），无需手动引入 Lombok（父 POM 已统一以 `provided` 作用域生效）。

## 一、模块一览表

| 模块 | artifactId | 职责一句话 |
|------|-----------|-----------|
| 基础模块 | `vt-boot-base-starter` | 常量枚举、租户/用户上下文、请求响应基类、通用异常、密码工具 |
| Web 模块 | `vt-boot-web-starter` | 统一响应体 `Result`/`MultiResult`、全局异常处理、CORS、WebMvc 配置 |
| AOP 模块 | `vt-boot-aop-starter` | `@AutoFillField` 公共字段自动填充（创建/更新人与时间、租户） |
| 异步模块 | `vt-boot-async-starter` | 虚拟线程执行器 `VtExecutors` 与自动配置 |
| 数据源模块 | `vt-boot-datasource-starter` | MyBatis-Plus 插件（分页 / 防全表 / 乐观锁 / 多租户）、公共字段填充 |
| 缓存模块 | `vt-boot-cache-starter` | Caffeine 本地缓存 + Spring Data Redis，`CacheUtils` 工具 |
| 日志模块 | `vt-boot-log-starter` | `@Log` / `@OperationRecord` 注解切面、操作日志策略、logback 环境配置 |
| 国际化模块 | `vt-boot-internationalization-starter` | 基于 Spring `MessageSource` 的国际化消息工具 `MessageUtils` |
| MQ 模块 | `vt-boot-mq-starter` | Disruptor 本地异步队列 + RabbitMQ，邮件/日志/短信处理策略 |
| OpenAPI 模块 | `vt-boot-openapi-starter` | springdoc-openapi 接口文档，默认文档信息可配 |
| 鉴权模块 | `vt-boot-satoken-starter` | Sa-Token 鉴权拦截器、安全工具、令牌异常处理、多租户拦截 |
| 文件模块 | `vt-boot-file-starter` | 文件/流内容读取、ZIP 打包等上传处理工具 |
| RPC 模块 | `vt-boot-rpc-starter` | 服务契约接口（如操作记录 SPI `IOperationRecordService`） |
| 定时模块 | `vt-boot-task-starter` | 定时任务模块（**占位，暂未实现**） |

> 各 Starter 均已注册到 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`，引入后即生效。

---

## 二、各模块详述

### 1. vt-boot-base-starter（基础底座）

**核心内容**：

- `BaseEntity` / `BaseDTO` / `BasePO` / `BaseVO`：公共模型基类（`id / tenantId / deleted / createBy / createTime / updateBy / updateTime`）。
- `PageRequest`：分页通用请求参数（`currentPage`、`pageSize`）。
- `BaseResponse`（已 `@Deprecated`） / `SingleResponse` / `MultipleResponse`：内部响应属性基类，对外建议使用 `Result` / `MultiResult`。
- `ResponseCodeEnum`：`SUCCESS / DUPLICATED / ILLEGAL_ARGUMENT / SYSTEM_ERROR / BIZ_ERROR`。
- 异常体系：`BaseException` 及子类 `ServiceException` / `UserException` / `CacheException` / `DataSourceException` / `FileException` / `MqException`。
- `TenantContext` / `UserContext`：请求线程上下文（租户 ID、用户信息）。
- `PasswordUtils`：密码加密工具（业务中示例用 BCrypt）。
- 常量包：操作类型、消息类型、租户枚举、响应码、 RabbitMQ 键、压缩后缀等。
- 启动 banner：`src/main/resources/banner.txt`，框架统一品牌输出；业务工程自带 `banner.txt` 或用 `spring.banner.location` 显式覆盖（详见《配置说明》第十节）。

> `ResponseCodeEnum` 的 `code` 在 `Result` 响应中作为字符串返回，禁止使用 HTTP 数字字符串。

### 2. vt-boot-web-starter（Web 基础）

**核心内容**：

- `Result<T>`：单结果响应，提供 `success(data)`、`error(code, msg)` 工厂方法。
- `MultiResult<T>`：列表/分页响应，提供 `page`/`size`/`total`/`totalPage`/`list`。
- `GlobalWebExceptionHandler`：Spring Boot 全局异常处理，将 `BaseException` 统一转换为标准响应。
- `WebMvcConfiguration`：MVC 自定义配置（拦截器、消息转换器等）。
- `CorsAutoConfiguration`：CORS 跨域配置（白名单化，禁止 `*` + `allowCredentials(true)`）。

**示例返回**：`Result.success(data)`，错误返回 `Result.error("BIZ_ERROR", "错误消息")`。

### 3. vt-boot-aop-starter（字段自动填充）

**核心内容**：

- 注解 `@AutoFillField`：作用于 Service 方法，自动填充 `createBy / createTime / updateBy / updateTime`，并可选填充 `tenantId`。
- `AutoFillFieldAspect`：切面实现，基于 `UserContext` / `TenantContext` 取值。

**适用场景**：数据新增/修改前统一填充审计字段，业务层仅需在方法上标注注解。

### 4. vt-boot-async-starter（虚拟线程异步）

**核心内容**：

- `VtExecutors.newVirtualThreadPerTaskExecutor()`：返回 `Executors.newVirtualThreadPerTaskExecutor()`。
- `VtAsyncAutoConfiguration`：虚拟线程异步配置。

**使用边界**：业务异步调用统一调用此执行器，任务体内需自行兜底异常；禁止直接构造平台线程池。

### 5. vt-boot-datasource-starter（数据源与 MyBatis-Plus）

**核心内容**：

- `DataSourceConfiguration`：注册 `MybatisPlusInterceptor`（分页 `PaginationInnerInterceptor`、防全表删除/更新 `BlockAttackInnerInterceptor`、乐观锁 `OptimisticLockerInnerInterceptor`、多租户 `TenantLineInnerInterceptor`）、`MetaObjectHandler`（公共字段自动填充 `DataObjectHandler`）。
- `DatasourceConfigurationProperties`（前缀 `vt.boot`）：`is-enable-tenant`、`ignore-table`、`db-type`。
- `CustomTenantHandler`：租户条件处理器，租户 ID 统一按字符串处理。

**分页约定**：业务层统一使用 `PageUtils.buildPage(dto)` / `PageUtils.buildPageResponse(iPage, voList)`，禁止在业务层直接 `new Page<>()`。

### 6. vt-boot-cache-starter（缓存）

**核心内容**：

- `CacheAutoConfiguration`、`CaffeineAutoConfiguration`、`RedisAutoConfiguration`：按需开启本地缓存 / Redis 缓存。
- `CacheConfigurationProperties`（前缀 `vt.cache`）：`enabled / is-caffeine-enabled / caffeine-init-size / caffeine-max-size / is-output-log-warn / is-redis-enabled`。
- `CacheUtils`：缓存读写工具，Redis 连接失败自动降级，批量读写走批量 API，禁止调用 `flushAll`/`flushDb`。

**key 前缀规范**：`{应用}:{模块}:{业务}:{标识}`。

### 7. vt-boot-log-starter（日志）

**核心内容**：

- 注解 `@Log`：方法级操作日志，支持 `operation / type / recordParams / recordResult`，日志类型 `OPERATION / ERROR / ACCESS / LOGIN / LOGOUT / AUTHENTICATION / INSERT / UPDATE / DELETE / SELECT / EXECUTE / OTHER`。
- 注解 `@OperationRecord`：操作记录审计，支持 `desc / module / type / recordParams / isSaveOperationRecord / saveOperationRecordMethod / asyncMethod`，异步保存基于 Disruptor。
- `LogAspectHandle` / `OperationRecordAspectHandle`：对应两个注解的切面处理。
- `OperationRecordStrategy` 与实现 `SyncOperationRecordSave` / `AsyncOperationRecordSave`：同步 / 异步保存策略，SPI 接口在 `vt-boot-rpc-starter` 中声明。
- `LogConfigurationProperties`（前缀 `vt.boot.log`）：`operation-record-enabled`，默认 `true`；框架内置空实现 `DefaultOperationRecordService`，业务只需提供自己的 `IOperationRecordService` Bean 即可覆盖（`@ConditionalOnMissingBean`）。
- 内置 `logback-spring.xml`：按 `spring.profiles.active` 自动切换控制台/文件输出与保留天数（`local`/`dev` = 7 天，`test` = 30 天，`prod` = 180 天且仅文件），业务工程无需配置 `logging.config`。

**日志规约**：`@Slf4j` + `{}` 占位符，禁止拼接；禁止输出令牌、密码、完整手机号/证件号（需打码）。

### 8. vt-boot-internationalization-starter（国际化）

**核心内容**：

- `I18nConfigure`：国际化自动配置（消息来源 `i18n/messages`）。
- `MessageUtils`：基于 Spring `MessageSource` + `LocaleContextHolder` 的静态工具，`message(code, args)`，找不到键时返回键本身。
- 资源文件：`messages.properties`、`messages_zh_CN.properties`、`messages_en_US.properties`。

**新增错误码三件事同步**：登记 `*ErrorCodeEnum`、补 `messages_zh_CN.properties`、补 `messages_en_US.properties`。

### 9. vt-boot-mq-starter（消息队列）

**核心内容**：

- `MqAutoConfiguration`：MQ 自动配置入口，配置前缀 `vt.boot.mq`。
- `DisruptorAutoConfiguration`：本地 Disruptor 异步队列，默认 `ProducerType.MULTI`，缓冲区大小为 2 的幂；提供 `Email / Log / Sms` 三种处理策略实现。
- `RabbitMqConfiguration`：Spring AMQP 配置，反序列化白名单 `rabbitmq-allowed-list-patterns`、默认转换器 `rabbitmq-default-message-converter`。
- `MqConfigurationProperties`（前缀 `vt.boot.mq`）：`is-enable-disruptor`（缺省开启）、`is-enable-rabbitmq`（缺省关闭，需显式置 `true`）、`rabbitmq-allowed-list-patterns`、`rabbitmq-default-message-converter`。
- `MqUtils`：MQ 发送工具，发送前校验消息头，MQ 异常不影响主业务。

**使用边界**：启用 RabbitMQ 时需同时配置 `spring.rabbitmq.*`；消息消费采用「有限重试 + 死信」策略。

### 10. vt-boot-openapi-starter（接口文档）

**核心内容**：

- `OpenApiAutoConfiguration`：基于 springdoc-openapi，默认启用。
- `OpenApiProperties`（前缀 `vt.boot.openapi`）：`enabled / title / version / description / contact-name / contact-email / contact-url`。

示例应用配置后访问：`/swagger-ui.html` 与 `/v3/api-docs`。

### 11. vt-boot-satoken-starter（鉴权）

**核心内容**：

- `SaTokenConfigure`、`SaTokenInterceptorsConfig`、`SaTokenPermissionConfiguration`：Sa-Token 拦截器注册与权限配置。
- `TokenRequestInterceptor` / `TokenResponseInterceptor`：令牌请求/响应拦截。
- `TenantWebInterceptor`：多租户 Web 拦截。
- `SaTokenExceptionHandler`：令牌异常统一处理。
- `StpInterfaceImpl`：Sa-Token 权限接口实现（角色/权限）。
- `SecurityUtils`：当前登录用户信息获取工具。
- `SaTokenProperties`（前缀 `sa-token`）：`enable / token-name / timeout / is-concurrent / is-share / token-style / is-log / match-path / path-patterns / exclude-path-patterns`。

**排除路径示例**：`/api/auth/login`、`/api/auth/refresh`、`/swagger-ui/**`、`/v3/api-docs/**` 等。

### 12. vt-boot-file-starter（文件处理）

**核心内容**：

- `FileUploadUtils`：文件对象/输入流读取为 `byte[]`，ZIP 压缩条目处理，目录创建 `mkdirs`，后缀判断 `endsWith("." + suffix)`。

**使用边界**：文件/流操作使用 try-with-resources，自己创建的流自己关闭。

### 13. vt-boot-rpc-starter（服务契约）

**核心内容**：

- `IOperationRecordService`：操作记录保存 SPI 接口（`saveOneOperationRecord(name, type, param, operator, module)`），由 `@OperationRecord` 异步策略调用；业务工程按需自行实现并注册 Bean。

### 14. vt-boot-task-starter（定时任务）

当前为占位模块，`pom.xml` 中未声明任何依赖与源码，**暂未实现定时任务能力**，后续可按需扩展。

---

## 三、在业务工程中引入建议

按需引入，避免全量拖拽：

```xml
<!-- Web 基础：统一响应 + 异常处理（建议所有项目引入） -->
<dependency>
    <groupId>com.ranyk</groupId>
    <artifactId>vt-boot-web-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>

<!-- 鉴权：登录鉴权与 Sa-Token -->
<dependency>
    <groupId>com.ranyk</groupId>
    <artifactId>vt-boot-satoken-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>

<!-- 数据源：MyBatis-Plus + 多租户 -->
<dependency>
    <groupId>com.ranyk</groupId>
    <artifactId>vt-boot-datasource-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>

<!-- 日志：操作日志注解切面 -->
<dependency>
    <groupId>com.ranyk</groupId>
    <artifactId>vt-boot-log-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

示例应用 `vt-boot-example-admin` 的依赖组合见 `pom.xml`（web / satoken / log / datasource / openapi）。
