package com.db.dao;

import com.db.common.SqlInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by mee on 16-1-13.
 */
public interface SearchDao {
    List<Map<String, Object>> searchFromDb(List<SqlInfo> sqlInfos);
}
