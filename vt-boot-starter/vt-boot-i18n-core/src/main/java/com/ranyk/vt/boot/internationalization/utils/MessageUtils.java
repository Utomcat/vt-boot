package com.ranyk.vt.boot.internationalization.utils;

import cn.hutool.extra.spring.SpringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * CLASS_NAME: MessageUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 国际化信息转换工具类
 * @date: 2026-02-08
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {
    /**
     * 获取国际化信息 Bean 对象
     * <p>
     * 这里采用惰性 + 可失败重试的方式获取, 而不是在静态常量中直接初始化:
     * 静态常量会在类加载阶段就向容器索要 Bean, 一旦触发时机早于容器就绪(例如启动早期的异常构造),
     * 就会抛出 ExceptionInInitializerError 并导致该类永久不可用。
     * </p>
     */
    private static volatile MessageSource messageSource;

    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 异常代码
     * @param args 异常信息参数
     * @return 获取国际化翻译值; 若容器中不存在 MessageSource 或该消息键不存在, 则原样返回 code
     */
    public static String message(String code, Object... args) {
        MessageSource source = getMessageSource();
        if (source == null) {
            return code;
        }
        try {
            return source.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return code;
        }
    }

    /**
     * 获取国际化信息 Bean 对象
     * <p>
     * 获取失败时返回 {@code null} 而不是抛出异常, 且不缓存失败结果, 以便后续调用可重试。
     * </p>
     *
     * @return 国际化信息 Bean 对象 {@link MessageSource}, 不可用时返回 null
     */
    private static MessageSource getMessageSource() {
        if (messageSource == null) {
            synchronized (MessageUtils.class) {
                if (messageSource == null) {
                    try {
                        messageSource = SpringUtil.getBean(MessageSource.class);
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        return messageSource;
    }

}
