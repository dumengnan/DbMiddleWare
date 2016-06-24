package com.db.common.sqlparser;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.pp.para.GFmtOpt;
import gudusoft.gsqlparser.pp.para.GFmtOptFactory;
import gudusoft.gsqlparser.pp.stmtformatter.FormatterFactory;

/**
 * Created by mee on 16-2-29.
 */
public class FormatSql {

    public String formatTargetSql(String sqlString){
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvpostgresql);
        sqlparser.sqltext = sqlString;
        String result = null;

        int ret = sqlparser.parse();
        if (ret == 0){
            GFmtOpt option = GFmtOptFactory.newInstance();

            result = FormatterFactory.pp(sqlparser, option);
            System.out.println(result);
        }else{
            System.out.println(sqlparser.getErrormessage());
        }

        return result;
    }

    public static void main(String args[]){
        String tempString =
                 "insert into employees_log select current_timestamp from upd;";

        //formatSql(tempString);
    }
}
