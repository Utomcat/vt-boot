package com.ranyk.vt.boot.satoken.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.ranyk.vt.boot.base.exception.UserException;
import com.ranyk.vt.boot.satoken.config.properties.SaTokenProperties;
import com.ranyk.vt.boot.satoken.interceptor.TenantWebInterceptor;
import com.ranyk.vt.boot.satoken.interceptor.TokenRequestInterceptor;
import com.ranyk.vt.boot.satoken.interceptor.TokenResponseInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CLASS_NAME: SaTokenInterceptorsConfig.java
 *
 * @author ranyk
 * @version V1.0
 * @description: sa-token 路由/请求拦截器自动配置类
 * @date: 2026-02-28
 */
@Slf4j
@AutoConfiguration
@AutoConfigureAfter(SaTokenConfigure.class)
@ConditionalOnProperty(name = "sa-token.enable", havingValue = "true")
public class SaTokenInterceptorsConfig implements WebMvcConfigurer {

    /**
     * SA-TOKEN 配置属性对象
     */
    private final SaTokenProperties saTokenProperties;
    /**
     * 是否启用多租户功能
     */
    @Value("${vt.boot.is-enable-tenant:false}")
    private Boolean isEnableTenant;

    /**
     * 构造方法 - 向 Spring IOC 容器中自动注入 SA-TOKEN 配置属性对象
     *
     * @param saTokenProperties SA-TOKEN 配置属性对象
     */
    @Autowired
    @SuppressWarnings("all")
    public SaTokenInterceptorsConfig(SaTokenProperties saTokenProperties) {
        log.debug("Auto wired SaTokenProperties. Current configuration attribute is : {}", saTokenProperties);
        this.saTokenProperties = saTokenProperties;
    }

    /**
     * 向 Spring MVC 中注册 Sa-Token 拦截器，配置路由鉴权规则
     *
     * @param registry 拦截器注册对象 {@link InterceptorRegistry}
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 注册 Sa-Token 请求拦截器 - 用于在请求处理前获取对应的请求 Token 中的用户信息
        log.debug("SA-TOKEN is enabled. register Sa-Token Request Interceptor. register login Successful Request Interceptor.");
        registry.addInterceptor(new TokenRequestInterceptor());
        log.debug("SA-TOKEN is enabled. register Sa-Token Interceptor. the current configuration attribute is {}", saTokenProperties);
        // 注册 Sa-Token 拦截器并配置鉴权逻辑
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 指定一条 match 规则
                    SaRouter
                            // 拦截的 path 列表，可以写多个 */
                            .match(saTokenProperties.getMatchPath())
                            // 排除掉的 path 列表，可以写多个
                            .notMatch(saTokenProperties.getExcludePathPatterns())
                            // 要执行的校验动作，可以写完整的 lambda 表达式
                            .check(r -> {
                                log.trace("========================= Sa-Token Interceptor. check login status. =========================");
                                try {
                                    StpUtil.checkLogin();
                                } catch (NotLoginException e) {
                                    log.trace("========================= Sa-Token Interceptor. check login status. Check fail. ==========================");
                                    throw new UserException("user.not.login", new Object[]{});
                                }
                                log.trace("========================= Sa-Token Interceptor. check login status. Check pass. ==========================");
                            });
                }))
                // 拦截所有请求路径
                .addPathPatterns(saTokenProperties.getPathPatterns())
                // 忽略所有无需鉴权的接口 登录接口、静态资源接口、图标接口
                .excludePathPatterns(saTokenProperties.getExcludePathPatterns());
        // 注册登录成功响应拦截器 - 用于在请求和响应头中返回 token 值
        log.debug("SA-TOKEN is enabled. register Sa-Token Response Interceptor. register login Successful Response Interceptor.");
        registry.addInterceptor(new TokenResponseInterceptor(saTokenProperties));
        if (isEnableTenant) {
            log.debug("Tenant is enabled. register Global Tenant Interceptor.");
            // 注册 多租户 拦截器 - 用于注册 租户上下文 的 租户ID 同时 用于在请求和响应头中返回租户ID
            registry.addInterceptor(new TenantWebInterceptor(isEnableTenant, saTokenProperties.getExcludePathPatterns()));
        }
    }

}
