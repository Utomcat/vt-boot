package com.ranyk.vt.boot.datasource.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.ranyk.vt.boot.datasource.config.properties.DatasourceConfigurationProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.ranyk.vt.boot.datasource.handler.CustomTenantHandler;
import com.ranyk.vt.boot.datasource.handler.DataObjectHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

/**
 * CLASS_NAME: DataSourceConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据源配置类
 * @date: 2026-02-13
 */
@AutoConfiguration
@ConditionalOnClass(MybatisPlusInterceptor.class)
@EnableConfigurationProperties(DatasourceConfigurationProperties.class)
@RequiredArgsConstructor
public class DataSourceConfiguration {

    /**
     * 数据源配置属性对象
     */
    private final DatasourceConfigurationProperties datasourceConfigurationProperties;

    /**
     * 自定义多租户处理器对象 提供者 - 用于按需获取多租户处理器, 避免在 Bean 创建阶段产生循环依赖
     */
    private final ObjectProvider<CustomTenantHandler> customTenantHandlerProvider;

    /**
     * 向 Spring Bean 容器中注入 自定义元对象处理器 对象 - 该操作是对 MyBatis Plus 的配置
     * <p>
     * 该处理器负责在插入/更新时填充租户ID、创建人、更新人、创建时间、更新时间、逻辑删除标记等公共字段,
     * 应用可通过自行注册 {@link MetaObjectHandler} Bean 来覆盖默认实现
     * </p>
     *
     * @return 返回自定义元对象处理器对象 {@link MetaObjectHandler}
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler() {
        return new DataObjectHandler();
    }

    /**
     * 向 Spring Bean 容器中注入 自定义多租户处理器对象 - 该操作是对 MyBatis Plus 的配置
     *
     * @return 返回自定义多租户处理器对象 {@link CustomTenantHandler}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBooleanProperty(prefix = "vt.boot", name = "is-enable-tenant", matchIfMissing = false)
    public CustomTenantHandler customTenantHandler() {
        return new CustomTenantHandler(datasourceConfigurationProperties);
    }

    /**
     * 向 Spring Bean 容器中注入 Mybatis Plus 拦截器对象, 当前注册了(执行顺序如下): - 该操作是对 MyBatis Plus 的配置
     * <p>
     * <ol>
     *     <li>多租户: TenantLineInnerInterceptor</li>
     *     <li>防全表更新与删除插件</li>
     *     <li>分页插件</li>
     *     <li>乐观锁插件</li>
     * </ol>
     * </p>
     * 对于 MyBatis Plus 的内部拦截器,推荐的配置顺序如下:
     * <p>
     * <ol>
     *     <li>动态表名: DynamicTableNameInnerInterceptor</li>
     *     <li>多租户: TenantLineInnerInterceptor</li>
     *     <li>数据权限（自定义拦截器类）: MyDataPermissionInterceptor</li>
     *     <li>防止全表更新/删除: BlockAttackInnerInterceptor</li>
     *     <li>分页: PaginationInnerInterceptor</li>
     *     <li>乐观锁: OptimisticLockerInnerInterceptor</li>
     *     <li>SQL 性能规范: IllegalSQLInnerInterceptor</li>
     * </ol>
     * </p>
     *
     * @return 返回 Mybatis Plus 拦截器对象 {@link MybatisPlusInterceptor}
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 多租户插件 - 插件 TenantLineInnerInterceptor 类在插件依赖 mybatis-plus-jsqlparser 下
        if (datasourceConfigurationProperties.getIsEnableTenant()) {
            CustomTenantHandler customTenantHandler = customTenantHandlerProvider.getIfAvailable();
            if (Objects.nonNull(customTenantHandler)) {
                interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(customTenantHandler));
            }
        }
        //防全表更新与删除插件 - 插件 BlockAttackInnerInterceptor 类在插件依赖 mybatis-plus-jsqlparser 下
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        //分页插件 - 插件 PaginationInnerInterceptor 类在插件依赖 mybatis-plus-jsqlparser 下
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(datasourceConfigurationProperties.getDbType()));
        //乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // SQL 性能规范插件/ 非法 SQL 拦截插件 - 插件 IllegalSQLInnerInterceptor 在 3.5.10 版本开始移除
        return interceptor;
    }

}