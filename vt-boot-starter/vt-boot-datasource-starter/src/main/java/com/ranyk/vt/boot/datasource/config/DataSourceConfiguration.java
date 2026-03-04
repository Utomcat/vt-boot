package com.ranyk.vt.boot.datasource.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.ranyk.vt.boot.datasource.config.properties.DatasourceConfigurationProperties;
import com.ranyk.vt.boot.datasource.handler.CustomTenantHandler;
import com.ranyk.vt.boot.datasource.handler.DataObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * CLASS_NAME: DataSourceConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据源配置类
 * @date: 2026-02-13
 */
@Slf4j
@AutoConfiguration
@AutoConfigureBefore({DataSourceAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
@EnableConfigurationProperties(value = com.ranyk.vt.boot.datasource.config.properties.DatasourceConfigurationProperties.class)
public class DataSourceConfiguration {

    /**
     * 数据源配置属性对象
     */
    private final com.ranyk.vt.boot.datasource.config.properties.DatasourceConfigurationProperties datasourceConfigurationProperties;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入数据源配置属性对象
     *
     * @param datasourceConfigurationProperties 数据源配置属性对象 {@link com.ranyk.vt.boot.datasource.config.properties.DatasourceConfigurationProperties}
     */
    @Autowired
    public DataSourceConfiguration(DatasourceConfigurationProperties datasourceConfigurationProperties) {
        this.datasourceConfigurationProperties = datasourceConfigurationProperties;
    }

    /**
     * 向 Spring Bean 容器中注入 自定义元对象处理器 对象 - 该操作是对 MyBatis Plus 的配置
     *
     * @return 返回自定义元对象处理器对象 {@link DataObjectHandler}
     */
    @Bean
    public DataObjectHandler myMetaobjectHandler() {
        return new DataObjectHandler();
    }

    /**
     * 向 Spring Bean 容器中注入 Mybatis Plus 拦截器对象, 当下注册了(执行顺序如下): - 该操作是对 MyBatis Plus 的配置
     * <p>
     *     <ol>
     *         <li>多租户: TenantLineInnerInterceptor</li>
     *         <li>防全表更新与删除插件</li>
     *         <li>分页插件</li>
     *         <li>乐观锁插件</li>
     *     </ol>
     * </p>
     * 对于 MyBatis Plus 的内部拦截器,推荐的配置顺序如下:
     * <p>
     *     <ol>
     *         <li>动态表名: DynamicTableNameInnerInterceptor</li>
     *         <li>多租户: TenantLineInnerInterceptor</li>
     *         <li>数据权限（自定义拦截器类）: MyDataPermissionInterceptor</li>
     *         <li>防止全表更新/删除: BlockAttackInnerInterceptor</li>
     *         <li>分页: PaginationInnerInterceptor</li>
     *         <li>乐观锁: OptimisticLockerInnerInterceptor</li>
     *         <li>SQL 性能规范: IllegalSQLInnerInterceptor</li>
     *     </ol>
     * </p>
     *
     * @return 返回 Mybatis Plus 拦截器对象 {@link MybatisPlusInterceptor}
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 多租户插件 - 插件 TenantLineInnerInterceptor 类在插件依赖 mybatis-plus-jsqlparser 下
        if (datasourceConfigurationProperties.getIsEnableTenant()) {
            TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
            tenantInterceptor.setTenantLineHandler(new CustomTenantHandler());
            interceptor.addInnerInterceptor(tenantInterceptor);
        }
        //防全表更新与删除插件 - 插件 BlockAttackInnerInterceptor 类在插件依赖 mybatis-plus-jsqlparser 下
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        //分页插件 - 插件 PaginationInnerInterceptor 类在插件依赖 mybatis-plus-jsqlparser 下
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MARIADB));
        //乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // SQL 性能规范插件/ 非法 SQL 拦截插件 - 插件 IllegalSQLInnerInterceptor 在 3.5.10 版本开始移除
        return interceptor;
    }

}
