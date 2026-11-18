# API 接口

本文档描述示例工程 `vt-boot-example-admin` 对外提供的 REST 接口。所有接口统一前缀为 `/api`。

- 基准地址：`http://localhost:9086`（端口以 `application.yaml` 为准）
- 内容类型：`application/json`
- 鉴权方式：请求头携带 Sa-Token 令牌，头名由 `sa-token.token-name` 决定，示例为 `Authorization`

## 一、统一响应结构

### 单结果 `Result<T>`

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | string | 响应码（`ResponseCodeEnum` 名或业务错误码） |
| `success` | boolean | 是否成功 |
| `msg` | string | 提示信息 |
| `data` | T | 返回数据 |

```json
{
  "code": "SUCCESS",
  "success": true,
  "msg": "SUCCESS",
  "data": { }
}
```

### 多结果/分页 `MultiResult<T>`

在 `Result` 基础上增加分页字段：

| 字段 | 类型 | 说明 |
|------|------|------|
| `data` | array | 列表数据 |
| `total` | long | 总记录数 |
| `page` | int | 当前页码 |
| `size` | int | 每页记录数 |

```json
{
  "code": "SUCCESS",
  "success": true,
  "msg": "SUCCESS",
  "data": [ ],
  "total": 100,
  "page": 1,
  "size": 10
}
```

## 二、认证接口 `/api/auth`

> `/api/auth/login` 与 `/api/auth/refresh` 已在 `sa-token.yaml` 的排除路径中，无需鉴权。

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/auth/login` | 账号密码登录 | 否 |
| POST | `/api/auth/refresh` | 刷新访问令牌 | 否 |
| POST | `/api/auth/logout` | 退出登录 | 是 |
| GET | `/api/auth/me` | 获取当前登录用户信息 | 是 |

### 登录

`POST /api/auth/login`

请求体：

```json
{
  "username": "admin",
  "password": "123456",
  "deviceInfo": "Chrome/Windows"
}
```

| 字段 | 必填 | 说明 |
|------|------|------|
| `username` | 是 | 用户名 |
| `password` | 是 | 密码 |
| `deviceInfo` | 否 | 设备信息（浏览器/APP 标识） |

返回 `data`（`LoginVO`）：

| 字段 | 说明 |
|------|------|
| `token` | 访问令牌 |
| `tokenName` | 令牌请求头名称 |
| `refreshToken` | 刷新令牌 |
| `userId` / `username` / `realName` / `role` / `tenantId` | 用户基本信息 |

失败时按「账号不存在 / 账号禁用 / 账号锁定 / 密码错误」返回对应提示，并写入登录审计日志。

### 刷新令牌

`POST /api/auth/refresh`

请求体：

```json
{ "refreshToken": "xxxx" }
```

刷新成功后返回新的 `token` 与 `refreshToken`（旧刷新令牌作废）。

### 退出登录

`POST /api/auth/logout`

注销当前会话，并撤销该用户仍在有效期内的全部刷新令牌。

### 获取当前用户信息

`GET /api/auth/me`

返回 `data`（`UserInfoVO`）：`userId / username / realName / role / tenantId / email / phone / avatar`。

## 三、用户管理 `/api/system/user`

> 需要登录，并对管理员角色做权限校验。

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/system/user/page` | 分页查询用户 |
| GET | `/api/system/user/{id}` | 查询用户详情 |
| POST | `/api/system/user` | 新增用户 |
| PUT | `/api/system/user` | 修改用户 |
| DELETE | `/api/system/user/{id}` | 删除用户 |
| PUT | `/api/system/user/{id}/password?newPassword=xxx` | 重置密码 |
| PUT | `/api/system/user/{id}/status?status=0` | 修改状态 |

分页查询请求体（`UserQueryPO`）：

```json
{
  "page": 1,
  "size": 10,
  "username": "a",
  "status": 1,
  "role": "ADMIN"
}
```

新增/修改请求体（`UserFormPO`）：

```json
{
  "id": null,
  "username": "test",
  "password": "123456",
  "realName": "测试用户",
  "email": "test@example.com",
  "phone": "13800000000",
  "role": "USER",
  "status": 1,
  "remark": "备注"
}
```

| 字段 | 说明 |
|------|------|
| `id` | 新增时为空，修改时必填 |
| `password` | 新增时必填，修改时为空表示不修改 |
| `role` | 角色：`ADMIN` / `USER` |
| `status` | 状态：`1` 正常、`0` 禁用 |

## 四、租户管理 `/api/system/tenant`

> 需要登录，并对管理员角色做权限校验。

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/system/tenant/page` | 分页查询租户 |
| GET | `/api/system/tenant/{id}` | 查询租户详情 |
| POST | `/api/system/tenant` | 新增租户 |
| PUT | `/api/system/tenant` | 修改租户 |
| DELETE | `/api/system/tenant/{id}` | 删除租户 |
| PUT | `/api/system/tenant/{id}/status?status=0` | 修改状态 |

分页查询请求体（`TenantQueryPO`）：

```json
{
  "page": 1,
  "size": 10,
  "name": "租户",
  "code": "T",
  "plan": "STANDARD",
  "status": 1
}
```

新增/修改请求体（`TenantFormPO`）：

```json
{
  "id": null,
  "name": "示例租户",
  "code": "demo",
  "contactName": "张三",
  "contactEmail": "demo@example.com",
  "plan": "STANDARD",
  "maxUsers": 50,
  "maxStorageMb": 10240,
  "maxDailyTokens": 100000,
  "status": 1,
  "expireTime": "2027-01-01 00:00:00",
  "remark": "备注"
}
```

| 字段 | 说明 |
|------|------|
| `plan` | 套餐：`FREE` / `STANDARD` / `ENTERPRISE` |
| `status` | 状态：`1` 正常、`0` 禁用 |

## 五、登录日志 `/api/system/login-log`

> 需要登录，并对管理员角色做权限校验。

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/system/login-log/page` | 分页查询登录审计日志 |

请求体（`LoginLogQueryPO`）：

```json
{
  "page": 1,
  "size": 10,
  "username": "admin",
  "success": 1
}
```

| 字段 | 说明 |
|------|------|
| `username` | 用户名，模糊匹配 |
| `success` | 是否成功：`1` 成功、`0` 失败 |

返回 `data`（`LoginLogVO`）：`id / userId / username / loginType / success / failReason / ip / deviceInfo / createTime`。

## 六、接口文档

启动应用后可通过以下地址查看在线接口文档：

- Swagger UI：`/swagger-ui.html`
- OpenAPI JSON：`/v3/api-docs`

文档信息（标题、版本、描述、联系人）由 `vt.boot.openapi.*` 配置，详见 [配置说明](./configuration.md)。
