# 数据库设计

本文说明示例工程 `vt-boot-example-admin` 涉及的数据库表结构。

> 仓库中未提交 `.sql` 建表脚本，下方 DDL 为基于 `src/main/java` 中的实体类整理的 **参考建表语句**，建表时请按实际环境调整字段、索引与字符集。

## 一、表清单

| 表名 | 说明 |
|------|------|
| `t_user` | 用户表（角色：ADMIN / USER） |
| `t_tenant` | 租户表（套餐：FREE / STANDARD / ENTERPRISE） |
| `t_login_log` | 登录审计流水表 |
| `t_user_refresh_token` | 用户刷新令牌表 |
| `operation_log_info` | 操作记录表（`@OperationRecord` 默认写入表，非强制，按需创建） |

所有表均继承公共字段：`id / tenantId / deleted / createBy / createTime / updateBy / updateTime`，其中 `deleted` 为逻辑删除标记（`0` 正常、`1` 已删除）。

## 二、建表参考

### 1. t_user — 用户表

```sql
CREATE TABLE IF NOT EXISTS `t_user` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`     BIGINT UNSIGNED COMMENT '租户ID',
    `username`      VARCHAR(64)  NOT NULL COMMENT '登录名',
    `password`      VARCHAR(255) NOT NULL COMMENT '加密密码(BCrypt)',
    `real_name`     VARCHAR(64)  COMMENT '真实姓名',
    `email`         VARCHAR(128) COMMENT '邮箱',
    `phone`         VARCHAR(32)  COMMENT '手机号',
    `avatar`        VARCHAR(255) COMMENT '头像相对路径',
    `role`          VARCHAR(32)  COMMENT '角色: ADMIN / USER',
    `status`        TINYINT      DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
    `deleted`       TINYINT      DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除',
    `create_by`     BIGINT UNSIGNED COMMENT '创建人ID',
    `create_time`   DATETIME     COMMENT '创建时间',
    `update_by`     BIGINT UNSIGNED COMMENT '更新人ID',
    `update_time`   DATETIME     COMMENT '更新时间',
    `last_login_time` DATETIME  COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(64)  COMMENT '最后登录IP',
    `remark`        VARCHAR(255) COMMENT '管理员备注',
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
    `tenant_id`      BIGINT UNSIGNED COMMENT '租户ID',
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

### 5. operation_log_info — 操作记录表（可选）

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
