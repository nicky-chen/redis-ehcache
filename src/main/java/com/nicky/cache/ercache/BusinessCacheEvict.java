package com.nicky.cache.ercache;

import org.springframework.cache.annotation.CacheEvict;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@CacheEvict(value="defaultCache")//与配置
public @interface BusinessCacheEvict {

}
