package com.db.servlet;

import com.db.common.jdbc.DSConfigBean;
import com.db.common.jdbc.ParseDSConfig;
import com.db.service.ModifySourceService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by mee on 16-3-4.
 */
public class ModifySourceServlet extends HttpServlet {

    @Autowired
    private ModifySourceService modifySourceService;

    public void setModifySourceService(ModifySourceService modifySourceService) {
        this.modifySourceService = modifySourceService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf8");
        PrintWriter out = response.getWriter();

        String type = request.getParameter("type");

        if (type.equals("submitdata")) {

            StringBuilder sb = new StringBuilder();
            BufferedReader br = request.getReader();
            String str;

            while((str = br.readLine()) != null){
                sb.append(str);
            }

            submitDataToServer(sb.toString());

        } else {
            Vector sourceInfos = modifySourceService.getSourceInfo();

            JSONArray jsonArray = new JSONArray();

            Iterator sourceInfoDriver = sourceInfos.iterator();
            while (sourceInfoDriver.hasNext()) {

                JSONObject jsonObject = new JSONObject();
                DSConfigBean sourceinfo = (DSConfigBean) sourceInfoDriver.next();

                jsonObject.put("username", sourceinfo.getUsername());
                jsonObject.put("password", sourceinfo.getPassword());
                jsonObject.put("driver", sourceinfo.getDriver());
                jsonObject.put("maxconn", sourceinfo.getMaxconn());
                jsonObject.put("minconn", sourceinfo.getMinconn());
                jsonObject.put("name", sourceinfo.getName());
                jsonObject.put("type", sourceinfo.getType());
                jsonObject.put("url", sourceinfo.getUrl());

                jsonArray.add(jsonObject);
            }
            out.print(jsonArray);
        }
    }

    public void submitDataToServer(String sourceData){
        try{
            JSONObject jsonObject = JSONObject.fromObject(sourceData);
            String url = jsonObject.getString("url");
            String type = jsonObject.getString("type");
            String name = jsonObject.getString("name");
            String driver = jsonObject.getString("driver");
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            String maxconn = jsonObject.getString("maxconn");
            String minconn = jsonObject.getString("minconn");

            DSConfigBean dsBean = new DSConfigBean();
            dsBean.setName(name);
            dsBean.setDriver(driver);
            dsBean.setMaxconn(Integer.parseInt(maxconn));
            dsBean.setMinconn(Integer.parseInt(minconn));
            dsBean.setPassword(password);
            dsBean.setType(type);
            dsBean.setUrl(url);
            dsBean.setUsername(username);

            ParseDSConfig parseDSConfig = new ParseDSConfig();
            parseDSConfig.writeToConfig("/config/ds.config.xml",dsBean);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
