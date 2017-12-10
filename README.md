# redis-ehcache
redis与ehcache实现二级缓存，ehcache做一级缓存，redis做二级缓存
构建ehcache+redis两级缓存

设计方案：
springboot 默认cache类提供多种缓存接口，根据修改缓存管理器的缓存规则可以重新指定缓存策略
所以我们可以创建一个二级缓存的bean，让管理器去控制，则可以实现二级缓存，
不管使用ehcache 或者 caffine，或者其他的本地缓存框架原理都是一样的；

mapcache中 写了一个简单的本地缓存池实现
后期课题：缓存实现中加入缓存命中，持久化，重写缓存mao类，缓存时间， 参数动态配置 --2017.12.10

