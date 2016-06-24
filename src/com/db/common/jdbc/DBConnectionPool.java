package com.db.common.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;


/**
 * Created by mee on 16-1-27.
 */

public class DBConnectionPool {
    private Connection con = null;
    private int inUsed = 0; //使用的连接数
    private ArrayList freeConnections = new ArrayList(); //存放空闲连接
    private int minConn;
    private int maxConn;
    private String name;
    private String password;
    private String url;
    private String driver;
    private String user;
    public Timer timer;

    public int getInUsed() {
        return inUsed;
    }

    public void setInUsed(int inUsed) {
        this.inUsed = inUsed;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public ArrayList getFreeConnections() {
        return freeConnections;
    }

    public void setFreeConnections(ArrayList freeConnections) {
        this.freeConnections = freeConnections;
    }

    public int getMinConn() {
        return minConn;
    }

    public void setMinConn(int minConn) {
        this.minConn = minConn;
    }

    public int getMaxConn() {
        return maxConn;
    }

    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public DBConnectionPool() {

    }

    /**
     * 创建连接池
     *
     * @param name
     * @param driver
     * @param Url
     * @param user
     * @param password
     * @param maxConn
     */
    public DBConnectionPool(String name, String driver, String Url, String user
            , String password, int maxConn) {
        this.name = name;
        this.driver = driver;
        this.url = Url;
        this.user = user;
        this.password = password;
        this.maxConn = maxConn;
    }

    /**
     * 释放连接(多线程同步方法)
     *
     * @param con
     */
    public synchronized void freeConnection(Connection con) {
        this.freeConnections.add(con); //添加到空闲连接的末尾
        this.inUsed--;
    }


    public synchronized void createMinConnection() {

        //确保连接池中存在最小连接个数

        while (this.freeConnections.size() < this.minConn) {

            System.out.println("create connection To ensure the minconnection");

            Connection con = newConnection();
            this.freeConnections.add(con);
        }
    }

    /**
     * 从连接池里得到连接
     */
    public synchronized Connection getConnection() {
        Connection con = null;
        if (this.freeConnections.size() > 0) {
            con = (Connection) this.freeConnections.get(0);
            this.freeConnections.remove(0); //从空闲连接中移除
            if (con == null)
                con = getConnection();
        } else {
            con = newConnection(); // 新建连接
        }

        if (this.maxConn == 0 || this.maxConn < this.inUsed) {
            con = null;
        }
        if (con != null) {
            this.inUsed++;
            System.out.println("receive " + this.name + "have " + this.inUsed + "connection");
        }

        return con;
    }

    /**
     * 释放全部连接
     */
    public synchronized void release() {
        Iterator allConns = this.freeConnections.iterator();
        while (allConns.hasNext()) {
            Connection con = (Connection) allConns.next();
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.freeConnections.clear();
    }

    /**
     * 创建新连接
     */
    private Connection newConnection() {
        try {
            Class.forName(driver); //jdbc注册驱动并实例化
            con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Can not find db driver");
        } catch (SQLException e1) {
            e1.printStackTrace();
            System.out.println("Can not create Connection");
        }
        return con;
    }

}
