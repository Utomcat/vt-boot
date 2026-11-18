package com.ranyk.vt.boot.base.config;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * CLASS_NAME: VtBootBaseAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 基础模块自动配置类
 * @date: 2026-11-18
 */
@AutoConfiguration
@ConditionalOnClass(SpringUtil.class)
public class VtBootBaseAutoConfiguration {

    /**
     * 向 Spring IOC 容器中注册 Hutool 的 Spring 工具类对象
     * <p>
     * {@link SpringUtil} 在 Hutool 中是以 {@code @Component} 形式提供的, 只有在消费方扫描到
     * {@code cn.hutool.extra.spring} 包时才会被注册。框架内的 {@code MessageUtils}、{@code MqUtils}、
     * {@code CacheUtils} 均依赖它获取 Bean, 若未注册会在首次调用时抛出空指针异常,
     * 因此这里显式注册, 保证"引入依赖即可用"。
     * </p>
     * <p>
     * 该方法声明为 {@code static}: {@link SpringUtil} 实现了 {@code BeanFactoryPostProcessor},
     * 静态工厂方法可以让它在 BeanFactoryPostProcessor 阶段被创建, 既不会触发
     * "is not eligible for getting processed by all BeanPostProcessors" 告警, 也能保证在容器启动早期就可用。
     * </p>
     *
     * @return Hutool Spring 工具类对象 {@link SpringUtil}
     */
    @Bean
    @ConditionalOnMissingBean
    public static SpringUtil springUtil() {
        return new SpringUtil();
    }
}
