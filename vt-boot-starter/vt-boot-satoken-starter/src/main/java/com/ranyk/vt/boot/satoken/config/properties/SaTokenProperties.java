package com.ranyk.vt.boot.satoken.config.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * CLASS_NAME: SaTokenProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: sa-token 配置属性类
 * @date: 2026-02-28
 */
@Data
@ToString
@ConfigurationProperties(prefix = "sa-token")
public class SaTokenProperties {

    /**
     * 是否启用 sa-token 功能，默认不启用
     */
    private Boolean enable = false;
    /**
     * token 名称（同时也是 cookie 名称）
     */
    private String tokenName = "satoken";
    /**
     * token 有效期（单位：秒） 默认30天，-1 代表永久有效
     */
    private Integer timeout = 2592000;
    /**
     * token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
     */
    private Long activityTimeout = -1L;
    /**
     * 是否允许同一账号多地同时登录 （为 true 时允许一起登录, 为 false 时新登录挤掉旧登录）
     */
    private Boolean isConcurrent = true;
    /**
     * 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token, 为 false 时每次登录新建一个 token）
     */
    private Boolean isShare = false;
    /**
     * token 风格（默认可取值：uuid、simple-uuid、random-32、random-64、random-128、tik）, 未配置则使用默认的 uuid
     */
    private String tokenStyle = "uuid";
    /**
     * 是否输出操作日志
     */
    private Boolean isLog = true;
    /**
     * 是否在初始化配置时在控制台打印版本字符画
     */
    private Boolean isPrint = true;
    /**
     * 对请求路径进行拦截校验的匹配规则，默认为 /**
     */
    private String matchPath = "/**";
    /**
     * 哪些路径需要进行拦截校验, 默认为 /**
     */
    private List<String> pathPatterns = List.of("/**");
    /**
     * 哪些路径不需要进行拦截校验, 默认为 /login (登录接口请求), /static/** (静态资源请求), /favicon.ico (图标请求), /api/captcha (验证码接口请求)
     */
    private List<String> excludePathPatterns = List.of("/api/login", "/static/**", "/favicon.ico", "/api/captcha");
    /**
     * 登录接口 - 用于在登录成功后, 拦截请求, 注册认证信息到请求头中
     */
    private String loginPathPatterns = "/api/login";

}
