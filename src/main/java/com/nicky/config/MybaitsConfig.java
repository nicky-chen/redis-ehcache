package com.nicky.config;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author Nicky_chin  --Created on 2017/10/10
 */
@Configuration
public class MybaitsConfig {

    @Bean(name = "pageHelper")
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");
       //设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用和startPage中的pageNum效果一样
        properties.setProperty("offsetAsPageNum", "true");
        //该参数默认为false 设置为true时，使用RowBounds分页会进行count查询
        properties.setProperty("rowBoundsWithCount", "true");
        //设置为true时，如果pageSize=0或者RowBounds.limit = 0就会查询出全部的结果（相当于没有执行分页查询，但是返回结果仍然是Page类型
        properties.setProperty("pageSizeZero", "false");
        //3.3.0版本可用 - 分页参数合理化，默认false禁用 启用合理化时，如果pageNum<1会查询第一页，
        // 如果pageNum>pages会查询最后一页 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据 -->
        properties.setProperty("reasonable", "false");
        //3.5.0版本可用 - 为了支持startPage(Object params)方法 增加了一个`params`参数来配置参数映射，
        // 用于从Map或ServletRequest中取值可以配置pageNum,pageSize,count,pageSizeZero,reasonable,不配置映射的用默认值
        properties.setProperty("params", "pageNum=start;pageSize=limit;");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

    @Bean(name = "orderConfig")
    public org.apache.ibatis.session.Configuration mybatisConfig(@Qualifier(value = "pageHelper") PageHelper pageHelper) {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(false);
        configuration.setUseGeneratedKeys(true);
        configuration.setDefaultExecutorType(ExecutorType.REUSE);
        configuration.setMapUnderscoreToCamelCase(true);
        //开启在嵌套查询时 resultMap的自动注入功能
        configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
        configuration.setLogImpl(StdOutImpl.class);
        configuration.addInterceptor(pageHelper);
        return configuration;
    }


}
