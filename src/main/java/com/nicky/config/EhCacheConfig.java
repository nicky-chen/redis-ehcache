package com.nicky.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Nicky_chin  --Created on 2017/11/14
 */
@Configuration
@EnableCaching
public class EhCacheConfig {

    /*
    * ehCache 操作对象
    */
    @Bean(name = "ehCache")
    public EhCacheFactoryBean ehCacheCacheManagerBean(
            @Qualifier(value = "appEhCacheCacheManager") EhCacheCacheManager ehCacheCacheManager){
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheName("ehCache");
        ehCacheFactoryBean.setCacheManager(ehCacheCacheManager.getCacheManager());
        return ehCacheFactoryBean;
    }

    /*
    * ehcache 主要的管理器
    */
    //@Primary
    @Bean(name = "appEhCacheCacheManager")
    public EhCacheCacheManager ehCacheCacheManager(@Qualifier(value = "ehcacheManager") EhCacheManagerFactoryBean bean){
        return new EhCacheCacheManager (bean.getObject ());
    }


    @Bean(name = "ehcacheManager")
    public EhCacheManagerFactoryBean getEhCacheBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        //<!--true:单例，一个cacheManager对象共享；false：多个对象独立  -->
        cacheManagerFactoryBean.setShared(true);
        cacheManagerFactoryBean.setCacheManagerName("ehcacheManager");
        return  cacheManagerFactoryBean;
    }




}
