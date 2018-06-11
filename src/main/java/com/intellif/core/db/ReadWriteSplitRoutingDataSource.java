package com.intellif.core.db;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReadWriteSplitRoutingDataSource extends AbstractRoutingDataSource implements InitializingBean{

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected Object determineCurrentLookupKey() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>test");
        DbContextHolder.DbType dbType =  DbContextHolder.getDbType();
        if(dbType==null)
            dbType = DbContextHolder.DbType.MASTER;
        return dbType;
    }

    @Override
    public void afterPropertiesSet() {
        Map<Object,Object> map = new HashMap<>();
        map.put(DbContextHolder.DbType.SLAVE,applicationContext.getBean("slave"));
        map.put(DbContextHolder.DbType.MASTER,applicationContext.getBean("master"));
        setTargetDataSources(map);
        setDefaultTargetDataSource(applicationContext.getBean("slave"));
    }
}