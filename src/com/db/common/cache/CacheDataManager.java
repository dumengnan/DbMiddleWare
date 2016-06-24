package com.db.common.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mee on 16-2-24.
 */

public class CacheDataManager{

    private final static int MAXCACHE_SIZE = 100;

    private static ConcurrentHashMap<String,CacheData> cache = new ConcurrentHashMap<>();

    //获得缓存数据对象
    public synchronized static CacheData getCacheData(String sqlString){

        System.out.println("Get Data in Cache");

        CacheData tempCache = cache.get(sqlString);

        //每次访问缓存数据时更新时间戳
        if(tempCache != null){
            int newTime = getCurrentTime();
            tempCache.setTimeStamp(newTime);
            return tempCache;
        }

        return null;
    }

    //判断缓存是否存在
    public synchronized static boolean isExistCache(String sqlString){

        if(cache.containsKey(sqlString))
            return true;
        else
            return false;

    }

    public static int getCurrentTime(){
        return (int) (System.currentTimeMillis()/1000L);
    }

    //清除所有缓存
    private synchronized static void clearAllCache(){
        cache.clear();
    }

    //清除指定缓存
    private synchronized static void clearCache(String key){
        cache.remove(key);
    }

    //将数据放入缓存中
    public synchronized static void putCacheData(String key, CacheData resultData){

        System.out.println("Put Data in Cache");

        if(getCacheDataSize() < MAXCACHE_SIZE){
            putDataToCache(key, resultData);
        }else{
            //缓存达到上限时,采用LRU算法移出相应的缓存
            updateCache();
            putDataToCache(key, resultData);
        }
    }

    public synchronized static void putDataToCache(String key, CacheData resultData){

        CacheData tempCache = cache.get(key);
        if(tempCache == null){
            tempCache = cache.putIfAbsent(key, resultData);
            if(tempCache == null){
                tempCache = resultData;
            }
        }

    }

    public synchronized static void updateCache(){

        int minTimeStamp = getCurrentTime();
        String removeKey = null;

        //找出最小时间戳(即最近最久未使用)对应的key
        for(Map.Entry<String,CacheData> entry: cache.entrySet()){
            String key = entry.getKey();
            CacheData tempCache = entry.getValue();
            if(tempCache.getTimeStamp() < minTimeStamp){
                removeKey = key;
                minTimeStamp = tempCache.getTimeStamp();
            }
        }

        clearCache(removeKey);
    }

    //获取缓存大小
    public static int getCacheDataSize(){
        return cache.size();
    }

}
