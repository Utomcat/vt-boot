package com.ranyk.vt.boot.task.config;

import com.ranyk.vt.boot.task.properties.VtTaskStoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.quartz.autoconfigure.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * CLASS_NAME: VtTaskStoreAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定时任务存储自动配置类 - 让 Quartz 默认使用 JDBC 存储, 避免服务重启后丢失任务
 * <p>
 * 框架不通过 {@code EnvironmentPostProcessor} 改写配置, 而是以自动配置类的方式完成存储装配:
 * </p>
 * <ul>
 *     <li>存在唯一数据源时, 把数据源注入 {@link SchedulerFactoryBean}, Quartz 会自动使用
 *     {@code LocalDataSourceJobStore}(任务与触发器落库到 QRTZ_* 表);</li>
 *     <li>启动时执行框架内置的幂等建表脚本, 表已存在即跳过, 不会清空已持久化的任务数据;</li>
 *     <li>使用方显式配置 {@code spring.quartz.job-store-type=memory} 或 {@code vt.task.store.type=memory}
 *     时本配置整体不生效, 退回 Quartz 内存存储。</li>
 * </ul>
 * <p>
 * 线程池、错过触发策略、集群等其余能力仍统一由 {@code spring.quartz.*} 配置, 框架不重复封装。
 * </p>
 * @date: 2026-11-20
 */
@Slf4j
@AutoConfiguration(after = VtTaskAutoConfiguration.class)
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class, SchedulerFactoryBeanCustomizer.class,
        ResourceDatabasePopulator.class})
@ConditionalOnSingleCandidate(DataSource.class)
@ConditionalOnBooleanProperty(prefix = "vt.task", name = "enabled", matchIfMissing = true)
@Conditional(VtTaskStoreCondition.class)
@EnableConfigurationProperties(VtTaskStoreProperties.class)
public class VtTaskStoreAutoConfiguration {

    /**
     * 注册 Quartz 工厂定制器 - 把数据源交给 SchedulerFactoryBean, 使其使用 LocalDataSourceJobStore
     * <p>
     * 方法声明 {@link VtQuartzSchemaInitializer} 的 {@link ObjectProvider} 并立即触发, 是为了让建表脚本
     * 在 SchedulerFactoryBean 初始化 JobStore 之前执行完毕(Quartz 启动时会读取 QRTZ_LOCKS 等表)。
     * </p>
     *
     * @param dataSource                 数据源对象 {@link DataSource}
     * @param schemaInitializerProvider  存储表初始化器提供者
     * @param transactionManager         事务管理器提供者, 存在唯一实现时交给 Quartz 使用
     * @return Quartz 工厂定制器 {@link SchedulerFactoryBeanCustomizer}
     */
    @Bean
    public SchedulerFactoryBeanCustomizer vtTaskJdbcStoreCustomizer(
            DataSource dataSource,
            ObjectProvider<VtQuartzSchemaInitializer> schemaInitializerProvider,
            ObjectProvider<PlatformTransactionManager> transactionManager) {
        log.debug("VtTask store is jdbc. register SchedulerFactoryBeanCustomizer.");
        schemaInitializerProvider.ifAvailable(initializer ->
                log.debug("VtTask store schema initializer is ready. scheduler will use jdbc job store."));
        return factory -> {
            factory.setDataSource(dataSource);
            transactionManager.ifUnique(factory::setTransactionManager);
        };
    }

    /**
     * 注册 Quartz 存储表初始化器 - 仅在 vt.task.store.initialize-schema=always 时执行
     *
     * @param dataSource      数据源对象 {@link DataSource}
     * @param storeProperties 存储配置属性对象 {@link VtTaskStoreProperties}
     * @return Quartz 存储表初始化器 {@link VtQuartzSchemaInitializer}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "vt.task.store", name = "initialize-schema",
            havingValue = "always", matchIfMissing = true)
    public VtQuartzSchemaInitializer vtQuartzSchemaInitializer(DataSource dataSource,
                                                               VtTaskStoreProperties storeProperties) {
        log.debug("VtTask store schema init is enabled.");
        return new VtQuartzSchemaInitializer(dataSource, storeProperties);
    }
}
