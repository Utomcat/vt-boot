package com.ranyk.vt.boot.aop.config;

import com.ranyk.vt.boot.aop.handle.AutoFillFieldAspect;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * CLASS_NAME: VtAopAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: AOP 模块自动配置类
 * @date: 2026-11-18
 */
@AutoConfiguration
@ConditionalOnClass(Aspect.class)
public class VtAopAutoConfiguration {

    /**
     * 向 Spring IOC 容器中注册自动填充字段切面对象
     * <p>
     * starter 内部禁止使用 {@code @Component}, 因为消费方通常只扫描自己的业务包,
     * 组件扫描无法覆盖 starter 的包路径, 会导致切面"引入但不生效"。
     * </p>
     *
     * @return 自动填充字段切面对象 {@link AutoFillFieldAspect}
     */
    @Bean
    @ConditionalOnMissingBean
    public AutoFillFieldAspect autoFillFieldAspect() {
        return new AutoFillFieldAspect();
    }
}
