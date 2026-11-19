# 系统字典

本文说明 `vt-boot` 示例工程中「系统字典」能力的**读写分层设计**、**缓存与失效约定**，以及未来是否/何时**下沉为独立 Starter** 的判定条件与迁移步骤。

> 适用代码：`vt-boot-example-admin`，包 `com.ranyk.vt.boot.example.system.dict`。
> 表结构见 [数据库设计](./database.md) 的「字典表」章节；建表与种子数据见 `vt-boot-example-admin/src/main/resources/db/dict-mysql.sql`。

## 一、定位与范围

- 字典属于**业务主数据**（业务语义强、含专属权限码与前端页面），不是像 `datasource / cache / web` 那样的横切技术能力。
- 因此当前**不**下沉为独立 Starter，而是采用「读写分层」：
  - **管理侧**（列表、增删改、启停、权限校验、前端页面）保留在业务工程 `vt-boot-example-admin` / `vt-boot-ui`。
  - **读取侧**抽象为 `DictReader` 接缝，供业务各模块统一消费，并在此处接入缓存；该接缝具备随时下沉为框架模块的条件。

术语：

| 术语 | 含义 |
|------|------|
| 字典类型 | 一类字典的定义（如「用户状态」），以全局唯一编码 `dictType` 标识，如 `sys_user_status` |
| 字典数据 | 某类型下的具体字典项（如「正常/1」「禁用/0」），以 `dictValue` 区分 |
| 管理侧 | 对字典类型与字典数据的 CRUD 与启停，直连数据库，强调实时与可写 |
| 读取侧 | 业务运行期按 `dictType` 读取字典项用于展示/判断，读多写少，走缓存 |

## 二、读写分层架构

### 1. 组件与职责

```text
                      管理侧 (直连数据库, 写入)                   读取侧 (缓存, 读多写少)
 ┌──────────────────────────────────────────────┐   ┌────────────────────────────────────────┐
 │ SysDictTypeApi / SysDictDataApi              │   │ 业务模块 / 前端 useDict                 │
 │   └─ SysDictTypeService / SysDictDataService │   │   └─ GET /api/system/dict/data/type/{t} │
 │        ├─ SysDictTypeMapper / SysDictDataMapper│   │        └─ SysDictDataService#listByType   │
 │        └─ (无缓存)                            │   │             └─ DictReader                │
 │                                              │   │                  └─ CachedDictReader       │
 │  写入后调用 ──────────────── evict(dictType) ─┼──▶│                      ├─ CacheUtils(Caffeine/Redis)
 └──────────────────────────────────────────────┘   │                      └─ SysDictDataMapper    │
                                                     └────────────────────────────────────────┘
```

| 组件 | 包/类 | 职责 |
|------|-------|------|
| 读取契约 | `reader.DictReader` | 只依赖 `DictItem`，不引任何框架实现类型；业务读取字典的唯一入口 |
| 读取模型 | `reader.DictItem` | `dictType / label / value / sort / tagType / cssClass / isDefault`，`Serializable` |
| 缓存实现 | `reader.impl.CachedDictReader` | 命中缓存返回；未命中回源 `SysDictDataMapper` 并回填；失效实现 |
| 数据服务 | `service.SysDictDataService` | 管理侧分页直查；`listByType` 委派 `DictReader`；写操作后失效 |
| 类型服务 | `service.SysDictTypeService` | 管理侧 CRUD；编码级联、删除、启停后失效 |

### 2. 关键调用链

- **读取（带缓存）**：
  `GET /api/system/dict/data/type/{dictType}` → `SysDictDataService#listByType` → `DictReader#listByType` → `CachedDictReader`（缓存命中直接返回；未命中查库并回填）→ `SysDictDataConverter#toVO(DictItem)` 映射为 `DictDataVO` 返回。
- **管理（直连）**：
  `POST /api/system/dict/{type|data}/page` → `Sys*Service#page` → `Mapper#selectPage`（不经缓存，保证管理页实时）。
- **写后失效**：所有写操作在事务方法内、返回前调用 `dictReader.evict(dictType)`（详见第四节）。

### 3. 为什么这样分层

| 关注点 | 管理侧 | 读取侧 |
|--------|--------|--------|
| 数据实时性 | 要求实时（刚改就可见） | 可接受短暂缓存 |
| 访问频率 | 低频 | 高频 |
| 数据量 | 分页、可筛选 | 单类型全量、体量小 |
| 是否加缓存 | 否 | 是 |

管理写与读取分离后，写入无需“边写边更新缓存”的复杂逻辑，**只做精确失效**即可，实现简单且不易出错。

## 三、数据模型与接口

### 1. 表

| 表 | 说明 | 关键约束 |
|----|------|----------|
| `t_dict_type` | 字典类型 | `(tenant_id, dict_type)` 唯一 |
| `t_dict_data` | 字典数据 | `(tenant_id, dict_type, dict_value)` 唯一 |

字段、索引与 DDL 详见 [数据库设计](./database.md)。

### 2. REST 接口

