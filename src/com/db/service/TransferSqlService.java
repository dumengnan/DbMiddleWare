package com.db.service;

/**
 * Created by mee on 16-1-19.
 */
public interface TransferSqlService {
    String transferToTarget(String sqlType, String sqlString);
}
