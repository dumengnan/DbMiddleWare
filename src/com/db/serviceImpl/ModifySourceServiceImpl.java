package com.db.serviceImpl;

import com.db.common.jdbc.DSConfigBean;
import com.db.common.jdbc.ParseDSConfig;
import com.db.service.ModifySourceService;

import java.util.Vector;

/**
 * Created by mee on 16-3-4.
 */
public class ModifySourceServiceImpl implements ModifySourceService {

    @Override
    public Vector getSourceInfo() {

        Vector sourceInfos = new Vector();

        ParseDSConfig pd = new ParseDSConfig();

        sourceInfos = pd.readConfigInfo("ds.config.xml");

        return sourceInfos;
    }
}
