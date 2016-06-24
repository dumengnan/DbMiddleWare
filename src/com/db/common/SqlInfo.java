package com.db.common;

/**
 * Created by mee on 16-1-13.
 */
public class SqlInfo {

    String db_type; //数据库类型
    String poolName;//连接池名字
    String sqlString;//sql语句

    public String getDb_type() {
        return db_type;
    }

    public void setDb_type(String db_type) {
        this.db_type = db_type;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getSqlString() {
        return sqlString;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }
}
