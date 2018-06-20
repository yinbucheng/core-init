package com.intellif.core.db;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用来统计连接失败的datasource
 */
//@Component
public class FailSource {

    private Integer maxFail = 2;

    private Map<String, Integer> failCount = new HashMap<>();

    /**
     * 连接失败
     *
     * @param sourceName
     */
    public synchronized void fail(String sourceName) {
        Integer count = failCount.get(sourceName);
        if (count == null)
            count = 0;
        count += 1;
        if (count >= maxFail) {
            DbContextHolder.fail(sourceName);
        } else {
            failCount.put(sourceName, count);
        }
    }


    /**
     * 连接成功
     *
     * @param sourceName
     */
    public void success(String sourceName) {
        if(failCount.get(sourceName)!=null) {
            synchronized (this) {
                failCount.remove(sourceName);
                DbContextHolder.success(sourceName);
            }
        }
    }
}
