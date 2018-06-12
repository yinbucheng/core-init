package com.intellif.core.db;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-10)
public class DynamicSourceAop {

    @Autowired
    private FailSource failSource;

    @Around("@annotation(com.intellif.annotation.ReadOnly)")
    public Object proceed(ProceedingJoinPoint point) throws Throwable {
        try {
            DbContextHolder.slave();
            Object result = point.proceed();
            failSource.success(DbContextHolder.getSource());
            return result;
        } catch (Exception e) {
            //这里表示无法连接数据库异常
            if (e.getMessage().contains("Could not open connection")) {
                failSource.fail(DbContextHolder.getSource());
            }
            throw new RuntimeException(e);
        } finally {
            DbContextHolder.clear();
        }
    }
}
