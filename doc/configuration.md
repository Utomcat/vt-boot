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
| `application.yaml` | 主配置：应用名、激活 Profile、虚拟线程、配置导入、Quartz 持久化、端口、日志级别、OpenAPI 信息 |
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

## 九、定时任务（task-starter）

任务调度基于 Quartz，**默认把任务持久化到数据库，服务重启后任务不会丢失**。

### 9.1 存储相关（`vt.task.store.*`）

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `vt.task.store.type` | `jdbc` | `jdbc` 表示任务与触发器持久化到数据库（默认，重启不丢失）；`memory` 退回 Quartz 内存存储 |
| `vt.task.store.initialize-schema` | `always` | 启动时执行框架内置的幂等建表脚本；`never` 表示交由 DBA / Flyway 手工建表 |
| `vt.task.store.schema` | `db/quartz-@@platform@@.sql` | 建表脚本位置，`@@platform@@` 按数据库类型替换为 `mysql_innodb` / `postgres` / `h2` |

要点：

- 存储装配由自动配置类 `VtTaskStoreAutoConfiguration` 完成（不使用 `EnvironmentPostProcessor` 改写配置）：容器中存在唯一数据源且开关打开时，把数据源交给 `SchedulerFactoryBean`，Quartz 自动使用 `LocalDataSourceJobStore`；建表脚本在 Quartz 初始化 JobStore 之前执行。
- 没有数据源的工程，本自动配置整体不生效，自动退回 Quartz 默认的内存存储。
- 需要显式接管时，Spring Boot 原生配置 `spring.quartz.job-store-type=memory` 会让框架存储装配整体后退；显式配置 `spring.quartz.job-store-type=jdbc` 时仍由框架负责建表（可用 `vt.task.store.initialize-schema=never` 关闭）。
- **不要用 Quartz 官方建表脚本替换 `vt.task.store.schema`**：官方脚本以 `DROP TABLE IF EXISTS` 开头，每次启动都会清空 `QRTZ_*` 中的任务数据；框架脚本是 `CREATE TABLE IF NOT EXISTS`，可重复执行且不会清空数据。
- 建表脚本位于 `vt-boot-task-starter/src/main/resources/db/`，需要离线建表时直接复制执行即可。
- Oracle / SQL Server / DB2 等未内置脚本，请把 `vt.task.store.initialize-schema` 置为 `never` 后自行建表。

### 9.2 vt.task（定时任务）

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `vt.task.enabled` | `true` | 是否启用定时任务 |
| `vt.task.overwrite-existing-jobs` | `false` | 启动时是否用代码中的定义覆盖数据库中已存在的同名任务 |
| `vt.task.tenant-id` | 空 | 任务执行时的默认租户 ID，为空表示不切换租户上下文 |
| `vt.task.jobs` | 空 | 声明式任务清单（`name` / `group` / `cron` / `executor` / `misfire` / `params`） |

> `overwrite-existing-jobs` 语义：`false`（默认）表示保留数据库中已持久化的任务，注册器发现同名任务直接跳过，暂停状态不会被重置；只有代码里的 cron 需要覆盖数据库时才临时置为 `true` 重启一次。

### 9.3 vt.task.log（任务执行日志）

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `vt.task.log.enabled` | `true` | 是否启用执行日志（容器中存在 `JdbcTemplate` 时生效） |
| `vt.task.log.table-name` | `vt_job_log` | 执行日志表名 |
| `vt.task.log.initialize-schema` | `never` | 置 `always` 时自动创建 `vt_job_log` 表 |
| `vt.task.log.max-error-length` | `4000` | 异常信息最大保存长度，超出截断 |

### 9.4 示例工程配置

```yaml
vt:
  task:
    overwrite-existing-jobs: false
    store:
      type: jdbc
      initialize-schema: always
```

