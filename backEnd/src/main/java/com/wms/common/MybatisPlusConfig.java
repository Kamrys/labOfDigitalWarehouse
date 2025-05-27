package com.wms.common;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO 拦截器
 */
@Configuration  // 用于标记一个类为配置类
public class MybatisPlusConfig {
    @Bean   // Bean是指由Spring容器管理的对象。Spring容器负责创建、配置和管理这些对象的生命周期。Bean通常是应用程序中的组件，如服务、存储库、控制器等
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // MyBatis-Plus框架中的一个拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加一个分页拦截器，指定数据库类型为MySQL
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
