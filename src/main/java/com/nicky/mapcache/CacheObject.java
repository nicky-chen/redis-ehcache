package com.nicky.mapcache;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

/**
 * @author Nicky_chin  --Created on 2017/11/30
 */
public class CacheObject implements Serializable {

    private static final long serialVersionUID = -7283811206226772793L;

    private static final long EXPIRE_TIME = 60 * 5L;

    //创建缓存的时间
    private long createTime = Instant.now().toEpochMilli();

    //缓存期满的时间
    private long expireTime;

    //缓存的实体
    private Object cacheModel;

    public CacheObject(Object obj) {
        this.cacheModel = obj;
        this.expireTime = EXPIRE_TIME;
    }

    public CacheObject(Object obj,long expires){
        this.cacheModel = obj;
        this.expireTime = expires;
    }

    /**
     * 对象是否失效
     */
    public boolean isExpired(){
        return !Duration.between(Instant.ofEpochMilli(this.getCreateTime() + this.getExpireTime()), Instant.now()).isNegative();
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Object getCacheModel() {
        return cacheModel;
    }

    public void setCacheModel(Object cacheModel) {
        this.cacheModel = cacheModel;
    }
}
