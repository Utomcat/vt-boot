# 配置说明

本文说明示例工程 `vt-boot-example-admin` 的配置文件拆分方式，以及各 Starter 提供的全部可配置项。

## 一、配置文件拆分

示例工程将配置按职责拆分到多个 YAML 文件，通过主配置中的 `spring.config.import` 统一导入：

```yaml
# application.yaml
spring:
  profiles:
    active: local
  config:
    import: >
      classpath:datasource.yaml,
      classpath:mybatisplus.yaml,
      classpath:sa-token.yaml
```

| 文件 | 职责 |
|------|------|
| `application.yaml` | 主配置：应用名、激活 Profile、虚拟线程、配置导入、端口、日志级别、OpenAPI 信息 |
| `datasource.yaml` | 数据源：`dynamic-datasource` 多数据源与 Hikari、MyBatis-Plus 方言、多租户开关 |
| `mybatisplus.yaml` | MyBatis-Plus：Mapper 位置、驼峰、逻辑删除、主键策略、执行器 |
| `sa-token.yaml` | 鉴权：Sa-Token 令牌名、有效期、并发/共享、拦截路径与排除路径 |

> `language.yaml` 与 `logback.yaml` 已不再需要：国际化默认值与 `logback-spring.xml` 均由 starter 内置（见第三、七节），业务工程只在需要覆盖时才显式配置。
> 建议业务工程沿用同样的「一职责一文件」拆分方式，便于多环境覆盖与维护。

## 二、application.yaml（主配置）

```yaml
spring:
  application:
    name: vt-boot-example
  profiles:
    active: local
  threads:
    virtual:
      enabled: true          # 开启全链路虚拟线程（Web / @Async / 调度）

vt:
  boot:
    log:
      operation-record-enabled: false   # 关闭操作记录入库（未实现 IOperationRecordService 时需关闭）
    openapi:
      enabled: true
      title: AI 知识库后台 API
      version: 1.0.0
      description: vt-boot 示例后台接口文档
      contact-name: ranyk

server:
  port: 9086
  servlet:
    encoding:
      charset: UTF-8
      force: true

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha

logging:
  level:
    com.ranyk.vt.boot.example: debug
    com.ranyk.vt.boot.datasource: info
```

要点：

- `spring.threads.virtual.enabled=true` 是框架虚拟线程能力的前提，务必开启。
- `vt.boot.log.operation-record-enabled=false`：示例未提供 `IOperationRecordService` 实现，需关闭操作记录审计；`@Log` 注解日志仍生效。

## 三、国际化（starter 内置默认值，无需配置文件）

`vt-boot-internationalization-starter` 通过 `EnvironmentPostProcessor` 注入以下**最低优先级**默认值，业务工程不配置也能用：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `spring.messages.basename` | `i18n/messages` | 消息资源基名 |
| `spring.messages.encoding` | `UTF-8` | 资源文件编码 |
| `spring.web.locale` | `zh_CN` | 默认区域 |
| `spring.web.locale-resolver` | `accept-header` | 按请求头 `Accept-Language` 解析 |

需要覆盖时直接在业务工程 `application.yaml` 写同名属性即可（默认值优先级最低，不会覆盖使用方配置）。

## 四、datasource.yaml（数据源）

采用 `dynamic-datasource` 结构，`master` 为默认数据源，多数据源时在 `datasource` 下继续追加：

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
          username: user
          password: "123456"
          hikari:
            connection-timeout: 30000
            maximum-pool-size: 20
            minimum-idle: 5
            idle-timeout: 600000
            max-lifetime: 1800000
            pool-name: vt-boot-hikari

vt:
  boot:
    db-type: MYSQL          # MyBatis-Plus 分页方言
    is-enable-tenant: false  # 关闭多租户插件
```

| 属性 | 说明 |
|------|------|
| `vt.boot.db-type` | MyBatis-Plus 分页插件方言，默认 `MYSQL` |
| `vt.boot.is-enable-tenant` | 是否启用多租户插件，默认 `false` |
| `vt.boot.ignore-table` | 不自动拼接租户条件的表名列表（列表类型） |

> `vt.boot` 前缀下的数据源属性由 `vt-boot-datasource-starter` 的 `DatasourceConfigurationProperties` 承载。

## 五、mybatisplus.yaml（MyBatis-Plus）

```yaml
mybatis-plus:
  mapper-locations: classpath*:mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
  global-config:
    banner: false
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
  executor-type: reuse
