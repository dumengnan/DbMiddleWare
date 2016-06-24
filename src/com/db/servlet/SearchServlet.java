package com.db.servlet;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.IOException;

import com.db.common.SqlInfo;
import com.db.common.cache.CacheData;
import com.db.common.cache.CacheDataManager;
import com.db.service.ResolveService;
import com.db.service.SearchService;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by mee on 15-12-28.
 */

public class SearchServlet extends HttpServlet{

    @Autowired
    private ResolveService resolveService;

    @Autowired
    private SearchService searchService;

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public void setResolveService(ResolveService resolveService) {
        this.resolveService = resolveService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException{
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest request,HttpServletResponse response)
            throws ServletException,IOException{

        doPost(request,response);
        System.out.println("SearchServlet is running");
    }

    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response)
            throws ServletException,IOException{

        response.setContentType("application/json");
        response.setCharacterEncoding("utf8");
        PrintWriter out = response.getWriter();


        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str;

        while((str = br.readLine()) != null){
            sb.append(str);
        }

        System.out.println("******SearchServlet****** input string is " + sb);

//        获取servlet上下文
//        ServletContext context = getServletContext();
//        WebApplicationContext applicationContext  =
//                WebApplicationContextUtils
//                .getWebApplicationContext(context);
//
//       resolveService = (ResolveService) applicationContext.getBean("resolveService");

        JSONArray returnobj = parseInput(sb);

        if(returnobj == null){
            ServletContext sc = getServletContext();
            RequestDispatcher rd = null;
            rd = sc.getRequestDispatcher("/result.jsp");
            rd.forward(request,response);
        }
        else
            out.print(returnobj);
    }

    //解析用户输入的json字符串
    public JSONArray parseInput(StringBuilder inputString){

        String inputSearch = "";
        JSONArray json_arr = null;
        List<SqlInfo> sqlinfos;
        List<Map<String, Object>> resultList = null;
        CacheDataManager cacheDataManager = new CacheDataManager();


        try{
            JSONObject jsonObject = JSONObject.fromObject(inputString.toString());
            inputSearch = jsonObject.getString("searchString");

            if(cacheDataManager.isExistCache(inputSearch)){
                resultList = cacheDataManager.getCacheData(inputSearch).getResultCache();
            }else{

                //分解用户的输入去各个数据库中查询
                sqlinfos = resolveService.resolveInput(inputSearch);

                //调用searchService去各数据源查询
                resultList = searchService.searchFromDb(sqlinfos);

                //将查询结果保存到缓存中
                CacheData cacheData = new CacheData();
                cacheData.setSqlCache(inputSearch);
                cacheData.setResultCache(resultList);
                cacheData.setTimeStamp(cacheDataManager.getCurrentTime());

                cacheDataManager.putCacheData(inputSearch,cacheData);
            }

            json_arr = new JSONArray();

            for(Map<String, Object>map: resultList){
                JSONObject json_obj = new JSONObject();
                for(Map.Entry<String,Object> entry:map.entrySet()){
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    try{
                        json_obj.put(key,value);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                json_arr.add(json_obj);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return json_arr;
    }

}
