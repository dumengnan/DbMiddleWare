package com.db.service;

import java.util.List;
import com.db.common.SqlInfo;


/**
 * Created by mee on 16-1-13.
 */
public interface ResolveService {
    List<SqlInfo> resolveInput(String inputstring);
}
