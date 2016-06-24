package com.db.serviceImpl;

import com.db.common.SqlInfo;
import com.db.common.sqlparser.FormatSql;
import com.db.common.sqlparser.getStmtTables;
import com.db.dao.ResolveInputDao;
import com.db.service.ResolveService;
import com.db.service.SearchService;
import com.db.service.TransferSqlService;
import gudusoft.gsqlparser.EDbVendor;


import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mee on 16-1-13.
 */
public class ResolveServiceImpl implements ResolveService{
    private ResolveInputDao resolveInputDao;
    private SearchService searchService;
    private TransferSqlService transferSqlService;

    public TransferSqlService getTransferSqlService() {
        return transferSqlService;
    }

    public void setTransferSqlService(TransferSqlService transferSqlService) {
        this.transferSqlService = transferSqlService;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public ResolveInputDao getResolveInputDao() {
        return resolveInputDao;
    }

    public void setResolveInputDao(ResolveInputDao resolveInputDao) {
        this.resolveInputDao = resolveInputDao;
    }

    @Override
    public List<SqlInfo> resolveInput(String inputstring) {

        System.out.println("******ResolveServiceImpl****** input string is " + inputstring);

        //去除sql语句后面的分号
        String tempString = inputstring.replace(";","");
        tempString = tempString.trim();
        tempString = tempString.toLowerCase();

        String tableName = getTableName(tempString);

        List<SqlInfo> sqlInfos;

        //获取数据源分布式情况,并将查询得到的相关数据源信息存进数组中
        sqlInfos = resolveInputDao.resolveInput(tableName);

        //将SQL语句转换为目标数据库的语法类型
        for(SqlInfo sqlinfo:sqlInfos){

            System.out.println(sqlinfo.getDb_type());
            if(!sqlinfo.getDb_type().equals("postgreSQL")){
                String targetSql = transferSqlService.transferToTarget(sqlinfo.getDb_type(),inputstring);

                sqlinfo.setSqlString(targetSql);
            }else{
                FormatSql formatSql = new FormatSql();
                inputstring = formatSql.formatTargetSql(inputstring);

                sqlinfo.setSqlString(inputstring);
            }
        }

        return sqlInfos;
    }

    //获取表名
    private String getTableName(String sqlString){
        getStmtTables tables  = new getStmtTables(sqlString, EDbVendor.dbvgeneric);
        Set<String> sourceTableName = new LinkedHashSet<>();
        Set<String> targetTableName = new LinkedHashSet<>();

        String result = null;

        sourceTableName = tables.getSourceTable();
        targetTableName = tables.getTargetTable();

        if(sourceTableName.size() > 0){
            Iterator<String> it = sourceTableName.iterator();
            while(it.hasNext()){
                String str = it.next();
                result = str;
            }
        }

        if(targetTableName.size() >= 0 ){
            Iterator<String> it = targetTableName.iterator();
            while(it.hasNext()){
                String str = it.next();
                result = str;
            }
        }

        System.out.println("return the table name is " + result);

        return result;
    }

}
