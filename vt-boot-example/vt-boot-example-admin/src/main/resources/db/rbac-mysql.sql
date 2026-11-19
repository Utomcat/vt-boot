-- ---------------------------------------------------------------------------
-- VT-Boot RBAC 初始化脚本 (MySQL / MariaDB)
--
-- 用途: 为 vt-boot-example-admin 补齐基于角色-菜单的权限模型, 支撑后台管理系统的
--       角色管理、菜单管理与前端动态菜单/按钮权限。
--
-- 特性: 幂等。建表使用 CREATE TABLE IF NOT EXISTS, 种子数据使用 INSERT IGNORE,
--       可重复执行而不会覆盖已调整过的数据。
--
-- 执行方式:
--   mysql -h<host> -P<port> -u<user> -p <database> < rbac-mysql.sql
--
-- 说明:
--   1. t_user.role 仍保留为"主角色编码", 用于兼容存量数据; 真正的角色来源是 t_user_role。
--   2. 菜单 type: DIR-目录 MENU-页面 BUTTON-按钮; BUTTON 行通过 permission 提供权限码。
--   3. 超级管理员角色 ADMIN 的权限码在代码中直接认定为 "*"(全部权限), 无需逐条绑定按钮,
--      但仍绑定全部菜单, 以保证前端菜单树完整。
-- ---------------------------------------------------------------------------

-- ---------------------------------------------------------------------------
-- 1. 表结构
-- ---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `t_role`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`   BIGINT          NOT NULL DEFAULT 1 COMMENT '租户ID',
    `code`        VARCHAR(64)     NOT NULL COMMENT '角色编码: ADMIN / USER / 自定义',
    `name`        VARCHAR(64)     NOT NULL COMMENT '角色名称',
    `description` VARCHAR(255)             DEFAULT NULL COMMENT '角色描述',
    `builtin`     TINYINT         NOT NULL DEFAULT 0 COMMENT '是否内置角色: 1-是(不可删除) 0-否',
    `sort`        INT             NOT NULL DEFAULT 0 COMMENT '排序值, 越小越靠前',
    `status`      TINYINT         NOT NULL DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人ID',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人ID',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`tenant_id`, `code`),
    KEY `idx_role_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

