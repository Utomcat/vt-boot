package com.ranyk.vt.boot.internationalization.env;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * CLASS_NAME: VtI18nEnvironmentPostProcessor.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 国际化默认配置注入器 - 在环境准备阶段注入 i18n 相关默认值, 保证引入依赖即可用
 * @date: 2026-11-18
 */
public class VtI18nEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    /**
     * 默认配置属性源名称
     */
    private static final String PROPERTY_SOURCE_NAME = "vt-boot-i18n-defaults";

    /**
     * 消息资源文件基础名, 对应 starter 内 i18n/messages*.properties
     */
    private static final String DEFAULT_MESSAGES_BASENAME = "i18n/messages";

    /**
     * 消息资源文件编码
     */
    private static final String DEFAULT_MESSAGES_ENCODING = "UTF-8";

    /**
     * 默认语言环境
     */
    private static final String DEFAULT_LOCALE = "zh_CN";

    /**
     * 默认语言解析策略 - accept-header 表示按请求头 Accept-Language 解析
     */
    private static final String DEFAULT_LOCALE_RESOLVER = "accept-header";

    /**
     * 向 Spring 环境中注入国际化默认配置
     * <p>
     * 该属性源使用 {@code addLast} 追加到属性源列表末尾, 优先级最低,
     * 使用方只要在自身的 {@code application.yaml} 中显式配置同名属性即可覆盖,
     * 因此不会出现"starter 内 application.yaml 污染使用方配置"的问题。
     * </p>
     * <p>
     * 未注入 {@code spring.messages.basename} 时, Spring Boot 默认只加载 {@code classpath:messages},
     * 而本模块的资源位于 {@code classpath:i18n/messages}, 会导致所有国际化文案取不到值。
     * </p>
     *
     * @param environment Spring 可配置环境对象 {@link ConfigurableEnvironment}
     * @param application Spring 应用对象 {@link SpringApplication}
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (environment.getPropertySources().contains(PROPERTY_SOURCE_NAME)) {
            return;
        }
        Map<String, Object> defaults = Map.of(
                "spring.messages.basename", DEFAULT_MESSAGES_BASENAME,
                "spring.messages.encoding", DEFAULT_MESSAGES_ENCODING,
                "spring.web.locale", DEFAULT_LOCALE,
                "spring.web.locale-resolver", DEFAULT_LOCALE_RESOLVER
        );
        environment.getPropertySources().addLast(new MapPropertySource(PROPERTY_SOURCE_NAME, defaults));
    }

    /**
     * 获取执行顺序 - 置于最低优先级, 保证在配置数据加载完成后追加属性源
     *
     * @return 执行顺序值
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
