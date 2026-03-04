package com.ranyk.vt.boot.satoken.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.ranyk.vt.boot.base.exception.UserException;
import com.ranyk.vt.boot.satoken.config.properties.SaTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
     * 构造方法 - 向 Spring IOC 容器中自动注入 SA-TOKEN 配置属性对象
     *
     * @param saTokenProperties    SA-TOKEN 配置属性对象
     */
    @Autowired
    public SaTokenInterceptorsConfig(SaTokenProperties saTokenProperties) {
        log.info("Auto wired SaTokenProperties. Current configuration attribute is : {}", saTokenProperties);
        this.saTokenProperties = saTokenProperties;
    }

    /**
     * 向 Spring MVC 中注册 Sa-Token 拦截器，配置路由鉴权规则
     *
     * @param registry 拦截器注册对象 {@link InterceptorRegistry}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("SA-TOKEN is enabled. register Sa-Token Interceptor. the current configuration attribute is {}", saTokenProperties);
        // 注册拦截器并配置鉴权逻辑
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 所有未放行的接口，必须先登录
                    if (SaRouter.isMatchCurrURI(saTokenProperties.getMatchPath())) {
                        try {
                            StpUtil.checkLogin();
                        } catch (NotLoginException e) {
                            throw new UserException("user.not.login", new Object[]{});
                        }
                    }
                }))
                // 拦截所有请求路径
                .addPathPatterns(saTokenProperties.getPathPatterns())
                // 忽略所有无需鉴权的接口 登录接口、静态资源接口、图标接口
                .excludePathPatterns(saTokenProperties.getExcludePathPatterns());
    }

}
