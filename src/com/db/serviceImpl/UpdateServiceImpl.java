package com.db.serviceImpl;

import com.db.common.SqlInfo;
import com.db.dao.UpdateDao;
import com.db.service.UpdateService;

import java.util.List;

/**
 * Created by mee on 16-2-29.
 */
public class UpdateServiceImpl implements UpdateService {
    private UpdateDao updateDao;

    public UpdateDao getUpdateDao() {
        return updateDao;
    }

    public void setUpdateDao(UpdateDao updateDao) {
        this.updateDao = updateDao;
    }

    @Override
    public int updateData(List<SqlInfo> sqlInfos) {

        return updateDao.updateToDb(sqlInfos);
    }
}
