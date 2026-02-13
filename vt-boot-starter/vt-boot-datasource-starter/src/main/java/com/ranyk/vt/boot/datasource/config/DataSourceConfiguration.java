package com.ranyk.vt.boot.datasource.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ranyk.vt.boot.datasource.handler.DataObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * CLASS_NAME: DataSourceConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据源配置类
 * @date: 2026-02-13
 */
@AutoConfiguration
@MapperScan("com.ranyk.vt.boot.**.repository")
public class DataSourceConfiguration {

    /**
     * 向 Spring Bean 容器中注入 自定义元对象处理器 对象
     *
     * @return 返回自定义元对象处理器对象 {@link DataObjectHandler}
     */
    @Bean
    public DataObjectHandler myMetaobjectHandler(){
        return new DataObjectHandler();
    }

    /**
     * 向 Spring Bean 容器中注入 Mybatis Plus 拦截器对象, 当下注册了(执行顺序如下):
     * <p>
     *     <ol>
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
        //防全表更新与删除插件 - 插件 BlockAttackInnerInterceptor 类在插件依赖 mybatis-plus-jsqlparser 下
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        //分页插件 - 插件 PaginationInnerInterceptor 类在插件依赖 mybatis-plus-jsqlparser 下
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MARIADB));
        //乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
