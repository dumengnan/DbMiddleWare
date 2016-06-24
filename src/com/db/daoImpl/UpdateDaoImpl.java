package com.db.daoImpl;

import com.db.common.SqlInfo;
import com.db.common.jdbc.DBConnectionManager;
import com.db.dao.UpdateDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by mee on 16-2-29.
 */
public class UpdateDaoImpl implements UpdateDao {
    @Override
    public int updateToDb(List<SqlInfo> sqlInfos) {
        DBConnectionManager connectionManager = DBConnectionManager.getInstance();

        Connection con = null;
        Statement stmt = null;
        for(SqlInfo sqlInfo:sqlInfos){

            String poolName = sqlInfo.getPoolName();
            String updateString = sqlInfo.getSqlString();

            try{
                con = connectionManager.getConnection(poolName);
                stmt = con.createStatement();

                stmt.executeUpdate(updateString);

                if(stmt != null)
                    stmt.close();

            }catch (SQLException se){
                se.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                connectionManager.freeConnection(poolName, con);
            }
        }

        return 0;
    }
}
