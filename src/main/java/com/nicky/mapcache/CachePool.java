package com.nicky.mapcache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Nicky_chin  --Created on 2017/11/30
 * 缓存池
 */
public class CachePool {

    private static CachePool pool;

    private final transient ReentrantLock lock = new ReentrantLock();

    private static volatile boolean isCacheCreated = false;

    //缓存Map
    private  Map<Integer,Object> cacheMap;

    private CachePool(Map<Integer, Object> map) {
        this.cacheMap = map;
    }

    private static class CachePoolHandler{
        private static CachePool cachePool = new CachePool(new ConcurrentHashMap<>());
    }
    /**
     * 得到唯一实例
     */
    public static CachePool getInstance(){

        if (!isCacheCreated) {
            pool = CachePoolHandler.cachePool;;
            isCacheCreated = true;
            //用于清除失效的value
            ClearCache clearCache = new ClearCache();
            clearCache.start();
        }
        return pool;
    }
    /**
     * 清除所有Item缓存
     */
    public void clearAllItems(){
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            cacheMap.clear();
        }finally {
            lock.unlock();
        }

    }
    /**
     * 获取缓存实体
     */
    public Object getCacheItem(Integer key){
        if(!cacheMap.containsKey(key)){
            return null;
        }
        CacheObject cacheObject = (CacheObject) cacheMap.get(key);
        //判断是否过期
        if(cacheObject.isExpired()){
            //removeCacheItem(key);
            return null;
        }
        return cacheObject.getCacheModel();
    }
    /**
     * 存放缓存信息
     */
    public void putCacheItem(Integer key, Object obj, Long expires) {
        boolean flag = cacheMap.containsKey(key);
        if (!flag) {
            if(expires == null){
                cacheMap.put(key, new CacheObject(obj));
            }
            cacheMap.put(key, new CacheObject(obj, expires));
        }
        if (flag) {
            CacheObject cacheObject = (CacheObject) cacheMap.get(key);
            if (cacheObject.isExpired()) {
                if(expires == null){
                    cacheMap.put(key, new CacheObject(obj));
                }
                cacheMap.put(key, new CacheObject(obj, expires));
            }
        }
    }
    /**
     * 移除缓存数据
     */
    public void removeCacheItem(Integer key){
        if(!cacheMap.containsKey(key)){
            return;
        }
        final ReentrantLock lock = this.lock;
        lock.tryLock();
        try {
            cacheMap.remove(key);
        }finally {
            lock.unlock();
        }

    }

    /**
     * 清除缓存的类
     */
    private static class ClearCache extends Thread {

        @Override
        public void run() {
            while (true) {
                pool.clearAllItems();
                try {
                    Thread.sleep(1000L * 10L);
                } catch (InterruptedException e) {
                    e.getLocalizedMessage();
                }
            }
        }
    }

    /**
     * 获取缓存数据的数量
     */
    public int getSize(){
        return cacheMap.size();
    }

    public Map<Integer, Object> getCacheMap() {
        return cacheMap;
    }

    public void setCacheMap(Map<Integer, Object> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public static void main(String[] args) {
        CachePool pool = CachePool.getInstance();
        pool.putCacheItem(2, 2, 1000 * 10L);
        System.out.println("ddd:" + pool.getCacheItem(2).toString());
        System.out.println(pool.getCacheMap());
        CachePool pools = CachePool.getInstance();
        pools.putCacheItem(3, 3, 10L);
        System.out.println(pool.getCacheMap());
        CachePool poolss = CachePool.getInstance();
        poolss.putCacheItem(4, 3, 5L);
        System.out.println(pool.getCacheMap());
    }
}
