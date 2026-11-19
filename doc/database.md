# 数据库设计

本文说明示例工程 `vt-boot-example-admin` 涉及的数据库表结构。

> 仓库中未提交基础业务表的 `.sql` 建表脚本，下方 DDL 为基于 `src/main/java` 中的实体类整理并与线上库核对过的 **参考建表语句**，建表时请按实际环境调整字段、索引与字符集。
> RBAC 相关的 4 张表（`t_role` / `t_menu` / `t_user_role` / `t_role_menu`）已提供幂等初始化脚本：`vt-boot-example-admin/src/main/resources/db/rbac-mysql.sql`（含建表与内置角色、菜单、权限种子数据）。

## 一、表清单

| 表名 | 说明 |
|------|------|
| `t_user` | 用户表（角色：ADMIN / USER） |
| `t_tenant` | 租户表（套餐：FREE / STANDARD / ENTERPRISE） |
| `t_login_log` | 登录审计流水表 |
| `t_user_refresh_token` | 用户刷新令牌表 |
| `t_role` | 角色表（ADMIN / USER / 自定义角色） |
| `t_menu` | 菜单与权限表（DIR / MENU / BUTTON） |
| `t_user_role` | 用户-角色关联表 |
| `t_role_menu` | 角色-菜单关联表 |
| `QRTZ_*`（11 张） | Quartz JDBC 存储表（任务明细 / 触发器 / 锁 / 实例状态），默认开启 JDBC 存储时自动创建 |
| `vt_job_log` | 定时任务执行日志表（`vt.task.log.initialize-schema=always` 时自动创建） |
| `operation_log_info` | 操作记录表（`@OperationRecord` 默认写入表，非强制，按需创建） |

所有表均继承公共字段：`id / tenantId / deleted / createBy / createTime / updateBy / updateTime`，其中 `deleted` 为逻辑删除标记（`0` 正常、`1` 已删除）。

## 二、建表参考

### 1. t_user — 用户表

```sql
CREATE TABLE IF NOT EXISTS `t_user` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`     BIGINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '租户ID',
    `username`      VARCHAR(64)  NOT NULL COMMENT '登录名',
    `password`      VARCHAR(255) NOT NULL COMMENT '加密密码(BCrypt; 兼容历史 MD5)',
    `real_name`     VARCHAR(64)  COMMENT '真实姓名',
    `email`         VARCHAR(128) COMMENT '邮箱',
    `phone`         VARCHAR(32)  COMMENT '手机号',
    `avatar`        VARCHAR(512) COMMENT '头像相对路径',
    `role`          VARCHAR(16)  NOT NULL DEFAULT 'USER' COMMENT '主角色编码: ADMIN / USER',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
    `login_fail_count` INT       NOT NULL DEFAULT 0 COMMENT '连续登录失败次数',
    `lock_time`     DATETIME     DEFAULT NULL COMMENT '账号锁定截止时间',
    `deleted`       TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除',
    `create_by`     BIGINT UNSIGNED COMMENT '创建人ID',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`     BIGINT UNSIGNED COMMENT '更新人ID',
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `last_login_time` DATETIME  COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(64)  COMMENT '最后登录IP',
    `remark`        VARCHAR(512) COMMENT '管理员备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_status` (`status`),
    KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

### 2. t_tenant — 租户表

```sql
CREATE TABLE IF NOT EXISTS `t_tenant` (
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`      BIGINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '租户ID',
    `name`           VARCHAR(128) NOT NULL COMMENT '租户名称',
    `code`           VARCHAR(64)  NOT NULL COMMENT '租户编码(唯一标识)',
    `contact_name`   VARCHAR(64)  COMMENT '联系人姓名',
    `contact_email`  VARCHAR(128) COMMENT '联系邮箱',
    `plan`           VARCHAR(32)  COMMENT '套餐: FREE / STANDARD / ENTERPRISE',
    `max_users`      INT          COMMENT '最大用户数配额',
    `max_storage_mb` BIGINT       COMMENT '最大存储空间(MB)',
    `max_daily_tokens` BIGINT    COMMENT '每日Token上限',
    `status`         TINYINT      DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
    `expire_time`    DATETIME     COMMENT '到期时间',
    `deleted`        TINYINT      DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除',
    `create_by`      BIGINT UNSIGNED COMMENT '创建人ID',
    `create_time`    DATETIME     COMMENT '创建时间',
    `update_by`      BIGINT UNSIGNED COMMENT '更新人ID',
    `update_time`    DATETIME     COMMENT '更新时间',
    `remark`         VARCHAR(255) COMMENT '管理员备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户表';
