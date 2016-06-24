package com.db.servlet;

import com.db.common.jdbc.DBConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by mee on 16-2-29.
 */
public class StartServlet extends HttpServlet {
    public StartServlet(){
        super();
    }

    public void init() throws ServletException{
        System.out.println("StartServlet");
        DBConnectionManager connectionManager = DBConnectionManager.getInstance();
    }
}
