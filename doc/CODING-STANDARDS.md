# 编码规范（VT-Boot）

> 本文是 VT-Boot 项目唯一有效的编码规范。`.cursor/rules/vt-boot-coding-standards.mdc` 为其常驻摘要，两者冲突时以本文为准。
> 技术栈：Spring Boot 4.1.2 + JDK 21（虚拟线程）+ Maven 聚合多模块；代码根包 `com.ranyk.vt.boot`。

## 一、格式（不可放宽）

- 编码 UTF-8，换行 **CRLF**，文件末尾保留换行，行尾不留空格。
- 缩进 **4 空格**，禁止 Tab；`yml/yaml/json/xml` 缩进 2；`*.java` 最大行宽 200。
- 花括号不得省略；`}` 与 `else/catch` 写成 `} else {`，禁止 `}` 与 `else` 分行。
- 导入：静态导入在前，其余按字母序；禁止星号导入（例外 `java.io` / `java.util` / `java.util.stream` / `lombok`）；未使用 import（含 Javadoc 引用）一律删除。
- 禁止 `System.out` / `System.err` / `printStackTrace` / 空 catch（变量名 `ignored` 除外）。
- 单个方法 ≤ 60 行，单个类 ≤ 500 行；单文件仅一个顶级类；公开 API 参数与返回值用显式类型。

## 二、类头注释（强制，全仓统一）

```java
/**
 * CLASS_NAME: Xxx.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 中文一句话描述, 句末不加句号
 * @date: yyyy-MM-dd
 */
```

- `CLASS_NAME` 必须与文件名完全一致（含 `.java`）。
- `@date` 为创建日期，后续修改不改写；`@version` 恒为 `V1.0`。
- 字段、枚举常量、方法一律用 `/** */` 中文注释，禁止用 `//` 解释含义。
- 方法注释写全 `@param`（每个参数）/ `@return`（非 void）/ `@throws`（显式抛出）；格式 `@param 参数名 中文说明`。
- 禁止空壳注释；禁止保留注释掉的代码块。

## 三、命名

| 类型 | 规则 |
|------|------|
| 自动配置 | `{能力}AutoConfiguration`；纯手工配置类可 `{能力}Configuration` |
| 配置属性 | `{能力}ConfigurationProperties` 或 `{能力}Properties` |
| 枚举 | `XxxEnum`，字段固定 `desc` 在前、`value` 在后，类上 `@Getter` |
| 异常 | `{模块}Exception`，继承 `BaseException` |
| 工具类 | `XxxUtils`；包名统一单数 `util` |
| 切面 | `{注解名}Aspect`（aop）/ `{注解名}AspectHandle`（log） |
| 策略 | `{领域}Strategy` / 实现 `{行为}{领域}StrategyImpl` |
| 服务 | 接口 `I{领域}Service`；默认实现 `Default{领域}Service` |
| DTO/PO/VO/Entity | `{业务}DTO/PO/VO`，基类 `Base{类型}` |
| 响应 | `Result`（单结果）、`MultiResult`（多结果/分页） |

- 布尔字段**不带 `is` 前缀**（用 `enabled` 而非 `isEnabled`）；历史配置键用 `@Name("is-xxx")` 保留绑定。
- 方法名动词开头（`get/set/build/create/resolve/validate/format/require/parse`）；`resolveXxx` = 归一化取值，`requireXxx` = 取不到即抛异常。

## 四、Lombok 固定搭配

| 场景 | 组合 |
|------|------|
| DTO/PO/VO/Entity | `@Data + @SuperBuilder + @NoArgsConstructor + @AllArgsConstructor` |
| 继承场景 | 追加 `@ToString(callSuper = true)`、`@EqualsAndHashCode(callSuper = true)` |
| 默认值字段 | 字段上 `@Builder.Default` + 直接赋初值 |
| 异常类 | `@Getter + @EqualsAndHashCode(callSuper = true)`，**禁止 `@Data`** |
| 纯静态工具类 | `@NoArgsConstructor(access = AccessLevel.PRIVATE)`，不加 `@Data` |
| 依赖注入 | `@RequiredArgsConstructor + private final`（禁止 `@Autowired` 字段/setter 注入） |
| 日志 | `@Slf4j` |

