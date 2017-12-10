package com.nicky.cache.ercache;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Cacheable(value="defaultCache")//与配置
public @interface BusinessCacheable {

}
