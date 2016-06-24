package com.db.dao;

import com.db.common.SqlInfo;

import java.util.List;

/**
 * Created by mee on 16-2-29.
 */
public interface UpdateDao {
    int updateToDb(List<SqlInfo> sqlInfos);
}
