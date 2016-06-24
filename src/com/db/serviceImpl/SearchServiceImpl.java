package com.db.serviceImpl;

import com.db.common.SqlInfo;
import com.db.dao.SearchDao;
import com.db.service.SearchService;

import java.util.List;
import java.util.Map;

/**
 * Created by mee on 16-1-13.
 */
public class SearchServiceImpl implements SearchService{
    private SearchDao searchDao;

    public SearchDao getSearchDao() {
        return searchDao;
    }

    public void setSearchDao(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    @Override
    public List<Map<String,Object>> searchFromDb(List<SqlInfo> sqlInfos){
        System.out.println("******SearchServiceImpl******  searchFromdb");

       return  searchDao.searchFromDb(sqlInfos);
    }
}
