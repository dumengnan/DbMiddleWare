package com.db.daoImpl;

import com.db.common.SqlInfo;
import com.db.dao.ResolveInputDao;

import java.sql.*;
import java.util.*;

/**
 * Created by mee on 16-1-13.
 */

public class ResolveInputDaoImpl implements ResolveInputDao {

    @Override
    public List<SqlInfo> resolveInput(String queryTablename) {

        List<SqlInfo> sinfoArray = new ArrayList<SqlInfo>();

        System.out.println("******ResolveInputDaoImpl****** input string is "+ queryTablename);

        Connection con = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/dbmiddle","root","123456");

            stmt = con.createStatement();
            String querySql = "select * from datasource where tablename = "+"\"" + queryTablename + "\""+";";
            System.out.println("querysql is :"+ querySql);
            ResultSet res = stmt.executeQuery(querySql);
           while(res.next()){
               //将查询到的数据源分布情况组装成列表,返回给用户
               SqlInfo sqlInfo = new SqlInfo();

               String poolName = res.getString("poolname");
               sqlInfo.setPoolName(poolName);

               String dbType = res.getString("dbtype");
               sqlInfo.setDb_type(dbType);

               sinfoArray.add(sqlInfo);

               System.out.println("The pool name is :"+poolName);
               System.out.println("The sql type is :"+dbType);
           }

        }catch (SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e){
            //Handle errors for others
            e.printStackTrace();
            System.out.println("Mysql error: " + e.getMessage());
        }finally {
                try{
                    if(stmt != null)
                        stmt.close();
                }catch (Exception e){

                }
                try{
                    if(con != null)
                        con.close();
                }catch (Exception e){

                }
        }

        return sinfoArray;
    }
}
