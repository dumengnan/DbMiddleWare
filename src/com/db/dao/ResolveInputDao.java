package com.db.dao;

import com.db.common.SqlInfo;

import java.util.List;

/**
 * Created by mee on 16-1-13.
 */
public interface ResolveInputDao {
    List<SqlInfo> resolveInput(String inputstring);
}