字典类型（前缀 `/api/system/dict/type`）：

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | `/page` | `system:dict:list` | 分页查询（管理，直查库） |
| GET | `/options` | 登录即可 | 全部启用类型精简列表，供数据页选择类型 |
| GET | `/{id}` | `system:dict:list` | 详情 |
| POST | `/` | `system:dict:add` | 新增 |
| PUT | `/` | `system:dict:edit` | 修改（编码变更级联更新数据） |
| DELETE | `/{id}` | `system:dict:delete` | 逻辑删除（有数据时拒绝） |
| PUT | `/{id}/status` | `system:dict:status` | 启停 |

字典数据（前缀 `/api/system/dict/data`）：

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | `/page` | `system:dict:list` | 分页查询（管理，直查库） |
| GET | `/type/{dictType}` | 登录即可 | **读取入口**，返回启用字典项（走缓存） |
| GET | `/{id}` | `system:dict:list` | 详情 |
| POST | `/` | `system:dict:add` | 新增 |
| PUT | `/` | `system:dict:edit` | 修改 |
| DELETE | `/{id}` | `system:dict:delete` | 逻辑删除 |
| PUT | `/{id}/status` | `system:dict:status` | 启停 |

## 四、缓存与失效约定

### 1. 缓存键规范

- 键格式：`vt:dict:data:{dictType}`，遵循框架全局约定 `{应用}:{模块}:{业务}:{标识}`。
- 一个 `dictType` 对应一个缓存条目，值为该类型下**全部启用**字典项的有序列表（`ArrayList<DictItem>`）。
- 统一通过 `CacheUtils` 读写，**不直接依赖** Caffeine/Redis 具体实现；切换缓存介质无需改动字典代码。

### 2. 读取约定

- `listByType(dictType)`：
  1. 尝试读缓存；命中（非占位值 `-` 且为列表）直接返回副本；
  2. 未命中回源数据库：先校验字典类型，**类型不存在或已被禁用则整类不可读、返回空列表**；否则按 `dict_type = ? AND status = 1 ORDER BY dict_sort, id` 查询；
  3. 回填缓存后返回。
- **类型禁用即整类不可读**：字典类型 `status=0` 时，其下所有字典数据读取均返回空列表（连同 `getByValue`/`getLabel` 一并失效）。
- `getByValue(dictType, value)` / `getLabel(dictType, value)` 基于 `listByType` 结果在内存中匹配，复用同一份缓存。
- 读取时 `dictType` 为空返回空列表/`null`，不抛异常、不缓存。

### 3. 失效约定（写操作必须失效）

> 原则：**写后精确失效受影响的 `dictType`；不清空无关缓存，不使用 `flushAll`。**

| 触发场景 | 失效的 key |
|----------|-----------|
| 新增字典数据 | 该数据 `dictType` |
| 修改字典数据 | 原 `dictType`；若所属类型变更，追加新 `dictType` |
| 删除字典数据 | 该数据 `dictType` |
| 启停字典数据 | 该数据 `dictType` |
| 新增字典类型 | 该类型编码（清理可能残留的空结果缓存） |
| 修改字典类型编码 | 旧编码；若编码变更，追加新编码（数据已级联迁移） |
| 删除字典类型 | 该类型 `dictType` |
| 启停字典类型 | 该类型 `dictType`（禁用后整类不可读，需失效原缓存） |

实现位置：

- `SysDictDataService` 的 `create / update / changeStatus / delete` 末尾调用 `dictReader.evict(...)`。
- `SysDictTypeService` 的 `update / changeStatus / delete` 末尾调用 `dictReader.evict(...)`。

### 4. 一致性与事务说明

- 失效在事务方法内部、返回前执行。字典为低频写、读多写少场景，可接受「失效后、提交前被并发读取到旧值」的极小概率窗口。
- 若对一致性要求更严，可优化为「事务提交后失效」：注册 `TransactionSynchronization#afterCommit` 再执行 `evict`，把窗口缩到提交之后。
- 写入侧**不做**「直接更新缓存」，避免双写不一致与并发覆盖问题；由读时回源保证最终正确。

### 5. 降级与容错

- 缓存读/写/失效异常均被捕获并记录告警日志，**自动降级为直接查库**，不影响主业务与接口可用性。
- 缓存未启用（`vt.cache.enabled=false`）或未引入 `vt-boot-cache-starter` 时，读取逻辑同样降级为直查库。

### 6. 介质、容量与过期

- 默认 Caffeine 本地缓存；`caffeine-max-size` 需大于字典类型数量（默认 1000 足够）。
- 框架 Caffeine **不支持运行时按 key 设 TTL**，因此新鲜度由「写时精确失效 + 容量上限」保证。
- 需要强时效/分布式共享时，切换 Redis：`vt.cache.is-redis-enabled=true` 并配置 `spring.data.redis.*`，代码零改动。

## 五、使用方式

### 1. 后端读取（推荐注入 `DictReader`）

