drop table if exists tenant_info;
create table tenant_info
(
    id          varchar(80)                            not null comment '数据主键' primary key,
    name        varchar(80)                            not null comment '租户名称',
    code        varchar(80)                            not null comment '租户编号',
    tenant_id   varchar(10)  default '0000000001'      not null comment '租户ID',
    status      int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    remark      varchar(500) default '-'               null comment '备注',
    create_by   varchar(80)  default '1'               not null comment '数据创建人ID',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by   varchar(80)  default '1'               not null comment '数据更新人',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '租户信息表'
  charset utf8;

INSERT INTO tenant_info (id, name, code, status, remark, create_by, update_by)
VALUES ('2040985303637278720', '默认租户', '0000000001', 1, '系统默认的租户数据', '1', '1');


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
VALUES ('0000000000000000001', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '0000000001', 1, '-', '1', '2026-03-01 18:39:45', '1',
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

INSERT INTO department_info (id, name, code, parent_id, parent_ids, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2029747360659869698', '最上级部门', '1', '-', '-', '0000000001', 1, '最上层部门', '1', '2026-03-06 10:26:49', '1', '2026-03-06 10:26:49');
INSERT INTO department_info (id, name, code, parent_id, parent_ids, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2029748395671527426', '子级部门一', '1-001', '2029747360659869698', '2029747360659869698', '0000000001', 1, '子级部门一', '1', '2026-03-06 10:37:44', '1', '2026-03-06 10:37:44');
INSERT INTO department_info (id, name, code, parent_id, parent_ids, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2029789851002052609', '子级部门的子级部门1', '1-001-0002', '2029748395671527426', '2029747360659869698,2029748395671527426', '0000000001', 1, '子级部门的子级部门一', '1', '2026-03-06 13:19:33', '1', '2026-03-06 13:35:04');


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

INSERT INTO account_role_connection (id, account_id, role_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041677923742289921', '0000000000000000001', '1', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');


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

INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041670702639779842', '新增账户信息权限', 'add:account', 3, '0000000001', 1, '新增账户信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041670783464017922', '修改账户信息权限', 'update:account', 3, '0000000001', 1, '修改账户信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041670868776161282', '删除账户信息权限', 'delete:account', 3, '0000000001', 1, '删除账户信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041671188898025473', '新增角色信息权限', 'add:role', 3, '0000000001', 1, '新增角色信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041671247416954881', '修改角色信息权限', 'update:role', 3, '0000000001', 1, '修改角色信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041671305927495681', '删除角色信息权限', 'delete:role', 3, '0000000001', 1, '删除角色信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041671526380113922', '新增用户信息权限', 'add:user', 3, '0000000001', 1, '新增用户信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041671579412893698', '修改用户信息权限', 'update:user', 3, '0000000001', 1, '修改用户信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041671637499809794', '删除用户信息权限', 'delete:user', 3, '0000000001', 1, '删除用户信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041672013674352641', '绑定账户用户信息权限', 'bundled:account:user', 3, '0000000001', 1, '绑定账户用户信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041672084717473793', '绑定账户角色信息权限', 'bundled:account:role', 3, '0000000001', 1, '绑定账户角色信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041672167328485377', '绑定角色权限信息权限', 'bundled:role:permission', 3, '0000000001', 1, '绑定角色权限信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041672834700972033', '查询账户信息权限', 'query:account', 3, '0000000001', 1, '查询账户信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041672896206245890', '查询角色信息权限', 'query:role', 3, '0000000001', 1, '查询角色信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041673028351987713', '查询用户信息权限', 'query:user', 3, '0000000001', 1, '查询用户信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041673208547676161', '新增权限信息权限', 'add:permission', 3, '0000000001', 1, '新增权限信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041673263354646529', '删除权限信息权限', 'delete:permission', 3, '0000000001', 1, '删除权限信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041673315192049666', '修改权限信息权限', 'update:permission', 3, '0000000001', 1, '修改权限信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041673359081246722', '查询权限信息权限', 'query:permission', 3, '0000000001', 1, '查询权限信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041673809671131138', '新增部门信息权限', 'add:department', 3, '0000000001', 1, '新增部门信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041673871377731586', '修改部门信息权限', 'update:department', 3, '0000000001', 1, '修改部门信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041673924628615170', '删除部门信息权限', 'delete:department', 3, '0000000001', 1, '删除部门信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041673973555171329', '查询部门信息权限', 'query:department', 3, '0000000001', 1, '查询部门信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041674168514809857', '绑定部门和账户信息权限', 'bundled:department:account', 3, '0000000001', 1, '绑定部门和账户信息权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041674479446953985', '新增字典类型权限', 'add:dict:type', 3, '0000000001', 1, '新增字典类型权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041674535608684545', '删除字典类型权限', 'delete:dict:type', 3, '0000000001', 1, '删除字典类型权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041674598821040129', '修改字典类型权限', 'update:dict:type', 3, '0000000001', 1, '修改字典类型权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041674668974968834', '查询字典类型权限', 'query:dict:type', 3, '0000000001', 1, '查询字典类型权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041674797379391489', '查询字典数据权限', 'query:dict', 3, '0000000001', 1, '查询字典数据权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041674845546778626', '新增字典数据权限', 'add:dict', 3, '0000000001', 1, '新增字典数据权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041674892040638465', '删除字典数据权限', 'delete:dict', 3, '0000000001', 1, '删除字典数据权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041674942984654850', '修改字典数据权限', 'update:dict', 3, '0000000001', 1, '修改字典数据权限', '0000000000000000001', '2026-04-08 08:13:23', '0000000000000000001', '2026-04-08 08:13:23');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041683870791454722', '解绑账户用户信息权限', 'unbundled:account:user', 3, '0000000001', 1, '解绑账户用户信息权限', '0000000000000000001', '2026-04-08 09:03:49', '0000000000000000001', '2026-04-08 09:03:49');
INSERT INTO permission_info (id, name, code, type, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041685883180748801', '查询账户用户关联信息权限', 'query:account:user', 3, '0000000001', 1, '查询账户用户关联信息权限', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');


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

INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165838426114', '1', '2041670702639779842', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165838426115', '1', '2041670783464017922', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165838426116', '1', '2041670868776161282', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534977', '1', '2041671188898025473', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534978', '1', '2041671247416954881', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534979', '1', '2041671305927495681', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534980', '1', '2041671526380113922', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534981', '1', '2041671579412893698', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534982', '1', '2041671637499809794', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534983', '1', '2041672013674352641', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534984', '1', '2041672084717473793', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534985', '1', '2041672167328485377', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534986', '1', '2041672834700972033', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534987', '1', '2041672896206245890', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534988', '1', '2041673028351987713', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534989', '1', '2041673208547676161', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534990', '1', '2041673263354646529', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534991', '1', '2041673315192049666', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534992', '1', '2041673359081246722', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165905534993', '1', '2041673809671131138', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449538', '1', '2041673871377731586', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449539', '1', '2041673924628615170', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449540', '1', '2041673973555171329', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449541', '1', '2041674168514809857', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449542', '1', '2041674479446953985', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449543', '1', '2041674535608684545', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449544', '1', '2041674598821040129', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449545', '1', '2041674668974968834', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449546', '1', '2041674797379391489', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449547', '1', '2041674845546778626', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449548', '1', '2041674892040638465', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449549', '1', '2041674942984654850', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449550', '1', '2041683870791454722', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');
INSERT INTO role_permission_connection (id, role_id, permission_id, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2041688165968449551', '1', '2041685883180748801', '0000000001', 1, '-', '0000000000000000001', '2026-04-08 09:11:44', '0000000000000000001', '2026-04-08 09:11:44');


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

INSERT INTO sys_dict_type (id, name, code, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2029425651004133377', 'sex 类型', 'sex', '0000000001', 1, '性别字典类型', '1', '2026-03-05 13:15:06', '0000000000000000001', '2026-03-06 16:37:58');


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

INSERT INTO sys_dict (id, dict_type_id, name, code, value, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2029850960790790145', '2029425651004133377', '男', 'man', '1', '0000000001', 1, '性别 男', '1', '2026-03-06 17:22:18', '1', '2026-03-06 17:22:18');
INSERT INTO sys_dict (id, dict_type_id, name, code, value, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2029851031980711938', '2029425651004133377', '女', 'woman', '2', '0000000001', 1, '性别 女', '1', '2026-03-06 17:22:18', '1', '2026-03-06 17:22:18');
INSERT INTO sys_dict (id, dict_type_id, name, code, value, tenant_id, status, remark, create_by, create_time, update_by, update_time) VALUES ('2029851113165660162', '2029425651004133377', '未知', 'none', '3', '0000000001', 1, '性别 未知', '1', '2026-03-06 17:22:18', '1', '2026-03-06 17:22:18');


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
INSERT INTO user_info (id, name, nick_name, avatar, sex, email, phone, status, tenant_id, remark, create_by,
                       create_time, update_by, update_time)
VALUES ('2040242542824079361', '超级管理员', '超级管理员', 'https://avatars.githubusercontent.com/u/8223568', 3,
        'ggqryd.l7m@126.com', '067 9277 7798', 1, '0000000001', '这是系统的超级管理员用户信息', '0000000000000000001',
        '2026-04-04 09:29:09', '0000000000000000001', '2026-04-04 09:29:09');


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
INSERT INTO account_user_connection (id, account_id, user_id, status, tenant_id, remark, create_by, create_time,
                                     update_by, update_time)
VALUES ('2040242730464657409', '0000000000000000001', '2040242542824079361', 1, '0000000001', '-',
        '0000000000000000001', '2026-04-04 09:29:09', '0000000000000000001', '2026-04-04 09:29:09');

drop table if exists operation_log_info;
create table operation_log_info
(
    id          varchar(100)                           not null comment '数据主键' primary key,
    name        varchar(100)                           not null comment '操作名',
    type        varchar(100)                           not null comment '操作类型',
    param       varchar(1000)                          not null comment '操作参数',
    operator    varchar(100)                           not null comment '操作人',
    status      int          default 1                 not null comment '数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）',
    tenant_id   varchar(10)  default '0000000001'      not null comment '租户ID',
    remark      varchar(100) default '-'               not null comment '备注',
    create_by   varchar(100) default '1'               not null comment '数据创建人',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '数据创建时间',
    update_by   varchar(100) default '1'               not null comment '数据更新人',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '数据更新时间'
) engine = InnoDB comment '操作日志记录表'
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
) engine = InnoDB comment '测试日志记录表'
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

