package com.db.serviceImpl;

import com.cownew.cownewsql.imsql.ISQLTranslator;
import com.cownew.cownewsql.imsql.common.DialectManager;
import com.cownew.cownewsql.imsql.common.TranslateException;


import com.db.service.TransferSqlService;

/**
 * Created by mee on 16-1-19.
 */
public class TransferSqlServiceImpl implements TransferSqlService {

    @Override
    public String transferToTarget(String sqlType, String sqlString) {
        String[] resultSqls = null;

        try {
            ISQLTranslator tx = DialectManager.createTranslator(sqlType);
            resultSqls = tx.translateSQL(sqlString);
            System.out.println("******TransferSqlServiceImpl****** transfer result" + resultSqls[0]);
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultSqls[0];
    }
}