- `@Autowired` 仅允许出现在 `@Bean` 方法参数上（可省略）。
- 禁止同存 `@Data` 与手写 getter/setter。

## 五、序列化与模型

- 可传输/可缓存模型 `implements Serializable`，声明 `@Serial private static final long serialVersionUID = <该类的随机长字面量>L;`。
    - `serialVersionUID` **每个类各自不同**，取该类的随机长整型字面量（如 IDE 生成值），**禁止全仓复制同一个值**，也禁止用 `1L`。
    - 变更 `serialVersionUID` 须在提交说明写明，并评估缓存/消息反序列化兼容性。
- 时间字段双注解：`@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")` + `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")`。
- 公共字段（`id / createTime / updateTime / createBy / updateBy / remark / status / tenantId`）只定义在 `BaseDTO` / `BasePO` / `BaseVO` / `BaseEntity`。
- 字段注释写明默认值与取值范围。

## 六、异常

- 业务只抛 `BaseException` 子类，禁止用 `RuntimeException` / `IllegalStateException` 表达业务语义。
- 用户文案走 `code + args` 国际化：

```java
throw new ServiceException(XxxErrorCodeEnum.XXX.getValue(), new Object[]{arg});
```

- 包装底层异常必须传 `cause`；错误码必须唯一。
- 新增错误码三件事同步：登记 `*ErrorCodeEnum`、补 `messages_zh_CN.properties`、补 `messages_en_US.properties`。
- 异常构造器仅 5 种形态：`(String msg)`、`(String msg, Throwable e)`、`(String module, String msg)`、`(String code, Object[] args)`、`(String module, String code, Object[] args)`。

## 七、响应体

- 对外唯一模型：`Result<T>` 与 `MultiResult<T>`；`BaseResponse` 族已 `@Deprecated`。
- 成功走 `Result.success(data)` / `MultiResult.successMulti(...)`；失败走 `Result.error(code, msg)`。
- `code` 只取 `ResponseCodeEnum` 名或业务错误码，禁止 HTTP 数字字符串、禁止 Hutool `HttpStatus`。
- 分页字段固定：`page` / `size` 为 `Integer`，`total` / `totalPage` 为 `Long`，`list` 为 `List<T>`；`long → int` 用 `Math.toIntExact`。

## 八、自动配置

```java
@AutoConfiguration
@EnableConfigurationProperties(XxxProperties.class)
@ConditionalOnBooleanProperty(prefix = "vt.boot.xxx", name = "enabled", havingValue = true)
public class XxxAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Xxx xxx(final XxxProperties properties) {
        log.debug("Xxx is enabled. create Xxx Object");
        return new Xxx(properties);
    }
}
```

