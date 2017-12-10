package com.nicky.cache.redis;


import com.nicky.utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Component("redisClient")
public class RedisClient {
	
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private JSONStringSerializer  serializer;

	/*******************如下是string类型的基本操作*******************************/
	
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public boolean tryGetDistributedLock(String lockKey, Object value, int expireTime) {
		Object result = redisTemplate.execute(new SessionCallback<Object>() {

            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.watch(lockKey);
                operations.multi();
                operations.opsForValue().setIfAbsent(lockKey, value);
                operations.expire(lockKey, expireTime, TimeUnit.MILLISECONDS);
                return operations.exec();
            }
        });
        List<Boolean> res = (ArrayList)result;
        //判断事务中的两步操作是否都成功都成功事务执行成功
        return !CollectionUtils.isEmpty(res) && res.stream().allMatch(x -> x);
	}

	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	public Boolean exists(String key) {
    	return redisTemplate.hasKey(key);
    }
	
	public String type(final String key) {
		return redisTemplate.type(key).code();
    }
	
	public Long incr(String key) {
		return redisTemplate.opsForValue().increment(key, 1);
    }
	
	public Long incrBy(String key, long delta) {
    	return redisTemplate.opsForValue().increment(key, delta);
    }
	
    public Long decr(String key) {
    	return redisTemplate.opsForValue().increment(key, -1);
    }
    
    public Long decrBy(String key, long delta) {
    	return redisTemplate.opsForValue().increment(key, -delta);
    }

	public boolean expire(String key, long seconds) {
		return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
	}
	
	public boolean expire(String key, long timeout,TimeUnit unit) {
		return redisTemplate.expire(key, timeout, unit);
	}
	
	public boolean expireAt(String key, Date date) {
		return redisTemplate.expireAt(key, date);
	}
	
	public Long ttl(final String key) {
    	return redisTemplate.getExpire(key);
    }

	public boolean expireAt(final String key, final long unixTime) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) {
				try {
					return ((DefaultStringRedisConnection)connection).pExpireAt(key, unixTime);
				} catch (Exception e) {
					return ((DefaultStringRedisConnection)connection).expireAt(key, unixTime / 1000);
				}
			}
		}, true);
	}
	
    public String getSet(String key, String value) {
    	return redisTemplate.opsForValue().getAndSet(key, value);
    }
    
    public boolean setnx(String key, String value) {
    	return redisTemplate.opsForValue().setIfAbsent(key, value);
    }
    
    public void setex(String key, long seconds, String value) {
    	redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }
	
	public Integer append(String key, String value) {
    	return redisTemplate.opsForValue().append(key, value);
    }
	
    public Boolean setbit(String key, long offset, boolean value) {
    	return redisTemplate.opsForValue().setBit(key, offset, value);
    }
    
    public Boolean getbit(String key, long offset) {
    	return redisTemplate.opsForValue().getBit(key, offset);
    }
    
    public void setrange(String key, long offset, String value) {
    	redisTemplate.boundValueOps(key).set(value, offset);
    }

    public String getrange(String key, long startOffset, long endOffset) {
    	return redisTemplate.boundValueOps(key).get(startOffset, endOffset);
    }
    
    /**
     * 模糊获取key值
     */
    public Set<String> keys(String pattern){
    	return redisTemplate.keys(pattern);
    }
    
    /**
     * 重命名key
     */
    public void rename(String oldKey,String newKey){
    	redisTemplate.rename(oldKey, newKey);
    }
	
	/********hash********/
	public Boolean hexists(String key, String field) {
		return redisTemplate.opsForHash().hasKey(key, field);
	}
	
	public Set<String> hkeys(String key) {
		Set<Object> keySet = redisTemplate.boundHashOps(key).keys();
		if(keySet == null){
			return null;
		}
		
		Set<String> keys = new LinkedHashSet<String>();
		for(Object obj : keySet){
			String hkey = obj.toString();
			keys.add(hkey);
		}
		return keys;
	}
	
	public List<String> hvals(String key){
		List<Object> valueList = redisTemplate.boundHashOps(key).values();
		if(valueList == null){
			return null;
		}
		
		List<String> values = new ArrayList<String>();
		for(Object obj : valueList){
			values.add(obj.toString());
		}
		
		return values;
	}
	
	public Long hlen(String key) {
		return redisTemplate.boundHashOps(key).size();
	}
	
	public void hmset(String key, Map<String,String> map) {
		redisTemplate.opsForHash().putAll(key, map);
	}
	
	public List<String> hmget(final String key, List<String> fields) {
		final String[] fieldArr = fields.toArray(new String[fields.size()]);
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			public List<String> doInRedis(RedisConnection connection) {
				return ((DefaultStringRedisConnection)connection).hMGet(key, fieldArr);
			}
		}, true);
	}

	public Map<String, String> hgetAll(final String key) {
		Map<String, String> map=redisTemplate.execute(new RedisCallback<Map<String, String>>() {
			public Map<String, String> doInRedis(RedisConnection connection) {
				return ((DefaultStringRedisConnection)connection).hGetAll(key);
			}
		}, true);
		
		return map;
	}
	
    public void hset(String key, String field, String value) {
    	redisTemplate.opsForHash().put(key, field, value);
    }
	
	public String hget(String key, String field) {
		return (String)redisTemplate.opsForHash().get(key, field);
	}
	
	public Boolean hsetnx(String key, String field, String value) {
		return redisTemplate.boundHashOps(key).putIfAbsent(key, value);
	}
	
	public Long hincrBy(String key, String field, long delta) {
    	return redisTemplate.boundHashOps(key).increment(key, delta);
    }
	
	/********List********/
	public Long rpush(String key, String value) {
    	return redisTemplate.opsForList().rightPush(key, value);
    }
	
	public Long rpush(String key, String... values) {
    	return redisTemplate.opsForList().rightPushAll(key, values);
    }
	
	public Long rpushAll(String key, List<String> values) {
    	return redisTemplate.opsForList().rightPushAll(key, values);
    }

	public Long lpush(String key, String value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	public Long lpush(String key, String... values) {
		return redisTemplate.opsForList().leftPushAll(key, values);
	}
	
	public Long lpushAll(String key, List<String> values) {
		return redisTemplate.opsForList().leftPushAll(key, values);
	}
	
	public Long llen(String key) {
    	return redisTemplate.boundListOps(key).size();
    }
	
	public List<String> lrange(String key, long start,long end) {
		return redisTemplate.boundListOps(key).range(start, end);
	}
	
	public void ltrim(String key, long start, long end) {
		redisTemplate.boundListOps(key).trim(start, end);
    }
	
	public String lindex(String key, long index) {
    	return redisTemplate.boundListOps(key).index(index);
    }
	
	public void lset(String key, long index, String value) {
		redisTemplate.boundListOps(key).set(index, value);
	}
	
	public Long lrem(String key, long count,String value) {
		return redisTemplate.boundListOps(key).remove(count, value);
	}
	
	public String lpop(String key) {
		return redisTemplate.boundListOps(key).leftPop();
	}
	
	public String rpop(final String key) { 
		return redisTemplate.boundListOps(key).rightPop();
	}
	
	public Long lpushx(String key, String value) {
    	return redisTemplate.boundListOps(key).leftPushIfPresent(value);
    }
    
    public Long rpushx(String key, String value) {
    	return redisTemplate.boundListOps(key).rightPushIfPresent(value);
    }
	
    public List<String> sort(SortQuery query) {
    	return redisTemplate.sort(query);
    }
	
	/********Set********/
	public Long sadd(String key, String... members) {
    	return redisTemplate.boundSetOps(key).add(members);
    }
	
	public Long saddAll(final String key, Set<String> members) {
		final String[] memebersArr = ArrayUtils.toStringArray(members);
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) {
				return ((DefaultStringRedisConnection)connection).sAdd(key, memebersArr);
			}
		}, true);
    }
	
	public Set<String> smembers(String key) {
		return redisTemplate.boundSetOps(key).members();
	}
	
	public Long srem(final String key, final String... members) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) {
				return ((DefaultStringRedisConnection)connection).sRem(key, members);
			}
		}, true);
	}
	
	public Long sremAll(final String key, Set<String> members) {
		String [] strArr = ArrayUtils.toStringArray(members);
		return this.srem(key, strArr);
	}
	
	public String spop(String key) {
		return redisTemplate.boundSetOps(key).pop();
	}
	
	public Long scard(String key) {
		return redisTemplate.boundSetOps(key).size();
	}
	
	public Boolean sismember(String key, String member) {
		return redisTemplate.boundSetOps(key).isMember(member);
	}
	
	public String srandmember(String key) {
		return redisTemplate.boundSetOps(key).randomMember();
	}
	
	
	/********SortedSet********/
	public Boolean zadd(String key, double score, String member) {
		return redisTemplate.boundZSetOps(key).add(member, score);
	}
	
	public Long zrem(final String key, final String... members){
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) {
				return ((DefaultStringRedisConnection)connection).zRem(key, members);
			}
		}, true);
	}
	
	public Long zremAll(String key, Set<String> members){
		String [] strArr = ArrayUtils.toStringArray(members);
		return this.zrem(key, strArr);
	}
	
	public Double zincrby(String key, double score,String member) {
		return redisTemplate.boundZSetOps(key).incrementScore(member, score);
	}
	
	public Long zrank(final String key, final String member){
		return redisTemplate.boundZSetOps(key).rank(member);
	}
	
	public Long zrevrank(final String key, final String member){
		return redisTemplate.boundZSetOps(key).reverseRank(member);
	}
	
    public Set<String> zrange(String key, long start, long end) {
    	return redisTemplate.boundZSetOps(key).range(start, end);
	}  
      
	public Set<String> zrevrange(String key, long start,long end) {
		return redisTemplate.boundZSetOps(key).reverseRange(start, end);
    }

    public Long zcard(String key) {
    	return redisTemplate.boundZSetOps(key).size();
    }
    

    public Double zscore(String key, String member) {
    	return redisTemplate.boundZSetOps(key).score(member);
    }
    

    public Long zcount(String key, double min, double max) {
    	return redisTemplate.boundZSetOps(key).count(min, max);
    }
    
    public Set<String> zrangeByScore(String key, double min,double max) {
    	return redisTemplate.boundZSetOps(key).rangeByScore(min, max);
    }
    

    public Set<String> zrangeByScore(String key, double min,
	    double max, int offset, int count) {
    	org.springframework.data.redis.connection.RedisZSetCommands.Range range = org.springframework.data.redis.connection.RedisZSetCommands.Range.range();
    	range = range.gte(min).lte(max);
    	org.springframework.data.redis.connection.RedisZSetCommands.Limit limit = org.springframework.data.redis.connection.RedisZSetCommands.Limit.limit();
    	limit = limit.offset(offset).count(count);
    	
    	return redisTemplate.boundZSetOps(key).rangeByLex(range, limit);
    }


    public Set<String> zrevrangeByScore(String key, double max,double min) {
    	return redisTemplate.boundZSetOps(key).reverseRangeByScore(min, max);
    }
    
    public void zremrangeByRank(String key, long start,long end) {
    	redisTemplate.boundZSetOps(key).removeRange(start, end);
    }

    public void zremrangeByScore(String key, double min,double max) {
    	redisTemplate.boundZSetOps(key).removeRangeByScore(min, max);
    }

	/**
	 * 删除key
	 * @param key
	 */
	public void del(String key) {
		redisTemplate.delete(key);
	}
	

	
	/*******************如下是对象类型的基本操作*******************************/
	public <T> void setObj(String key, T value){
    	this.set(key, serializer.stringSerialize(value));
    }

	public <T> T getObj(String key){
        return this.getObj(key, null);
    }
	
	public <T> T getObj(String key,Class<T> objClass){
        return serializer.stringUnserialize(this.get(key),objClass);
    }
	
    public <T> T getSetObj(String key, T value){
        return this.getSetObj(key, value, null);
    }
    
    public <T> T getSetObj(String key, T value,Class<T> objClass){
        return serializer.stringUnserialize(this.getSet(key, serializer.stringSerialize(value)),objClass);
    }

    public <T> Boolean setnxObj(String key, T value){
    	return this.setnx(key, serializer.stringSerialize(value));
    }

    public <T> void setexObj(String key, int seconds, T value){
    	this.setex(key,seconds,serializer.stringSerialize(value));
    }
	
    public <T> void hmsetObj(String key, Map<String, T> hash){
    	Map<String,String> jsonStrMap = serializer.objMap2StrMap(hash);
        this.hmset(key, jsonStrMap);
    }
    
    public <T> List<T> hmgetObj(String key, List<String> fields){
    	return this.hmgetObj(key, fields, null);
    }
    
	public <T> List<T> hmgetObj(String key, List<String> fields, Class<T> objClass){
		List<String> jsonStrList = this.hmget(key, fields);
        return serializer.jsonStrList2ObjList(jsonStrList, objClass);
    }
	
	public <T> void hsetObj(String key, String field, T value){
        this.hset(key, field, serializer.stringSerialize(value));
    }

	public <T> T hgetObj(String key, String field){
		return this.hgetObj(key, field, null);
	}
	
	public <T> T hgetObj(String key, String field,Class<T> objClass){
        return serializer.stringUnserialize(this.hget(key,field),objClass);
    }
	
	public <T> Map<String, T> hgetAllObj(String key){
		return this.hgetAllObj(key,null);
	}
	
    public <T> Map<String, T> hgetAllObj(String key,Class<T> objClass){
    	Map<String,String> jsonStrMap = this.hgetAll(key);
    	return serializer.strMap2ObjMap(jsonStrMap, objClass);
    }

    public <T> void hsetnxObj(String key, String field, T value){
    	this.hsetnx(key,field, serializer.stringSerialize(value));
    }

    public <T> List<T> hvalsObj(String key){
    	return this.hvalsObj(key, null);
    }
    
    public <T> List<T> hvalsObj(String key,Class<T> objClass){
    	List<String> strList = this.hvals(key);
        return serializer.jsonStrList2ObjList(strList, objClass);
    }

    /**基于对象的List操作*/
    public <T> Long rpushObj(String key, T... args){
        return this.rpush(key, this.serializer.objArr2StrArr(args));
    }
    
    public <T> Long rpushAllObj(String key, List<T> objList){
        return this.rpushAll(key, this.serializer.objList2JsonStrList(objList));
    }

    public <T> Long lpushObj(String key, T... args){
    	return this.lpush(key, this.serializer.objArr2StrArr(args));
    }
    
    public <T> Long lpushAllObj(String key, List<T> objList){
    	return this.lpushAll(key, this.serializer.objList2JsonStrList(objList));
    }

    public <T> List<T> lrangeObj(String key, long start, long end){
    	return this.lrangeObj(key, start,end,null);
    }
    
    public <T> List<T> lrangeObj(String key, long start, long end,Class<T> objClass){
    	return this.serializer.jsonStrList2ObjList(this.lrange(key, start, end), objClass);
    }
    
    public <T> T lindexObj(String key, long index){
    	return this.lindexObj(key, index, null);
    }

    public <T> T lindexObj(String key, long index,Class<T> objClass){
    	return this.serializer.stringUnserialize(this.lindex(key, index), objClass);
    }

    public <T> void lsetObj(String key, long index, T value){
    	this.lset(key, index, this.serializer.stringSerialize(value));
    }

    public <T> Long lremObj(String key, long count, T value){
    	return this.lrem(key, count, this.serializer.stringSerialize(value));
    }

    public <T> T lpopObj(String key){
    	return this.lpopObj(key, null);
    }
    
    public <T> T lpopObj(String key,Class<T> objClass){
        return this.serializer.stringUnserialize(this.lpop(key), objClass);
    }

    public <T> T rpopObj(String key){
    	return this.rpopObj(key, null);
    }
    
    public <T> T rpopObj(String key,Class<T> objClass){
    	return this.serializer.stringUnserialize(this.rpop(key), objClass);
    }

    /**基于对象的Set操作*/
    public <T> Long saddObj(String key, T... member){
    	return this.sadd(key, this.serializer.objArr2StrArr(member));
    }
    
    public <T> Long saddAllObj(String key, Set<T> memberSet){
    	return this.saddAll(key, this.serializer.objSet2JsonStrSet(memberSet));
    }

    public <T> Set<T> smembersObj(String key){
    	return this.smembersObj(key, null);
    }
    
    public <T> Set<T> smembersObj(String key,Class<T> objClass){
        return this.serializer.jsonStrSet2ObjSet(this.smembers(key), objClass);
    }

    public <T> Long sremObj(String key, T... member){
        return this.srem(key, this.serializer.objArr2StrArr(member));
    }
    
    public <T> Long sremAllObj(String key, Set<T> members){
        return this.sremAll(key, this.serializer.objSet2JsonStrSet(members));
    }

    public <T> T spopObj(String key){
    	return this.spopObj(key,null);
    }
    
    public <T> T spopObj(String key,Class<T> objClass){
        return this.serializer.stringUnserialize(this.spop(key), objClass);
    }

    public <T> Boolean sismemberObj(String key, T member){
        return this.sismember(key, this.serializer.stringSerialize(member));
    }

    public <T> T srandmemberObj(String key){
    	return this.srandmemberObj(key, null);
    }
    
    public <T> T srandmemberObj(String key,Class<T> objClass){
        return this.serializer.stringUnserialize(this.srandmember(key), objClass);
    }

    
    /**基于对象的SortedSet操作*/
    public <T> Boolean zaddObj(String key, double score, T member){
        return this.zadd(key, score, this.serializer.stringSerialize(member));
    }

    public <T> Set<T> zrangeObj(String key, long start, long end){
    	return this.zrangeObj(key, start, end, null);
    }
    
    public <T> Set<T> zrangeObj(String key, long start, long end,Class<T> objClass){
        return this.serializer.jsonStrSet2ObjSet(this.zrange(key, start, end), objClass);
    }

    public <T> Long zremObj(String key, T... member){
        return this.zrem(key, this.serializer.objArr2StrArr(member));
    }
    
    public <T> Long zremAllObj(String key, Set<T> members){
        return this.zremAll(key, this.serializer.objSet2JsonStrSet(members));
    }

    public <T> Double zincrbyObj(String key, double score, T member){
        return this.zincrby(key, score, this.serializer.stringSerialize(member));
    }

    public <T> Long zrankObj(String key, T member){
        return this.zrank(key, this.serializer.stringSerialize(member));
    }

    public <T> Long zrevrankObj(String key, T member){
        return this.zrevrank(key, this.serializer.stringSerialize(member));
    }

    public <T> Set<T> zrevrangeObj(String key, long start, long end){
    	return this.zrevrangeObj(key, start, end, null);
    }
    
    public <T> Set<T> zrevrangeObj(String key, long start, long end,Class<T> objClass){
        return this.serializer.jsonStrSet2ObjSet(this.zrevrange(key, start, end), objClass);
    }

    public <T> Double zscoreObj(String key, T member){
        return this.zscore(key, this.serializer.stringSerialize(member));
    }

    public <T> Set<T> zrangeByScoreObj(String key, double min, double max){
    	return this.zrangeByScoreObj(key, min, max, null);
    }
    
    public <T> Set<T> zrangeByScoreObj(String key, double min, double max,Class<T> objClass){
        return this.serializer.jsonStrSet2ObjSet(this.zrangeByScore(key, min, max), objClass);
    }

    public <T> Set<T> zrevrangeByScoreObj(String key, double max, double min){
    	return this.zrevrangeByScoreObj(key, max, min, null);
    }
    
    public <T> Set<T> zrevrangeByScoreObj(String key, double max, double min,Class<T> objClass){
    	return this.serializer.jsonStrSet2ObjSet(this.zrevrangeByScore(key, max, min), objClass);
    }

    public <T> Set<T> zrangeByScoreObj(String key, double min, double max, int offset, int count){
    	return this.zrangeByScoreObj(key, min, max, offset, count, null);
    }
    
    public <T> Set<T> zrangeByScoreObj(String key, double min, double max, int offset, int count,Class<T> objClass){
    	return this.serializer.jsonStrSet2ObjSet(this.zrangeByScore(key, min, max, offset, count), objClass);
    }

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}
   
}
