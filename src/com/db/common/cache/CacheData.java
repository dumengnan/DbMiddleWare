package com.db.common.cache;


import java.util.List;
import java.util.Map;

/**
 * Created by mee on 16-2-24.
 */
public class CacheData {
    private String sqlCache;
    private List<Map<String, Object>> resultCache;
    private int timeStamp;

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSqlCache() {
        return sqlCache;
    }

    public void setSqlCache(String sqlCache) {
        this.sqlCache = sqlCache;
    }

    public List<Map<String, Object>> getResultCache() {
        return resultCache;
    }

    public void setResultCache(List<Map<String, Object>> resultCache) {
        this.resultCache = resultCache;
    }
}