```java
@Service
@RequiredArgsConstructor
public class SomeBizService {

    private final DictReader dictReader;

    public String demo() {
        // 读取某类型全部启用项
        List<DictItem> items = dictReader.listByType("sys_user_status");
        // 按值取标签（未命中返回 null）
        String label = dictReader.getLabel("sys_user_status", "1");
        return label;
    }
}
```

> 管理侧 Service（`SysDictDataService`）内部读取也统一走 `DictReader`，保证业务与管理读取口径一致。

### 2. 前端读取（`useDict`）

```ts
import { useDict } from '@/composables/useDict'

const { data, labelOf, tagOf, load } = useDict()
await load('sys_user_status')
labelOf('1')   // => '正常'
```

前端每次 `load` 都会请求后端读取接口；后端命中缓存，前端无需再自建缓存。

### 3. 直接调接口

`GET /api/system/dict/data/type/{dictType}`，返回启用字典项列表（`Result<List<DictDataVO>>`）。

## 六、配置

`application.yaml`：

```yaml
vt:
  cache:
    enabled: true                # 总开关
    is-caffeine-enabled: true    # 本地缓存（默认开启，无外部依赖）
    caffeine-init-size: 100
    caffeine-max-size: 1000
    is-redis-enabled: false      # 切分布式缓存时置 true 并配置 spring.data.redis.*
```

依赖：`vt-boot-example-admin` 已引入 `vt-boot-cache-starter`。

## 七、下沉为独立 Starter 的触发条件与迁移步骤

### 1. 触发条件（满足任一即可推进）

- 出现**第二个**需要字典能力的后端工程/应用；
- 需要**多应用共享同一套字典与统一缓存治理**；
- 架构目标明确为「下游工程零成本获得字典能力」（对齐 `task-admin-starter` 的开箱即用定位）。

反之，若 `vt-boot-example-admin` 即最终产品、无复用诉求，则**不建议**下沉。

### 2. 下沉前必须解决的事项

| 事项 | 说明 |
|------|------|
| 权限码解耦 | 去掉 `system:dict:*` 这类业务专属权限，改为由应用拦截器/权限 SPI 统一授权（参考 `task-admin-starter` 不依赖 satoken） |
| 前端归属 | Java Starter 无法交付 Vue 页面；前端仍需各应用自建或作为独立模板，需明确接受“后端下沉、前端不同步”的现实 |
| 依赖收敛 | Starter 依赖仅保留 `base`（异常/基类）+ `datasource`（MyBatis-Plus）+ `web`（Result）+ `cache`（缓存） |
| 自动配置 | 采用 `@AutoConfiguration` + `@Bean`，提供 `vt.dict.enabled` 开关，遵循 `CODING-STANDARDS.md` 第八节 |
| 建表脚本 | 随包提供幂等 SQL，按需初始化 |
| 序列化 | `DictItem` 保持 `Serializable` 与独立 `serialVersionUID`，兼容缓存/未来消息传输 |

### 3. 迁移步骤（Checklist）

1. 新建模块 `vt-boot-dict-starter`，加入父 `pom.xml` 的 `<modules>` 与 `dependencyManagement`。
2. 迁移 `reader`（`DictReader` / `DictItem`）到 `com.ranyk.vt.boot.dict.reader`；无需改动业务调用方的语义。
3. 迁移 `CachedDictReader` 到 `com.ranyk.vt.boot.dict.reader.impl`，改为通过 `@AutoConfiguration` 注册 Bean。
4. 迁移实体/Mapper/Service/Controller 与 `Dict*PO/DTO/VO/Converter`，去掉业务权限码；改为可插拔授权。
5. 迁移 `db/dict-mysql.sql` 到 Starter 资源目录，提供初始化开关。
6. 提供 `vt.dict.enabled` 开关与 `DictProperties`（如需），注册到 `AutoConfiguration.imports`。
7. 业务工程改为依赖该 Starter，删除本地重复实现；前端页面与菜单留在 `vt-boot-ui`。
8. 补充文档与示例，并在 `modules.md` 一览表登记该模块。

## 八、注意事项

- **不要**在读取侧直接查 `SysDictDataMapper` 或自行加缓存，统一走 `DictReader`，否则会绕过失效逻辑造成脏读。
- **不要**对字典缓存调用 `flushAll`/`flushDb`；只按 `dictType` 精确失效。
- 修改字典类型编码会级联更新字典数据并同时失效新旧编码缓存；如非必要，避免频繁改编码。
- **类型禁用即整类不可读（已落实）**：`CachedDictReader` 读取前会校验字典类型状态，类型不存在或 `status=0` 时返回空列表；类型启停、删除、编码变更、新增均已触发对应缓存失效。
- 缓存值以「启用项有序列表」为单位；单类型数据量应保持较小，避免缓存条目过大。

## 九、变更记录

| 日期 | 变更 | 说明 |
|------|------|------|
| 2026-11-20 | 初始版本 | 记录读写分层、缓存失效约定与下沉触发条件；读取侧接入 Caffeine 缓存 |
| 2026-11-20 | 类型禁用策略 | 落实「类型禁用即整类不可读」：读取前校验类型状态，类型不存在/禁用返回空；补充类型新增时的缓存失效 |
