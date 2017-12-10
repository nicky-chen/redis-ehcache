package com.nicky.config;

import com.nicky.cache.ercache.EhRedisCache;
import net.sf.ehcache.Cache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Collections;

/**
 * @author Nicky_chin  --Created on 2017/11/14
 * 管理二级缓存
 */
@Configuration
public class EhRedisCacheConfig {

    @Bean(name = "ehRedisCache")
    public EhRedisCache getEhRedisCacheObj(@Qualifier(value = "ehCache") EhCacheFactoryBean ehCacheFactoryBean) {

        EhRedisCache cache = new EhRedisCache();
        cache.setEhCache(new Cache(ehCacheFactoryBean));
        cache.setName("userCache");
        cache.setActiveCount(10);
        return cache;

    }

    /**
     *指定ehcache的主管理器为ehRedisCache
     */
    @Primary
    @Bean(name = "ehRedisCacheManager")
    public SimpleCacheManager getSimpleCacheManagerObj(@Qualifier(value = "ehRedisCache") EhRedisCache cache) {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Collections.singletonList(cache));
        return manager;
    }

}