CREATE TABLE IF NOT EXISTS `t_menu`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`   BIGINT          NOT NULL DEFAULT 1 COMMENT '租户ID',
    `parent_id`   BIGINT          NOT NULL DEFAULT 0 COMMENT '父菜单ID, 0 表示顶级',
    `name`        VARCHAR(64)     NOT NULL COMMENT '菜单名称',
    `type`        VARCHAR(16)     NOT NULL DEFAULT 'MENU' COMMENT '类型: DIR-目录 MENU-页面 BUTTON-按钮',
    `path`        VARCHAR(200)             DEFAULT NULL COMMENT '路由地址(目录/页面使用)',
    `component`   VARCHAR(200)             DEFAULT NULL COMMENT '前端组件标识(页面使用)',
    `icon`        VARCHAR(64)              DEFAULT NULL COMMENT '图标名称(Element Plus 图标)',
    `permission`  VARCHAR(128)             DEFAULT NULL COMMENT '权限码, 如 system:user:add',
    `sort`        INT             NOT NULL DEFAULT 0 COMMENT '排序值, 越小越靠前',
    `visible`     TINYINT         NOT NULL DEFAULT 1 COMMENT '是否在侧边栏显示: 1-显示 0-隐藏',
    `status`      TINYINT         NOT NULL DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人ID',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人ID',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_menu_parent` (`parent_id`),
    KEY `idx_menu_permission` (`permission`),
    KEY `idx_menu_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单与权限表';

CREATE TABLE IF NOT EXISTS `t_user_role`
(
    `id`      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `role_id` BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_role_role` (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户-角色关联表';

CREATE TABLE IF NOT EXISTS `t_role_menu`
(
    `id`      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT UNSIGNED NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
    KEY `idx_role_menu_menu` (`menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色-菜单关联表';

-- ---------------------------------------------------------------------------
-- 2. 内置角色
-- ---------------------------------------------------------------------------

INSERT IGNORE INTO `t_role` (`id`, `tenant_id`, `code`, `name`, `description`, `builtin`, `sort`, `status`)
VALUES (1, 1, 'ADMIN', '超级管理员', '拥有全部菜单与权限, 不可删除', 1, 1, 1),
       (2, 1, 'USER', '普通用户', '仅可访问工作台与任务调度', 1, 2, 1);

-- ---------------------------------------------------------------------------
-- 3. 内置菜单 (目录 / 页面)
-- ---------------------------------------------------------------------------

INSERT IGNORE INTO `t_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `icon`, `permission`, `sort`, `visible`, `status`)
VALUES (1, 0, '工作台', 'MENU', '/dashboard', 'dashboard/Dashboard', 'Odometer', NULL, 1, 1, 1),
       (10, 0, '系统管理', 'DIR', '/system', NULL, 'Setting', NULL, 10, 1, 1),
       (11, 10, '用户管理', 'MENU', '/system/user', 'system/user/UserList', 'User', 'system:user:list', 1, 1, 1),
       (12, 10, '租户管理', 'MENU', '/system/tenant', 'system/tenant/TenantList', 'OfficeBuilding', 'system:tenant:list', 2, 1, 1),
       (13, 10, '登录日志', 'MENU', '/system/login-log', 'system/log/LoginLogList', 'Document', 'system:login-log:list', 3, 1, 1),
       (14, 10, '角色管理', 'MENU', '/system/role', 'system/role/RoleList', 'UserFilled', 'system:role:list', 4, 1, 1),
       (15, 10, '菜单管理', 'MENU', '/system/menu', 'system/menu/MenuList', 'Menu', 'system:menu:list', 5, 1, 1),
       (20, 0, '任务调度', 'DIR', '/task', NULL, 'Timer', NULL, 20, 1, 1),
       (21, 20, '任务管理', 'MENU', '/task/job', 'task/JobList', 'List', 'task:job:list', 1, 1, 1),
       (22, 20, '执行日志', 'MENU', '/task/log', 'task/JobLogList', 'Tickets', 'task:log:list', 2, 1, 1);

-- ---------------------------------------------------------------------------
-- 4. 内置按钮权限
-- ---------------------------------------------------------------------------

INSERT IGNORE INTO `t_menu` (`id`, `parent_id`, `name`, `type`, `permission`, `sort`, `visible`, `status`)
VALUES (111, 11, '用户新增', 'BUTTON', 'system:user:add', 1, 0, 1),
       (112, 11, '用户编辑', 'BUTTON', 'system:user:edit', 2, 0, 1),
       (113, 11, '用户删除', 'BUTTON', 'system:user:delete', 3, 0, 1),
       (114, 11, '重置密码', 'BUTTON', 'system:user:reset', 4, 0, 1),
       (115, 11, '用户启停', 'BUTTON', 'system:user:status', 5, 0, 1),
       (121, 12, '租户新增', 'BUTTON', 'system:tenant:add', 1, 0, 1),
       (122, 12, '租户编辑', 'BUTTON', 'system:tenant:edit', 2, 0, 1),
       (123, 12, '租户删除', 'BUTTON', 'system:tenant:delete', 3, 0, 1),
       (124, 12, '租户启停', 'BUTTON', 'system:tenant:status', 4, 0, 1),
       (141, 14, '角色新增', 'BUTTON', 'system:role:add', 1, 0, 1),
       (142, 14, '角色编辑', 'BUTTON', 'system:role:edit', 2, 0, 1),
       (143, 14, '角色删除', 'BUTTON', 'system:role:delete', 3, 0, 1),
       (144, 14, '菜单授权', 'BUTTON', 'system:role:assign', 4, 0, 1),
       (151, 15, '菜单新增', 'BUTTON', 'system:menu:add', 1, 0, 1),
       (152, 15, '菜单编辑', 'BUTTON', 'system:menu:edit', 2, 0, 1),
       (153, 15, '菜单删除', 'BUTTON', 'system:menu:delete', 3, 0, 1),
       (211, 21, '任务新增', 'BUTTON', 'task:job:add', 1, 0, 1),
       (212, 21, '任务编辑', 'BUTTON', 'task:job:edit', 2, 0, 1),
       (213, 21, '任务删除', 'BUTTON', 'task:job:delete', 3, 0, 1),
       (214, 21, '立即执行', 'BUTTON', 'task:job:trigger', 4, 0, 1),
       (221, 22, '日志清理', 'BUTTON', 'task:log:clean', 1, 0, 1);

-- ---------------------------------------------------------------------------
-- 5. 角色-菜单授权
-- ---------------------------------------------------------------------------

-- 超级管理员(ADMIN): 绑定全部菜单与按钮, 便于前端渲染菜单树; 其权限码在代码中视为 "*"
INSERT IGNORE INTO `t_role_menu` (`role_id`, `menu_id`)
SELECT 1, `id`
FROM `t_menu`
WHERE `deleted` = 0;

-- 普通用户(USER): 工作台 + 任务调度(含任务按钮与日志清理)
INSERT IGNORE INTO `t_role_menu` (`role_id`, `menu_id`)
VALUES (2, 1),
       (2, 20),
       (2, 21),
       (2, 22),
       (2, 211),
       (2, 212),
       (2, 213),
       (2, 214),
       (2, 221);

-- ---------------------------------------------------------------------------
-- 6. 存量用户角色绑定 (兼容 t_user.role)
--    role = 'ADMIN' -> 角色 1(超级管理员); 其它 -> 角色 2(普通用户)
-- ---------------------------------------------------------------------------

INSERT IGNORE INTO `t_user_role` (`user_id`, `role_id`)
SELECT `id`, 1
FROM `t_user`
WHERE `deleted` = 0
  AND `role` = 'ADMIN';

INSERT IGNORE INTO `t_user_role` (`user_id`, `role_id`)
SELECT `id`, 2
FROM `t_user`
WHERE `deleted` = 0
  AND (`role` IS NULL OR `role` <> 'ADMIN');
