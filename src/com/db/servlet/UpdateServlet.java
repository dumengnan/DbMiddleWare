package com.db.servlet;

import com.db.common.SqlInfo;
import com.db.service.ResolveService;
import com.db.service.UpdateService;
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
import java.util.List;

/**
 * Created by mee on 16-1-12.
 */
public class UpdateServlet extends HttpServlet{

    @Autowired
    private ResolveService resolveService;

    @Autowired
    private UpdateService updateService;

    @Override
    public void init(ServletConfig config) throws ServletException{
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
        super.init(config);
    }

    public void setUpdateService(UpdateService updateService) {
        this.updateService = updateService;
    }

    public void setResolveService(ResolveService resolveService) {
        this.resolveService = resolveService;
    }

    @Override
    public void doGet(HttpServletRequest request,HttpServletResponse response)
            throws ServletException,IOException {
        doPost(request,response);
    }

    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response)
            throws ServletException,IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf8");
        PrintWriter out = response.getWriter();


        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str;

        while((str = br.readLine()) != null){
            sb.append(str);
        }

        JSONObject resultObj = new JSONObject();

        resultObj = parseInput(sb.toString());

        out.print(resultObj);
    }

    public JSONObject parseInput(String inputString){

        List<SqlInfo> sqlinfos;
        String inputUpdate = null;
        JSONObject tempObj = new JSONObject();

        try{
            JSONObject jsonObject = JSONObject.fromObject(inputString);
            inputUpdate = jsonObject.getString("updateString");

            sqlinfos = resolveService.resolveInput(inputUpdate);
            int updateResult = updateService.updateData(sqlinfos);
            if(updateResult == 0)
                tempObj.put("SUCCESS",true);
            else
            tempObj.put("SUCCESS",false);

            System.out.println("update data finished");
        }catch (Exception e){
            e.printStackTrace();
        }

        return tempObj;
    }

}
