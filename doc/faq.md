# 常见问题（FAQ）

本文汇总构建、启动、配置与使用过程中的高频问题与排查思路。

## 一、构建相关

### Q1：构建报「本框架依赖 JDK 21 虚拟线程」

**原因**：Maven 运行所依赖的 JDK 低于 21，被父 POM 的 `maven-enforcer-plugin` 拦截。

**处理**：确认 `JAVA_HOME` 指向 JDK 21 及以上，执行 `java -version` 与 `mvn -v` 核对后重试。注意 `mvn -v` 显示的是 Maven 自身使用的 JDK。

### Q2：构建报「请使用 Maven 3.6.3 或更高版本重试」

**原因**：Maven 版本过低。

**处理**：升级 Maven（推荐 3.9.9+），或确认当前 `mvn` 指向的是预期的 Maven 安装。

### Q3：修改了 Starter 后，业务工程仍使用旧版本

**原因**：Starter 以 `0.0.1-SNAPSHOT` 发布到本地仓库，业务工程命中的是本地仓库里旧的快照。

**处理**：在根目录重新执行 `mvn clean install -DskipTests`，将最新 Starter 安装到本地仓库后再构建业务工程。

### Q4：MapStruct 生成的实现类缺少部分字段映射

**原因**：注解处理器顺序或配置问题。父 POM 已固定处理器链为 Lombok → Lombok-MapStruct Binding → MapStruct。

**处理**：确认未在子模块覆盖 `annotationProcessorPaths`；如自定义了 `@Mapper`，注意 `unmappedTargetPolicy` 配置。

## 二、启动相关

### Q5：启动报「无法创建数据源 / 连接被拒绝」

**原因**：`datasource.yaml` 中的数据库地址、账号或密码不正确，或数据库实例不可达。

**处理**：核对 `spring.datasource.dynamic.datasource.master.*`，确认数据库已启动、库已创建。建表参考 [数据库设计](./database.md)。

### Q6：启动报缺少 `IOperationRecordService` 实现 Bean

**原因**：`vt.boot.log.operation-record-enabled=true`（默认值）但工程未提供操作记录 SPI 实现。

**处理**：示例工程已在 `application.yaml` 中置为 `false`。若需启用操作记录，请实现 `com.ranyk.vt.boot.rpc.service.IOperationRecordService` 并注册为 Bean。

### Q7：接口全部 401 / 未登录

**原因**：请求未携带令牌，或令牌已过期。

**处理**：先调用 `POST /api/auth/login` 获取令牌，后续请求在请求头 `Authorization` 中携带。确认 `sa-token.yaml` 的排除路径包含登录、刷新等无需鉴权的接口。

### Q8：Swagger UI 打不开

**原因**：`vt.boot.openapi.enabled=false`，或未引入 `vt-boot-openapi-starter`，或路径被鉴权拦截。

**处理**：确认 `vt.boot.openapi.enabled=true`、引入 OpenAPI Starter，并在 `sa-token.yaml` 排除 `/swagger-ui/**`、`/v3/api-docs/**`。

### Q9：`.properties` 或资源文件中文乱码

**原因**：资源过滤编码未统一为 UTF-8。

**处理**：父 POM 已对资源插件配置 UTF-8 过滤；业务工程自定义 `resources` 时请保持 `encoding` 为 UTF-8。

## 三、配置相关

### Q10：多数据源如何配置？

在 `spring.datasource.dynamic.datasource` 下按 `master` 的格式继续追加数据源，`dynamic.primary` 指定默认数据源，业务代码通过 `@DS("数据源名")` 切换。

### Q11：如何开启多租户？

将 `vt.boot.is-enable-tenant` 置为 `true`，并通过 `vt.boot.ignore-table` 配置不参与租户隔离的表。租户条件由 `TenantLineInnerInterceptor` 自动拼接，业务 SQL 不要手写 `tenant_id`。

### Q12：如何开启 RabbitMQ？

`vt.boot.mq.is-enable-rabbitmq` 置为 `true`，并配置 `spring.rabbitmq.*`（未配置时回退到 `localhost:5672` 默认值）。反序列化白名单通过 `vt.boot.mq.rabbitmq-allowed-list-patterns` 维护。

### Q13：如何切换日志环境？

无需额外配置：`vt-boot-log-starter` 内置 `logback-spring.xml`，按 `spring.profiles.active` 自动切换（`local`/`dev`/未指定 = 控制台 + 文件保留 7 天，`test` = 保留 30 天，`prod` = 仅文件保留 180 天）。日志根目录用 `vt.boot.log.base-dir` 调整，默认 `./logs`。

## 四、使用相关

### Q14：统一响应里 `code` 为什么是字符串？

框架约定 `code` 取 `ResponseCodeEnum` 名称或业务错误码（便于国际化映射），不使用 HTTP 数字字符串。详见 [编码规范](./CODING-STANDARDS.md) 第七节。

### Q15：如何新增一个业务模块？

参考 `vt-boot-example-admin` 的分层结构：`api`（PO 入参）→ `converter`（MapStruct）→ `service`（DTO）→ `mapper` + `entity` → `vo`，最后以 `Result` / `MultiResult` 返回。详见 [架构设计](./architecture.md)。

### Q16：如何在业务工程中只引入部分能力？

`vt-boot` 的 Starter 相互解耦、按需引入。最小组合通常为 `vt-boot-web-starter`（响应与异常）+ 业务所需能力模块。详见 [模块说明](./modules.md)。

## 五、反馈

如遇本文未覆盖的问题，请优先检查各模块对应的配置项（见 [配置说明](./configuration.md)）与构建环境（JDK 21 / Maven 3.6.3+）。