```

### 3. t_login_log — 登录审计流水表

```sql
CREATE TABLE IF NOT EXISTS `t_login_log` (
    `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`  BIGINT UNSIGNED COMMENT '租户ID',
    `user_id`    BIGINT UNSIGNED COMMENT '用户ID(登录成功或匹配到账号时填充)',
    `username`   VARCHAR(64)  NOT NULL COMMENT '登录名(原始输入, 含未匹配账号)',
    `login_type` VARCHAR(32)  COMMENT '登录方式: PASSWORD / REFRESH_TOKEN / API_KEY',
    `success`    TINYINT      COMMENT '是否成功: 0-失败 1-成功',
    `fail_reason`VARCHAR(255) COMMENT '失败原因',
    `ip`         VARCHAR(64)  COMMENT '客户端IP',
    `device_info`VARCHAR(255) COMMENT '设备信息',
    `deleted`    TINYINT      DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除',
    `create_by`  BIGINT UNSIGNED COMMENT '创建人ID',
    `create_time`DATETIME     COMMENT '创建时间',
    `update_by`  BIGINT UNSIGNED COMMENT '更新人ID',
    `update_time`DATETIME     COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_username` (`username`),
    KEY `idx_success` (`success`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录审计流水表';
```

### 4. t_user_refresh_token — 用户刷新令牌表

```sql
CREATE TABLE IF NOT EXISTS `t_user_refresh_token` (
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`   BIGINT UNSIGNED COMMENT '租户ID',
    `user_id`     BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `refresh_token` VARCHAR(255) NOT NULL COMMENT 'Refresh Token 值',
    `device_info` VARCHAR(255) COMMENT '设备信息',
    `ip`          VARCHAR(64)  COMMENT '创建时客户端IP',
    `expire_time` DATETIME     COMMENT '过期时间',
    `revoked`     TINYINT      DEFAULT 0 COMMENT '是否已撤销: 0-有效 1-已撤销',
    `deleted`     TINYINT      DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除',
    `create_by`   BIGINT UNSIGNED COMMENT '创建人ID',
    `create_time` DATETIME     COMMENT '创建时间',
    `update_by`   BIGINT UNSIGNED COMMENT '更新人ID',
    `update_time` DATETIME     COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_refresh_token` (`refresh_token`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户刷新令牌表';
```

### 5. RBAC 相关表（角色 / 菜单 / 关联）

> 完整建表与种子数据见 `vt-boot-example-admin/src/main/resources/db/rbac-mysql.sql`（幂等，可重复执行）。

```sql
CREATE TABLE IF NOT EXISTS `t_role` (
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`   BIGINT          NOT NULL DEFAULT 1 COMMENT '租户ID',
    `code`        VARCHAR(64)     NOT NULL COMMENT '角色编码: ADMIN / USER / 自定义',
    `name`        VARCHAR(64)     NOT NULL COMMENT '角色名称',
    `description` VARCHAR(255)             DEFAULT NULL COMMENT '角色描述',
    `builtin`     TINYINT         NOT NULL DEFAULT 0 COMMENT '是否内置角色: 1-是 0-否',
    `sort`        INT             NOT NULL DEFAULT 0 COMMENT '排序值',
    `status`      TINYINT         NOT NULL DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人ID',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人ID',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`tenant_id`, `code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `t_menu` (
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`   BIGINT          NOT NULL DEFAULT 1 COMMENT '租户ID',
    `parent_id`   BIGINT          NOT NULL DEFAULT 0 COMMENT '父菜单ID, 0 表示顶级',
    `name`        VARCHAR(64)     NOT NULL COMMENT '菜单名称',
    `type`        VARCHAR(16)     NOT NULL DEFAULT 'MENU' COMMENT 'DIR-目录 MENU-页面 BUTTON-按钮',
    `path`        VARCHAR(200)             DEFAULT NULL COMMENT '路由地址',
    `component`   VARCHAR(200)             DEFAULT NULL COMMENT '前端组件标识',
    `icon`        VARCHAR(64)              DEFAULT NULL COMMENT '图标名称',
    `permission`  VARCHAR(128)             DEFAULT NULL COMMENT '权限码',
    `sort`        INT             NOT NULL DEFAULT 0 COMMENT '排序值',
    `visible`     TINYINT         NOT NULL DEFAULT 1 COMMENT '侧边栏显示: 1-显示 0-隐藏',
    `status`      TINYINT         NOT NULL DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人ID',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人ID',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_menu_parent` (`parent_id`),
    KEY `idx_menu_permission` (`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单与权限表';

CREATE TABLE IF NOT EXISTS `t_user_role` (
    `id`      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `role_id` BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';

CREATE TABLE IF NOT EXISTS `t_role_menu` (
    `id`      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT UNSIGNED NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单关联表';
```

约定：

- `t_user_role` / `t_role_menu` 为物理删除的关联表，不继承公共审计字段。
- 超级管理员（角色编码 `ADMIN`）的权限码在代码中直接视为通配 `*`，无需逐条绑定按钮；但为前端菜单渲染仍需绑定全部菜单。
- 用户权限解析优先读取 `t_user_role`，无绑定时回退到 `t_user.role`，用于兼容存量数据。

### 6. operation_log_info — 操作记录表（可选）

`@OperationRecord` 默认将审计记录写入 `operation_log_info` 表。若启用操作记录审计（`vt.boot.log.operation-record-enabled=true`），需自行建表，表字段可与 `t_login_log` 保持一致并按需扩展 `operation / module / param / operator` 等字段；也可通过 `@OperationRecord(module = "xxx")` 指定自定义表名。

## 三、字段约定

| 约定 | 说明 |
|------|------|
| 逻辑删除 | `deleted` 字段，`0` 正常、`1` 已删除，与 `BaseEntity` `@TableLogic` 对应 |
| 租户隔离 | 多租户模式下所有表均包含 `tenant_id`，由 `TenantLineInnerInterceptor` 自动拼接条件 |
| 主键策略 | 数据库自增（`AUTO_INCREMENT`），与 `BaseEntity` `@TableId(type = IdType.AUTO)` 对应 |
| 字段命名 | 下划线命名，与驼峰 Java 字段通过 `map-underscore-to-camel-case: true` 自动映射 |
| 时间类型 | `DATETIME`，与 Java `LocalDateTime` 对应 |

## 四、外键说明

示例工程暂未在 DDL 中声明外键约束；`t_login_log.user_id` / `t_user_refresh_token.user_id` / `t_user.tenant_id` / `t_tenant.tenant_id` 建议按业务需要自行添加索引与外键。

## 五、定时任务相关表（Quartz + 执行日志）

定时任务默认使用 Quartz 的 JDBC 存储（`vt.task.store.type=jdbc`，由自动配置类 `VtTaskStoreAutoConfiguration` 装配），启动时执行框架内置的**幂等建表脚本**自动创建下表：脚本使用 `CREATE TABLE IF NOT EXISTS`，表已存在则跳过，**不会清空已持久化的任务数据**。

脚本位置（`vt-boot-task-starter/src/main/resources/db/`）：

| 脚本 | 用途 |
|------|------|
| `quartz-mysql_innodb.sql` | MySQL / MariaDB 的 `QRTZ_*` 建表脚本 |
| `quartz-postgres.sql` | PostgreSQL 的 `QRTZ_*` 建表脚本 |
| `quartz-h2.sql` | H2（单元测试 / 本地开发）的 `QRTZ_*` 建表脚本 |
| `vt_job_log-mysql.sql` / `vt_job_log-postgresql.sql` | 执行日志表，默认不自动创建，需要时把 `vt.task.log.initialize-schema` 置为 `always` |

| 表名 | 说明 |
|------|------|
| `QRTZ_JOB_DETAILS` | 任务明细（含序列化后的 `JobDataMap` BLOB） |
| `QRTZ_TRIGGERS` | 触发器主表（下次/上次触发时间、状态、错过触发策略） |
| `QRTZ_CRON_TRIGGERS` | Cron 触发器（cron 表达式、时区） |
| `QRTZ_SIMPLE_TRIGGERS` / `QRTZ_SIMPROP_TRIGGERS` / `QRTZ_BLOB_TRIGGERS` | 其它触发器类型的属性子表 |
| `QRTZ_CALENDARS` | 日历 |
| `QRTZ_PAUSED_TRIGGER_GRPS` | 被暂停的触发器分组 |
| `QRTZ_FIRED_TRIGGERS` | 执行中的触发器（实例级状态） |
| `QRTZ_SCHEDULER_STATE` | 调度器实例状态（集群心跳） |
| `QRTZ_LOCKS` | 行锁（集群互斥） |
| `vt_job_log` | 任务执行日志（任务名、分组、触发时间、耗时、状态、异常、实例与租户） |

要点：

- `QRTZ_*` 的 `SCHED_NAME` 取自 `spring.quartz.scheduler-name`（默认 `quartzScheduler`）：同一个库里多个应用共用 Quartz 表时，务必确认该值是否应当一致。
- 任务持久化后重启不会丢失；不要手工 `DELETE` 这些表的数据，等同于清空任务。
- `vt_job_log` 只记录执行流水，与任务定义无关，可按 `create_time` 定期清理。