> 以上三项均有默认值，全部省略效果相同；示例工程显式声明是为了让持久化行为一目了然。
> 多实例部署时再补 `spring.quartz.properties.org.quartz.jobStore.isClustered: true`（`spring.quartz.scheduler-name` 默认 `quartzScheduler`，多个应用共用同库时务必区分）。

## 十、多环境配置建议

- 通过 `spring.profiles.active` 切换环境（示例默认 `local`）。
- 敏感信息（数据库密码、密钥）建议使用环境变量或外部配置覆盖，不要提交到仓库。
- 日志环境、接口文档开关建议按环境差异化配置（生产关闭 Swagger UI）。

## 十一、启动 Banner（框架内置）

启动 banner 属于框架级内容，由 `vt-boot-base-starter` 内置在 classpath 根（`src/main/resources/banner.txt`），所有依赖 vt-boot 的应用启动时自动显示，**业务工程不要重复放置**，示例工程也不放。

覆盖方式全部走 Spring Boot 原生机制，无需任何 vt-boot 专属配置：

| 需求 | 做法 |
|------|------|
| 使用框架 banner（默认） | 不配置，业务工程也不放 `banner.txt` |
| 换成业务工程自己的 banner | 业务工程放 `src/main/resources/banner.txt`（fat jar 中 `BOOT-INF/classes` 先于 `BOOT-INF/lib` 加载，应用侧优先） |
| 指定其它文件名或外部路径 | `spring.banner.location: classpath:my-banner.txt` 或 `file:./config/banner.txt` |
| 关闭 banner | `spring.main.banner-mode: off` |
| banner 同时写入日志文件 | `spring.main.banner-mode: log` |
| 用图片转字符画 | `spring.banner.image.location: classpath:banner.png` |

完整配置写法如下（写入业务工程主配置 `application.yaml` 的最外层，属于框架级配置，不要塞进 `datasource.yaml` / `sa-token.yaml` 这类业务配置）：

```yaml
spring:
  main:
    # 可选值: console(默认, 输出到控制台) / log(输出到日志文件) / off(关闭)
    banner-mode: console
  banner:
    # 默认就是 classpath 根下的 banner.txt, 改名或换目录时才需要写
    location: classpath:banner.txt
    # banner 文件编码, 默认即 UTF-8; banner 中含中文时应显式声明
    charset: UTF-8
```

要点：

- 以上三个属性全部是 Spring Boot 原生属性（`spring.main.banner-mode`、`spring.banner.location`、`spring.banner.charset`），vt-boot 不做任何改写或二次封装。
- 三项都有默认值，**全部不配置时即等价于上面这段**：使用框架 `banner.txt`、输出到控制台、按 UTF-8 解码。所以这段配置只在需要改行为（`log` / `off` / 换文件）时按需修改。
- 如果按本工程的拆分习惯单独建 `banner.yaml`，必须在主配置的 `spring.config.import` 中补上 `classpath:banner.yaml`，否则不会加载。
- 不要让多个模块各放一份同名 `banner.txt`：classpath 根出现多个同名资源时只会命中其中一个，具体命中哪个由加载顺序决定，属于不可控行为；需要统一品牌时只在 `vt-boot-base-starter` 保留一份。
- `spring.banner.location` 指向的资源不存在时，Spring Boot 会静默退回内置默认 banner（只打印版本号），不会报错；排查以启动输出为准。
- banner 中的占位符由 Spring Boot 运行期解析，与 Maven 过滤无关（父 POM 已统一 `filtering=false`）。`${spring-boot.version}` 稳定可用；`${application.version}` 取启动类所在 jar 的 MANIFEST `Implementation-Version`，IDEA 中直接运行解析不到，需打包运行才准确。
- 修改 banner 后必须重启应用（工程未引入 `spring-boot-devtools`），热重载不会重新打印。
- `banner.txt` 统一 UTF-8 + CRLF，`*.txt` 已在 `.editorconfig` 中关闭行尾空格修剪，避免艺术字对齐被破坏。
