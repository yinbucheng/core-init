package com.intellif.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAop2 {

    @Pointcut("execution(public * com.intellif.web.*.*(..))")
    private void testAop(){}

    @Before("testAop()")
    public void test(JoinPoint joinPoint){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>test aop");
    }
}
