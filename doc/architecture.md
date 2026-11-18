# 架构设计

本文说明 `vt-boot` 的整体结构、模块依赖关系、代码分层约定与关键运行时机制。

## 一、总体结构

`vt-boot` 为 Maven 聚合多模块工程，顶层两个聚合：

```text
vt-boot (pom, 聚合 + 依赖版本统一管理)
├── vt-boot-starter (pom, 能力模块聚合)
│   └── vt-boot-*-starter (14 个能力模块, jar / pom)
└── vt-boot-example (pom, 示例聚合)
    └── vt-boot-example-admin (jar, 可执行示例应用)
```

- **父 POM** 统一管理依赖版本（`dependencyManagement`）、编译插件（Lombok + MapStruct 注解处理链）、资源过滤、环境校验（JDK 21 / Maven 3.6.3+）与 Checkstyle。
- **Starter** 提供能力，彼此解耦，业务工程按需引入；每个 Starter 通过 Spring Boot 3+ 的 `AutoConfiguration.imports` 注册自动配置。
- **Example** 演示能力落地，`vt-boot-example-admin` 依赖 web / satoken / log / datasource / openapi 五个 Starter。

## 二、模块依赖关系

```text
                        ┌────────────────────┐
                        │  vt-boot-base      │  常量 / 上下文 / 请求响应 / 异常 / 工具
                        └─────────┬──────────┘
       ┌──────────────┬───────────┼──────────────┬───────────────┬───────────────┐
       ▼              ▼           ▼              ▼               ▼               ▼
   aop(填充)      web(响应)   datasource     cache(缓存)   log(日志)        mq(消息)
       │              │      (MP 插件)         │              │               │
       └──────────────┴───────────┴──────────────┴──────────────┴───────────────┘
                                   │
                     ┌─────────────┴─────────────┐
                     ▼                           ▼
              satoken(鉴权)              internationalization(国际化)
                     │                           │
                     └──────────────┬────────────┘
                                    ▼
                          openapi / file / rpc / task
```

要点：

- `vt-boot-base` 是几乎所有模块的公共底座（响应体、异常、枚举、上下文、基类）。
- `vt-boot-web` 依赖 `base`，对外暴露 `Result` / `MultiResult` 与全局异常处理。
- `vt-boot-datasource` 依赖 `base`，提供 MyBatis-Plus 插件与多租户能力。
- `vt-boot-satoken` 依赖 `base` 与 `web`，负责鉴权与多租户上下文注入。
- `vt-boot-rpc` 仅提供服务契约接口（如操作记录 SPI），供跨模块解耦使用。

模块职责详见 [模块说明](./modules.md)。

## 三、代码分层约定

示例工程采用清晰的**四层模型 + 模型分离**结构（包 `com.ranyk.vt.boot.example`）：

| 层 | 包 | 职责 | 模型 |
|----|----|------|------|
| API 层 | `*.api` | 暴露 REST 接口、参数校验、权限校验 | 入参 `PO`、返回 `Result`/`MultiResult` |
| 转换层 | `*.converter` | MapStruct 编译期模型转换 | — |
| Service 层 | `*.service` | 业务编排、事务、校验 | 业务参数 `DTO` |
| 持久层 | `*.mapper` + `*.domain.entity` | MyBatis-Plus 数据访问 | 实体 `Entity` |
| 领域模型 | `*.domain` | 数据传输与展示 | `PO` / `DTO` / `VO` |

**模型约定**：

- `PO`（Persistent/Presentation Object 命名约定）：API 层入参。
- `DTO`：Service 层内部业务参数。
- `VO`：对外返回视图对象。
- `Entity`：数据库映射实体，继承 `BaseEntity`（含 `id / tenantId / deleted / createBy / createTime / updateBy / updateTime`）。
- 模型之间转换统一使用 MapStruct（`@Mapper(componentModel = SPRING)`），在编译期生成实现，避免手写样板代码。

**一次请求的数据流**：

```text
HTTP 请求
  → API（接收 PO, @Valid 校验）
  → Converter（PO → DTO）
  → Service（业务处理, 事务）
  → Mapper/Entity（MyBatis-Plus 访问数据库）
  → Converter（Entity → VO）
  → Result<T> / MultiResult<T>（统一响应）
  → HTTP 响应
```

## 四、统一响应与异常

- 对外响应模型统一为 `Result<T>`（单结果）与 `MultiResult<T>`（列表/分页），位于 `vt-boot-web-starter`。
- 响应结构：`code`（取值 `ResponseCodeEnum` 名或业务错误码）、`success`、`msg`、`data`。
- 异常统一由 `GlobalWebExceptionHandler` 全局处理，业务代码只抛 `BaseException` 子类（`ServiceException` / `UserException` / `CacheException` / `DataSourceException` / `FileException` / `MqException`）。
- 用户提示文案通过 `code + args` 走国际化（见 [配置说明](./configuration.md) 与 `MessageUtils`）。

## 五、虚拟线程模型

框架以虚拟线程为并发基础：

- 通过 `spring.threads.virtual.enabled=true` 让 Spring Boot 为 Web 请求、`@Async`、调度等使用虚拟线程。
- `vt-boot-async-starter` 提供 `VtExecutors.newVirtualThreadPerTaskExecutor()`，作为业务自定义异步任务的标准执行器。
- 编码规范要求：异步统一使用虚拟线程执行器，禁止直接 `new Thread(...)` / 手动 `new ThreadPoolExecutor(...)`（除非有明确容量边界并在类注释说明）。

## 六、自动配置机制

每个 Starter 遵循统一的自动配置范式：

```java
@AutoConfiguration
@EnableConfigurationProperties(XxxProperties.class)
@ConditionalOnBooleanProperty(prefix = "vt.boot.xxx", name = "enabled", havingValue = true)
public class XxxAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Xxx xxx(final XxxProperties properties) { ... }
}
```

- 自动配置类登记于 `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`。
- 每个 `@Bean` 均带 `@ConditionalOnMissingBean`，业务工程可自行覆盖。
- 布尔开关统一使用 `@ConditionalOnBooleanProperty`。
- 配置属性类使用 `@ConfigurationProperties` + `@Data` + `@EnableConfigurationProperties`，不额外加 `@Component`。

## 七、多租户与上下文

- `vt-boot-base` 提供 `TenantContext`、`UserContext`（`ThreadLocal`），请求进入时 `set`、请求结束 `afterCompletion` 中 `clear`。
- `vt-boot-datasource` 通过 `TenantLineInnerInterceptor` + `CustomTenantHandler` 自动拼接租户条件，业务 SQL 不手写 `tenant_id`。
- 是否启用租户由 `vt.boot.is-enable-tenant` 控制，忽略表通过 `vt.boot.ignore-table` 配置。
- 跨线程传递上下文需显式 `ContextSnapshot.capture()/restore()`（禁止依赖 `ThreadLocal` 自动透传）。

## 八、构建期约束

父 POM 在 `validate` 阶段通过 `maven-enforcer-plugin` 强制校验：

- JDK `[21,)`；
- Maven `[3.6.3,)`。

同时统一配置 `maven-compiler-plugin` 的注解处理器链（Lombok → Lombok-MapStruct Binding → MapStruct），保证 Lombok 生成代码后再由 MapStruct 处理。详见 [编码规范](./CODING-STANDARDS.md)。
