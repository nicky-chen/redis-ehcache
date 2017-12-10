package com.nicky.cache.ercache;

import com.nicky.cache.redis.RedisClient;;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * 两级缓存，一级:ehcache,二级为redisCache
 *
 */
@Component("ehRedisCache")
public class EhRedisCache implements Cache {

	private static final Logger log = LoggerFactory.getLogger(EhRedisCache.class);

	private String name;

	/*** 一定容量的LRU队列 */
	@Autowired
	private net.sf.ehcache.Cache ehCache;

	@Autowired
	private RedisClient redisClient;

	/**
	 * 缓存命中次数
	 */
	private int activeCount = 10;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return this;
	}

	@Override
	public ValueWrapper get(Object key) {
		final String keyStr = key.toString();
		Element value = ehCache.get(keyStr);
		log.info("Cache L1 (ehcache) :{}={}", keyStr, value);
		if (value != null) {
			// TODO 访问10次EhCache 强制访问一次redis 使得数据不失效
            //缓存命中次数统计
			if (value.getHitCount() < activeCount) {
				return new SimpleValueWrapper(value.getObjectValue());
			} else {
                //统计次数重置为0
				value.resetAccessStatistics();
			}
		}

		Object obj = redisClient.getObj(keyStr);
        ehCache.put(new Element(keyStr, obj));// 取出来之后缓存到本地
		log.info("Cache L2 (redis) :{}={}", keyStr, obj);

		return (obj != null ? new SimpleValueWrapper(obj) : null);
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		String keyStr = key.toString();
		Element value = ehCache.get(keyStr);
		log.info("Cache L1 (ehcache) :{}={}", keyStr, value);
		if (value != null) {
			// TODO 访问10次EhCache 强制访问一次redis 使得数据不失效
			if (value.getHitCount() < activeCount) {
				return (value != null ? (T) value.getObjectValue() : null);
			} else {
				value.resetAccessStatistics();
			}
		}

		T obj = redisClient.getObj(keyStr, type);
		ehCache.put(new Element(keyStr, obj));// 取出来之后缓存到本地
		log.info("Cache L2 (redis) :{}={}", key, obj);
		return obj;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		return null;
	}

	@Override
	public void put(Object key, Object value) {
		log.info("Cache put :{}={}", key, value);
		final String keyStr = key.toString();
		//缓存5分钟
        redisClient.tryGetDistributedLock(keyStr, value, 1000 * 60 * 5);
        ehCache.put(new Element(keyStr, value));
	}

	@Override
	public void evict(Object key) {
		log.info("Cache evict :{}", key);
		final String keyStr = key.toString();
		ehCache.remove(keyStr);
	}

	@Override
	public void clear() {
		ehCache.removeAll();
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(int activeCount) {
		this.activeCount = activeCount;
	}

	public net.sf.ehcache.Cache getEhCache() {
		return ehCache;
	}

	public void setEhCache(net.sf.ehcache.Cache ehCache) {
		this.ehCache = ehCache;
	}

}
