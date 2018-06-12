package com.intellif.core.db;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-10)
public class ReadOnlyInterceptor{
    private static Logger logger = Logger.getLogger(ReadOnlyInterceptor.class);


    @Around("@annotation(com.intellif.annotation.ReadOnly)")
    public Object proceed(ProceedingJoinPoint point)throws Throwable{
        try{
           logger.info(">>>>>>>>>>>>设置只读数据库");
           DbContextHolder.setDbType(DbContextHolder.DbType.SLAVE);
           Object result = point.proceed();
           return result;
        }finally {
            DbContextHolder.clearDbType();
        }
    }
}
