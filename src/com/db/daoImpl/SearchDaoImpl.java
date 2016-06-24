package com.db.daoImpl;

import com.db.common.SqlInfo;
import com.db.common.jdbc.DBConnectionManager;
import com.db.dao.SearchDao;


import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by mee on 16-1-13.
 */

public class SearchDaoImpl implements SearchDao {

    private final int COREPOOLSIZE = 10;
    private final int MAXPOOLSIZE = 10;
    private final int KEEPALIVETIME = 10;

    @Override
    public List<Map<String, Object>> searchFromDb(List<SqlInfo> sqlInfos) {

        DBConnectionManager connectionManager = DBConnectionManager.getInstance();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(COREPOOLSIZE,MAXPOOLSIZE,KEEPALIVETIME, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(5), new ThreadPoolExecutor.CallerRunsPolicy());

        List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
        long startTime = System.currentTimeMillis();

        System.out.println("******SearchDaoImpl******");

        for (SqlInfo sqlinfo : sqlInfos) {

            executor.execute(new Runnable() {

                @Override
                public void run() {
                    queryFromDb(sqlinfo,connectionManager,rows);
                }
            });
        }

        while(! executor.isTerminated()){
            try{
                executor.shutdown();
                executor.awaitTermination(10,TimeUnit.DAYS);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Search Action use time : "+(endTime-startTime) + " ms");

        return rows;
    }

    private void queryFromDb(SqlInfo sqlinfo,DBConnectionManager connectionManager,
                                                 List<Map<String,Object>> resultRows){

        String poolName = sqlinfo.getPoolName();
        String queryString = sqlinfo.getSqlString();

        Connection con = null;
        Statement stmt = null;

        try {
            con = connectionManager.getConnection(poolName);
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(queryString);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while(rs.next()){
                Map<String, Object> columns = new LinkedHashMap<String, Object>();

                for(int i = 1; i <= columnCount; i++){
                    columns.put(metaData.getColumnLabel(i),rs.getObject(i));
                    System.out.println("the columnLabel is "+metaData.getColumnLabel(i)+" "+rs.getObject(i));
                }

                synchronized (this){
                    resultRows.add(columns);
                }
            }

            System.out.println("pool Name is :" + sqlinfo.getPoolName());
            System.out.println("sql String is :" + sqlinfo.getSqlString());
            System.out.println("dbType is :" + sqlinfo.getDb_type());

            rs.close();
            stmt.close();
        }catch (SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();

        } catch (Exception e){
            e.printStackTrace();
        }finally {
            //free connection but not close
            connectionManager.freeConnection(poolName,con);
        }

    }

}
