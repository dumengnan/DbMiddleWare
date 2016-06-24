package com.db.common.jdbc;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by mee on 16-1-27.
 */

public class DBConnectionManager {

    static private DBConnectionManager instance;//唯一数据库连接管理实例类
    static private int clients; //客户连接数

    private Vector drivers = new Vector();//驱动信息
    private Hashtable pools = new Hashtable(); //连接池

    /**
     * 实例化管理类
     */
    public DBConnectionManager(){
        this.init();
    }

    /**
     * 得到唯一实例管理类
     */
    static synchronized public DBConnectionManager getInstance(){
        if(instance == null){
            instance = new DBConnectionManager();
        }
        return instance;
    }

    /**
     * 释放连接
     * @param name
     * @param con
     */
    public void freeConnection(String name,Connection con){
        DBConnectionPool pool = (DBConnectionPool)pools.get(name); //根据关键名字得到连接
        if(pool != null){
            pool.freeConnection(con);//释放连接
        }
    }

    /**
     * 得到一个连接根据连接池的名字name
     * @param name
     * @return
     */
    public Connection getConnection(String name){
        DBConnectionPool pool = null;
        Connection con = null;
        pool = (DBConnectionPool)pools.get(name); //根据名字获取连接池
        con = pool.getConnection();//从连接池获得连接
        if(con != null)
            System.out.println(name+ "  Accept the connection ");
        return con;
    }

    /**
     * 释放所有连接
     */
    public synchronized void release(){
        Enumeration allpools = pools.elements();
        while(allpools.hasMoreElements()){
            DBConnectionPool pool = (DBConnectionPool)allpools.nextElement();
            if(pool != null)
                pool.release();
        }
        pools.clear();
    }

    /**
     * 创建连接池
     * @param dsb
     */
    private void createPools(DSConfigBean dsb){
        DBConnectionPool dbpool = new DBConnectionPool();
        dbpool.setName(dsb.getName());
        dbpool.setDriver(dsb.getDriver());
        dbpool.setUrl(dsb.getUrl());
        dbpool.setUser(dsb.getUsername());
        dbpool.setPassword(dsb.getPassword());
        dbpool.setMaxConn(dsb.getMaxconn());
        dbpool.setMinConn(dsb.getMinconn());
        dbpool.createMinConnection();//创建指定的最小连接数

        pools.put(dsb.getName(),dbpool);
    }

    /**
     * 初始化连接池的参数
     */
    private void init(){
        //加载驱动程序
        this.loadDrivers();
        //创建连接池
        Iterator alldriver = drivers.iterator();
        while(alldriver.hasNext()){
            this.createPools((DSConfigBean)alldriver.next());
        }
    }

    /**
     * 加载驱动程序
     */
    private void loadDrivers(){
        ParseDSConfig pd = new ParseDSConfig();

        drivers = pd.readConfigInfo("ds.config.xml");
    }

}