- 登记到 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`，一行一个全限定名，不写注释。
- 每个 `@Bean` 必须带 `@ConditionalOnMissingBean`。
- Starter 内部组件一律 `@AutoConfiguration` + `@Bean` 注册，**禁止 `@Component` / `@Service` / `@Repository`**（仅使用方业务工程可用）。
- 布尔开关用 `@ConditionalOnBooleanProperty`，禁止 `@ConditionalOnProperty(havingValue="true")`。
- 配置属性类：`@ConfigurationProperties` + `@Data` + `@EnableConfigurationProperties`，**禁止再加 `@Component`**；字段用包装类型且给初始值。

## 九、日志

- `@Slf4j` + `{}` 占位符，禁止字符串拼接；参数 > 3 个先抽局部变量。
- 异常对象作最后一个参数：`log.error("xxx key: {}, 异常 => ", key, e);`
- 级别：`trace` 方法进出横幅/细节，`debug` 流程节点，`warn` 可恢复/业务拒绝，`error` 需人工介入。
- 配置类初始化日志用英文短句（`Xxx is enabled. create Xxx Object`），业务日志用中文。
- 横幅仅允许 `=` 与空格，禁止 emoji。
- 禁止输出令牌、密码、完整手机号/证件号（打码：`"{}****{}", head, tail`）。

## 十、常量与枚举

- 枚举放 `constant` 包，类上 `@Getter`；字段 `desc` 在前、`value` 在后。
- 枚举常量初始化表达式禁止运行期值（`LocalDateTime.now()`、随机数、`SpringUtil.getBean`），需要动态默认值用 `Supplier`。
- 状态码、租户 ID、操作员 ID、表名、MQ 键等魔法值全部收敛到枚举，业务代码不出现字面量。

## 十一、工具类

- 私有构造 + 全静态方法：`@NoArgsConstructor(access = AccessLevel.PRIVATE)`，不加 `@Data` / `@Builder`。
- 需要 Spring Bean 时用 DCL 懒加载私有 getter，**禁止**在 `static final` 字段初始化处 `SpringUtil.getBean(...)`。
- 参数校验失败抛本模块异常，禁止静默返回 `null`。
- 文件/流操作 `try-with-resources`；自己创建的流自己关闭。

## 十二、并发与上下文

- 异步统一虚拟线程执行器 `VtExecutors.newVirtualThreadPerTaskExecutor()`；禁止 `new Thread(...)` / `new ThreadPoolExecutor(...)`（除非有容量边界并在类注释写明理由）。
- 提交任务必须在任务体内兜底异常。
- `ThreadLocal` 上下文（`TenantContext` / `UserContext`）：请求进入 `set`，`afterCompletion` 中 `clear`；跨线程用 `ContextSnapshot.capture()/restore()` 显式传递。
- 并发写共享状态用 `ConcurrentHashMap` / `AtomicXxx` / `LongAdder`。

## 十三、持久层（MyBatis-Plus）

- ORM 实体继承 `BaseEntity`，主键 `@TableId(type = IdType.ASSIGN_ID)`（示例数据表使用自增 `AUTO`，以实际表结构为准）。
- 逻辑删除显式：`@TableLogic(value = "1", delval = "-1")`（示例使用 `0`/`1`，以 `BaseEntity` 现有实现为准）。
- 多租户由 `TenantLineInnerInterceptor` 统一处理，业务 SQL 不手写 `tenant_id`；租户 ID 统一按字符串（`StringValue`）。
- 分页统一 `PageUtils.buildPage(dto)` / `PageUtils.buildPageResponse(iPage, voList)`，业务层禁止 `new Page<>`。

## 十四、缓存 / 文件 / MQ / Web 要点

- 缓存 key 前缀 `{应用}:{模块}:{业务}:{标识}`；禁止 `flushAll` / `flushDb`（按前缀 scan + del）；Caffeine 收到 `timeout` 直接抛 `CacheException`；批量读写走批量 API。
- 文件：复制流用 `read(buf)` 循环写；ZIP 条目分隔符统一 `"/"`；目录创建用 `mkdirs`；后缀判断用 `endsWith("." + suffix)`。
- MQ：Disruptor `ProducerType.MULTI` + `bufferSize` 为 2 的幂；消费「有限重试 + 死信」；发送前校验消息头；MQ 异常不影响主业务。
- Web：路径 `/api/{模块}/{动作}`；鉴权由 `SaTokenInterceptorsConfig` 统一注册；排除路径用 `AntPathMatcher`；拦截器拒绝走统一异常出口；CORS 白名单化（禁止 `"*"` + `allowCredentials(true)`）；密码只走 `PasswordUtils`。

## 十五、提交

- 提交信息 `type(scope): 中文描述`，type ∈ `feat/fix/refactor/docs/style/test/build/chore`。
- 一次提交只做一件事；合并前 `mvn -q clean compile` + `mvn checkstyle:check -Pcheckstyle` 通过。
