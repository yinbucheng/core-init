package com.intellif.core.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//@Configuration
//@Primary
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Autowired
    private ApplicationContext applicationContext;

    public DynamicRoutingDataSource() {
        init();
    }

    public void init() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mysql?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
        dataSource.setUsername("root");
        dataSource.setPassword("introcks1234");
        Map<Object, Object> tempMap = new HashMap<>();
        tempMap.put("temp", dataSource);
        setTargetDataSources(tempMap);
        setDefaultTargetDataSource(dataSource);
        super.afterPropertiesSet();
    }


    @Override
    public Object determineCurrentLookupKey() {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>choice db");
        return DbContextHolder.getSource();
    }

    //去掉DynamicRoutingDataSource这个对象
    private Map<Object, Object> excludeCurrentDataSource(Map<String, DataSource> dataSources) {
        Map<Object, Object> targetDataSource = new HashMap<>();
        Iterator<String> keys = dataSources.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!(dataSources.get(key) instanceof DynamicRoutingDataSource)) {
                targetDataSource.put(key, dataSources.get(key));
            }
        }
        return targetDataSource;
    }


    @Override
    public void afterPropertiesSet() {
        Map<String, DataSource> dataSource = applicationContext.getBeansOfType(DataSource.class);
        if (dataSource == null || dataSource.size() == 0)
            throw new RuntimeException("datasource not init ......");
        Map<Object, Object> targetDataSource = excludeCurrentDataSource(dataSource);
        //设置目标数据源
        setTargetDataSources(targetDataSource);
        //设置默认数据源
        setDefaultTargetDataSource(dataSource.get("master"));
        //初始化控制master和slave
        DbContextHolder.initSource(targetDataSource);
        super.afterPropertiesSet();
    }


}