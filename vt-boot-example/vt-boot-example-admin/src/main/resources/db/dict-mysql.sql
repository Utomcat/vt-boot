-- ---------------------------------------------------------------------------
-- VT-Boot 数据字典初始化脚本 (MySQL / MariaDB)
--
-- 用途: 为 vt-boot-example-admin 增加系统字典能力, 包括字典类型(t_dict_type)与
--       字典数据(t_dict_data)两张表, 以及后台菜单/按钮权限入口。
--
-- 特性: 幂等。建表使用 CREATE TABLE IF NOT EXISTS, 种子数据使用 INSERT IGNORE,
--       可重复执行而不会覆盖已调整过的数据。
--
-- 前置: 建议先执行 rbac-mysql.sql, 以保证内置角色(ADMIN)与系统管理目录(菜单 id=10)存在。
--
-- 执行方式:
--   mysql -h<host> -P<port> -u<user> -p <database> < dict-mysql.sql
-- ---------------------------------------------------------------------------

-- ---------------------------------------------------------------------------
-- 1. 表结构
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `t_dict_type`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`   BIGINT          NOT NULL DEFAULT 1 COMMENT '租户ID',
    `dict_name`   VARCHAR(128)    NOT NULL COMMENT '字典名称, 如 用户状态',
    `dict_type`   VARCHAR(128)    NOT NULL COMMENT '字典类型编码, 业务引用的唯一标识, 如 sys_user_status',
    `status`      TINYINT         NOT NULL DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
    `remark`      VARCHAR(255)             DEFAULT NULL COMMENT '备注',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人ID',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人ID',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_type` (`tenant_id`, `dict_type`),
    KEY `idx_dict_type_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='字典类型表';

CREATE TABLE IF NOT EXISTS `t_dict_data`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`   BIGINT          NOT NULL DEFAULT 1 COMMENT '租户ID',
    `dict_type`   VARCHAR(128)    NOT NULL COMMENT '所属字典类型编码, 关联 t_dict_type.dict_type',
    `dict_label`  VARCHAR(255)    NOT NULL COMMENT '字典标签, 展示文本, 如 正常',
    `dict_value`  VARCHAR(255)    NOT NULL COMMENT '字典键值, 存储与判断使用, 如 1',
    `dict_sort`   INT             NOT NULL DEFAULT 0 COMMENT '排序值, 越小越靠前',
    `tag_type`    VARCHAR(32)              DEFAULT NULL COMMENT '标签样式: primary/success/warning/danger/info',
    `css_class`   VARCHAR(128)             DEFAULT NULL COMMENT '额外样式类名',
    `is_default`  TINYINT         NOT NULL DEFAULT 0 COMMENT '是否默认选中: 1-是 0-否',
    `status`      TINYINT         NOT NULL DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
    `remark`      VARCHAR(255)             DEFAULT NULL COMMENT '备注',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人ID',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人ID',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_value` (`tenant_id`, `dict_type`, `dict_value`),
    KEY `idx_dict_data_type` (`dict_type`),
    KEY `idx_dict_data_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='字典数据表';

-- ---------------------------------------------------------------------------
-- 2. 菜单与按钮权限 (系统管理 -> 字典管理)
-- ---------------------------------------------------------------------------

INSERT IGNORE INTO `t_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `icon`, `permission`, `sort`, `visible`, `status`)
VALUES (16, 10, '字典管理', 'MENU', '/system/dict', 'system/dict/DictTypeList', 'Collection', 'system:dict:list', 6, 1, 1);

INSERT IGNORE INTO `t_menu` (`id`, `parent_id`, `name`, `type`, `permission`, `sort`, `visible`, `status`)
VALUES (161, 16, '字典新增', 'BUTTON', 'system:dict:add', 1, 0, 1),
       (162, 16, '字典编辑', 'BUTTON', 'system:dict:edit', 2, 0, 1),
       (163, 16, '字典删除', 'BUTTON', 'system:dict:delete', 3, 0, 1),
       (164, 16, '字典启停', 'BUTTON', 'system:dict:status', 4, 0, 1);

-- 超级管理员(ADMIN, role_id=1)绑定字典菜单与按钮, 便于前端渲染菜单树; 其权限码在代码中视为 "*"
INSERT IGNORE INTO `t_role_menu` (`role_id`, `menu_id`)
VALUES (1, 16),
       (1, 161),
       (1, 162),
       (1, 163),
       (1, 164);

-- ---------------------------------------------------------------------------
-- 3. 内置字典类型
-- ---------------------------------------------------------------------------

INSERT IGNORE INTO `t_dict_type` (`tenant_id`, `dict_name`, `dict_type`, `status`, `remark`)
VALUES (1, '系统开关状态', 'sys_common_status', 1, '通用启停状态'),
       (1, '用户状态', 'sys_user_status', 1, '用户启用/禁用状态'),
       (1, '性别', 'sys_user_gender', 1, '用户性别'),
       (1, '是否', 'sys_yes_no', 1, '通用是否标识'),
       (1, '登录结果', 'sys_login_result', 1, '登录成功/失败');

-- ---------------------------------------------------------------------------
-- 4. 内置字典数据
-- ---------------------------------------------------------------------------

INSERT IGNORE INTO `t_dict_data` (`tenant_id`, `dict_type`, `dict_label`, `dict_value`, `dict_sort`, `tag_type`, `is_default`, `status`, `remark`)
VALUES (1, 'sys_common_status', '正常', '1', 1, 'success', 1, 1, NULL),
       (1, 'sys_common_status', '禁用', '0', 2, 'danger', 0, 1, NULL),
       (1, 'sys_user_status', '正常', '1', 1, 'success', 1, 1, NULL),
       (1, 'sys_user_status', '禁用', '0', 2, 'danger', 0, 1, NULL),
       (1, 'sys_user_gender', '男', '1', 1, 'primary', 0, 1, NULL),
       (1, 'sys_user_gender', '女', '2', 2, 'danger', 0, 1, NULL),
       (1, 'sys_user_gender', '未知', '0', 3, 'info', 0, 1, NULL),
       (1, 'sys_yes_no', '是', 'Y', 1, 'success', 0, 1, NULL),
       (1, 'sys_yes_no', '否', 'N', 2, 'info', 0, 1, NULL),
       (1, 'sys_login_result', '成功', '1', 1, 'success', 0, 1, NULL),
       (1, 'sys_login_result', '失败', '0', 2, 'danger', 0, 1, NULL);