```

要点：

- `mapper-locations` 默认从 `classpath*:mapper/**/*.xml` 加载 XML 映射。
- 逻辑删除字段 `deleted`，`1` 为已删除、`0` 为正常，与 `BaseEntity` 的 `@TableLogic` 对应。
- 主键策略 `auto`（数据库自增），与 `BaseEntity` 的 `@TableId(type = IdType.AUTO)` 对应。

## 六、sa-token.yaml（鉴权）

```yaml
sa-token:
  enable: true
  token-name: Authorization
  timeout: 2592000            # 30 天（秒）
  is-concurrent: true
  is-share: false
  token-style: uuid
  is-log: true
  is-print: false
  match-path: /**
  path-patterns:
    - /**
  exclude-path-patterns:
    - /api/auth/login
    - /api/auth/refresh
    - /favicon.ico
    - /static/**
    - /error
    - /v3/api-docs/**
    - /swagger-ui/**
    - /swagger-ui.html
    - /webjars/**
```

`vt-boot-satoken-starter` 的 `SaTokenProperties`（前缀 `sa-token`）完整属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `enable` | `false` | 是否启用 Sa-Token 功能 |
| `token-name` | `satoken` | 令牌头名称（同时用于 Cookie） |
| `timeout` | `2592000` | 令牌有效期（秒），`-1` 永久 |
| `activity-timeout` | `-1` | 最低活跃频率（秒），`-1` 不冻结 |
| `is-concurrent` | `true` | 是否允许同账号多地同时登录 |
| `is-share` | `false` | 多人登录是否共用同一 token |
| `token-style` | `uuid` | 令牌风格（uuid / simple-uuid / random-32 / random-64 / random-128 / tik） |
| `is-log` | `true` | 是否输出操作日志 |
| `is-print` | `true` | 是否打印版本字符画 |
| `match-path` | `/**` | 拦截匹配规则 |
| `path-patterns` | — | 需要拦截的路径列表 |
| `exclude-path-patterns` | — | 排除拦截的路径列表 |

## 七、日志配置（内置 logback-spring.xml）

`vt-boot-log-starter` 内置 `logback-spring.xml`，由 Spring Boot 自动加载，**业务工程无需再配置 `logging.config`**，按 `spring.profiles.active` 自动切换：

| profile | 输出目标 | 日志保留 |
|---------|----------|----------|
| `local` / `dev` / 未指定 | 控制台 + 文件 | 7 天 |
| `test` | 控制台 + 文件 | 30 天 |
| `prod` | 仅文件（不输出控制台） | 180 天 |

可覆盖项（可选，写在业务工程 `application.yaml` 即可）：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `vt.boot.log.base-dir` | `./logs` | 日志根目录 |
| `spring.application.name` | `application` | 日志文件名中的模块名 |
| `logging.level.*` | — | 日志级别，由 Spring Boot 统一应用 |

如需完全自定义，可在业务工程放置自己的 `logback-spring.xml`（同名文件优先），或用 `--logging.config` 显式指定。

## 八、其余 Starter 配置项

### vt.boot.log（日志模块）

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `vt.boot.log.operation-record-enabled` | `true` | 是否启用操作记录审计（启用需提供 `IOperationRecordService` 实现） |

### vt.cache（缓存模块）

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `vt.cache.enabled` | `true` | 是否启用缓存 |
| `vt.cache.is-caffeine-enabled` | `true` | 是否启用 Caffeine 本地缓存（本地缓存无外部依赖，默认开） |
| `vt.cache.caffeine-init-size` | `100` | 本地缓存初始化大小 |
| `vt.cache.caffeine-max-size` | `500` | 本地缓存最大条数 |
| `vt.cache.is-output-log-warn` | `false` | 是否输出缓存移除日志 |
| `vt.cache.is-redis-enabled` | `false` | 是否启用 Redis 缓存 |

### vt.boot.mq（消息模块）

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `vt.boot.mq.is-enable-disruptor` | `true`（Starter 内置 yaml 缺省开启） | 是否开启 Disruptor 本地异步队列 |
| `vt.boot.mq.is-enable-rabbitmq` | `false` | 是否开启 RabbitMQ，需显式置 `true` 并配置 `spring.rabbitmq.*` |
| `vt.boot.mq.rabbitmq-allowed-list-patterns` | 内置白名单 | RabbitMQ 允许反序列化的类列表 |
| `vt.boot.mq.rabbitmq-default-message-converter` | `simpleMessageConverter` | 默认消息转换器（`simpleMessageConverter` / `jacksonJsonMessageConverter`） |

### vt.boot.openapi（接口文档模块）

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `vt.boot.openapi.enabled` | `true` | 是否启用 OpenAPI 自动配置 |
| `vt.boot.openapi.title` | `vt-boot API 文档` | 文档标题 |
| `vt.boot.openapi.version` | `1.0.0` | 文档版本 |
| `vt.boot.openapi.description` | `vt-boot 接口文档` | 文档描述 |
| `vt.boot.openapi.contact-name` | `ranyk` | 联系人名称 |
| `vt.boot.openapi.contact-email` | — | 联系人邮箱 |
| `vt.boot.openapi.contact-url` | — | 联系人主页 |

## 九、多环境配置建议

- 通过 `spring.profiles.active` 切换环境（示例默认 `local`）。
- 敏感信息（数据库密码、密钥）建议使用环境变量或外部配置覆盖，不要提交到仓库。
- 日志环境、接口文档开关建议按环境差异化配置（生产关闭 Swagger UI）。
