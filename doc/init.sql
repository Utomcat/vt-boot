drop table if exists account_info;
create table account_info
(
    id          varchar(80)                            not null comment '数据主键' primary key,
    user_name   varchar(100)                           not null comment '用户名',
    password    varchar(100)                           not null comment '密码',
    tenant_id   varchar(10)  default '0000000001'      not null comment '租户ID',
    status      int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark      varchar(500) default '-'               null comment '备注',
    create_by   varchar(80)  default '1'               not null comment '数据创建人ID',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by   varchar(80)  default '1'               not null comment '数据更新人',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '登录账户信息表'
  charset utf8;

INSERT INTO account_info (id, user_name, password, tenant_id, status, remark, create_by, create_time, update_by,
                          update_time)
VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '0000000001', 1, '-', '1', '2026-03-01 18:39:45', '1',
        '2026-03-01 18:39:45');

drop table if exists department_info;
create table department_info
(
    id          varchar(80)                            not null comment '数据主键' primary key,
    name        varchar(80)                            not null comment '部门名称',
    code        varchar(80)                            not null comment '部门编号',
    tenant_id   varchar(10)  default '0000000001'      not null comment '租户ID',
    status      int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark      varchar(500) default '-'               null comment '备注',
    create_by   varchar(80)  default '1'               not null comment '数据创建人ID',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by   varchar(80)  default '1'               not null comment '数据更新人ID',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '部门信息表'
  charset utf8;

drop table if exists department_account_connection;
create table department_account_connection
(
    id            varchar(80)                            not null comment '数据主键' primary key,
    account_id    varchar(80)                            not null comment '账户ID',
    department_id varchar(80)                            not null comment '部门ID',
    tenant_id     varchar(10)  default '0000000001'      not null comment '租户ID',
    status        int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark        varchar(500) default '-'               null comment '备注',
    create_by     varchar(80)  default '1'               not null comment '数据创建人ID',
    create_time   timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by     varchar(80)  default '1'               not null comment '数据更新人ID',
    update_time   timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '部门账号关联关系表'
  charset utf8;

drop table if exists role_info;
create table role_info
(
    id          varchar(80)                            not null comment '数据主键' primary key,
    name        varchar(80)                            not null comment '角色名称',
    code        varchar(80)                            not null comment '角色编号',
    tenant_id   varchar(10)  default '0000000001'      not null comment '租户ID',
    status      tinyint      default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark      varchar(500) default '-'               null comment '备注',
    create_by   varchar(80)  default '1'               not null comment '数据创建人ID',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by   varchar(80)  default '1'               not null comment '数据更新人ID',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '角色信息表'
  charset utf8;
INSERT INTO role_info (id, name, code, tenant_id, status, remark, create_by, create_time, update_by, update_time)
VALUES ('1', '超级管理员', 'super:admin', '0000000001', 1, '-', '1', '2026-03-05 02:04:21', '1', '2026-03-05 02:04:21');
INSERT INTO role_info (id, name, code, tenant_id, status, remark, create_by, create_time, update_by, update_time)
VALUES ('2', '管理员', 'admin', '0000000001', 1, '-', '1', '2026-03-05 02:04:21', '1', '2026-03-05 02:04:21');
INSERT INTO role_info (id, name, code, tenant_id, status, remark, create_by, create_time, update_by, update_time)
VALUES ('3', '游客', 'tourists', '0000000001', 1, '-', '1', '2026-03-05 02:04:21', '1', '2026-03-05 02:04:21');


drop table if exists account_role_connection;
create table account_role_connection
(
    id          varchar(80)                            not null comment '数据主键' primary key,
    account_id  varchar(80)                            not null comment '账户ID',
    role_id     varchar(80)                            not null comment '角色ID',
    tenant_id   varchar(10)  default '0000000001'      not null comment '租户ID',
    status      tinyint      default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark      varchar(500) default '-'               null comment '备注',
    create_by   varchar(80)  default '1'               not null comment '数据创建人ID',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by   varchar(80)  default '1'               not null comment '数据更新人ID',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '账户角色关联关系表'
  charset utf8;

drop table if exists permission_info;
create table permission_info
(
    id          varchar(80)                            not null comment '数据主键' primary key,
    name        varchar(80)                            not null comment '权限名称',
    code        varchar(80)                            not null comment '权限编号',
    type        tinyint      default 1                 not null comment '权限类型（1: 菜单; 2: 按钮; 3: 功能; 4: 其他;）',
    tenant_id   varchar(10)  default '0000000001'      not null comment '租户ID',
    status      tinyint      default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark      varchar(500) default '-'               null comment '备注',
    create_by   varchar(80)  default '1'               not null comment '数据创建人ID',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by   varchar(80)  default '1'               not null comment '数据更新人ID',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '权限信息表'
  charset utf8;

drop table if exists role_permission_connection;
create table role_permission_connection
(
    id            varchar(80)                            not null comment '数据主键' primary key,
    role_id       varchar(80)                            not null comment '角色ID',
    permission_id varchar(80)                            not null comment '权限ID',
    tenant_id     varchar(10)  default '0000000001'      not null comment '租户ID',
    status        tinyint      default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark        varchar(500) default '-'               null comment '备注',
    create_by     varchar(80)  default '1'               not null comment '数据创建人ID',
    create_time   timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by     varchar(80)  default '1'               not null comment '数据更新人ID',
    update_time   timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '角色权限关联关系表'
  charset utf8;

drop table if exists sys_dict_type;
create table sys_dict_type
(
    id          varchar(80)                            not null comment '数据主键' primary key,
    name        varchar(100)                           not null comment '字典类型名',
    code        varchar(100)                           not null comment '字典类型编码',
    tenant_id   varchar(10)  default '0000000001'      not null comment '租户ID',
    status      int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark      varchar(500) default '-'               null comment '备注',
    create_by   varchar(80)  default '1'               not null comment '数据创建人ID',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by   varchar(80)  default '1'               not null comment '数据更新人',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '字典类型表'
  charset utf8;

drop table if exists sys_dict;
create table sys_dict
(
    id           varchar(80)                            not null comment '数据主键' primary key,
    dict_type_id varchar(80)                            not null comment '字典类型ID',
    name         varchar(100)                           not null comment '字典值名称',
    code         varchar(100)                           not null comment '字典值编码',
    value        varchar(100)                           not null comment '字典值',
    tenant_id    varchar(10)  default '0000000001'      not null comment '租户ID',
    status       int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark       varchar(500) default '-'               null comment '备注',
    create_by    varchar(80)  default '1'               not null comment '数据创建人ID',
    create_time  timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by    varchar(80)  default '1'               not null comment '数据更新人',
    update_time  timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '字典表'
  charset utf8;

drop table if exists user_info;
create table user_info
(
    id          varchar(100)                           not null comment '数据主键' primary key,
    name        varchar(100)                           not null comment '用户名称',
    nick_name   varchar(100)                           not null comment '用户昵称',
    avatar      varchar(500)                           not null comment '用户头像',
    sex         int          default 1                 not null comment '性别',
    email       varchar(100) default '-'               not null comment '邮箱',
    phone       varchar(20)  default '-'               not null comment '手机',
    status      int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    tenant_id   varchar(10)  default '0000000001'      not null comment '租户ID',
    remark      varchar(100) default '-'               not null comment '备注',
    create_by   varchar(100) default '1'               not null comment '数据创建人',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by   varchar(100) default '1'               not null comment '数据更新人',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '用户信息表'
  charset utf8;

drop table if exists account_user_connection;
create table account_user_connection
(
    id          varchar(100)                           not null comment '数据主键' primary key,
    account_id  varchar(100)                           not null comment '账户ID',
    user_id     varchar(100)                           not null comment '用户ID',
    status      int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    tenant_id   varchar(10)  default '0000000001'      not null comment '租户ID',
    remark      varchar(100) default '-'               not null comment '备注',
    create_by   varchar(100) default '1'               not null comment '数据创建人',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by   varchar(100) default '1'               not null comment '数据更新人',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '账户用户信息关联关系表'
  charset utf8;

drop table if exists app_log;
create table app_log
(
    id          varchar(80)                            not null comment '数据ID' primary key,
    column_one  varchar(10)  default '-'               not null comment '列一',
    column_two  varchar(100) default '-'               not null comment '列二',
    status      int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark      varchar(500) default '-'               null comment '备注',
    create_by   varchar(80)  default '1'               not null comment '创建人',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by   varchar(80)  default '1'               not null comment '更新人',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '更新时间'
) engine = InnoDB comment '日志记录表'
  charset utf8;

drop table if exists app_service_table;
create table app_service_table
(
    id           varchar(80)                            not null comment '数据ID' primary key,
    service_name varchar(100) default '-'               not null comment '业务名称',
    service_data varchar(20)  default '-'               not null comment '业务数据',
    status       int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark       varchar(500) default '-'               null comment '备注',
    create_by    varchar(80)  default '1'               not null comment '创建人',
    create_time  timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by    varchar(80)  default '1'               not null comment '更新人',
    update_time  timestamp    default CURRENT_TIMESTAMP not null comment '更新时间'
) engine = InnoDB comment '测试业务表'
  charset utf8;

