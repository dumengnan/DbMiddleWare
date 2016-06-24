package com.db.common.jdbc;

import org.jdom2.JDOMException;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


/**
 * Created by mee on 16-1-27.
 */
public class ParseDSConfig {

    public ParseDSConfig(){

    }

    public Vector readConfigInfo(String path){

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("/config/"+path);
        Vector dsConfig = null;

        try{

            dsConfig = new Vector();
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(in);

            Element root = doc.getRootElement();
            List pools = root.getChildren();
            Element pool = null;
            Iterator allPool = pools.iterator();
            while(allPool.hasNext()){
                pool = (Element)allPool.next();
                DSConfigBean dscBean = new DSConfigBean();
                dscBean.setType(pool.getChild("type").getText());
                dscBean.setName(pool.getChild("name").getText());

                dscBean.setDriver(pool.getChildText("driver"));
                dscBean.setUrl(pool.getChildText("url"));
                dscBean.setUsername(pool.getChildText("username"));
                dscBean.setPassword(pool.getChildText("password"));
                dscBean.setMaxconn(Integer.parseInt(pool.getChildText("maxconn")));
                dscBean.setMinconn(Integer.parseInt(pool.getChildText("minconn")));

                dsConfig.add(dscBean);
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (JDOMException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return dsConfig;
    }

    public void writeToConfig(String path,
                              DSConfigBean dsConfigBean) {

        Document document = null;
        Element root = null;
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        XMLOutputter outp = new XMLOutputter();

        try{
            SAXBuilder sb = new SAXBuilder();
            document = sb.build(in);

            root = document.getRootElement();

            Element poolInfo = new Element("pool");
            poolInfo.addContent(new Element("type").setText(dsConfigBean.getType()));
            poolInfo.addContent(new Element("name").setText(dsConfigBean.getName()));
            poolInfo.addContent(new Element("url").setText(dsConfigBean.getUrl()));
            poolInfo.addContent(new Element("driver").setText(dsConfigBean.getDriver()));
            poolInfo.addContent(new Element("username").setText(dsConfigBean.getUsername()));
            poolInfo.addContent(new Element("password").setText(dsConfigBean.getPassword()));
            poolInfo.addContent(new Element("minconn").setText(""+dsConfigBean.getMinconn()));
            poolInfo.addContent(new Element("maxconn").setText(""+dsConfigBean.getMaxconn()));

            document.getRootElement().addContent(poolInfo);


            outp.setFormat(Format.getPrettyFormat());
            outp.output(document, new FileWriter(this.getClass().getResource(path).getFile()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
