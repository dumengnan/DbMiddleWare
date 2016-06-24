package com.db.service;

import com.db.common.SqlInfo;

import java.util.List;

/**
 * Created by mee on 16-2-29.
 */
public interface UpdateService {
    int updateData(List<SqlInfo> sqlInfos);
}
