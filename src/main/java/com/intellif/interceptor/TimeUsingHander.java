package com.intellif.interceptor;

import com.intellif.annotation.Print;
import com.intellif.annotation.PrintAll;
import com.intellif.annotation.PrintMethodTime;
import org.apache.log4j.Logger;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TimeUsingHander extends Hander {

    private Logger logger = Logger.getLogger(TimeUsingHander.class);

    @Override
    public Object procced(Object o,Object sourceObject) {
        Class clazz = sourceObject.getClass();
        Print print = (Print) clazz.getAnnotation(Print.class);
        if(print==null)
            return o;
        Class[] interfaces = clazz.getInterfaces();
        //采用cglib
        if(interfaces==null||interfaces.length==0){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(clazz);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object proxy, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    Method sourceMethod = clazz.getMethod(method.getName(),method.getParameterTypes());
                    if(sourceMethod.getAnnotation(PrintMethodTime.class)==null&&sourceMethod.getAnnotation(PrintAll.class)==null){
                        return method.invoke(o,objects);
                    }
                    long startTime =System.currentTimeMillis();
                    Object result = method.invoke(o,objects);
                    long endTime = System.currentTimeMillis();
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+clazz.getName()+":"+method.getName()+" 方法执行耗时为"+(endTime-startTime));
                    return result;
                }
            });
            return enhancer.create();
        }else{
            return Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Method realMethod = clazz.getMethod(method.getName(),method.getParameterTypes());
                    if(realMethod.getAnnotation(PrintMethodTime.class)==null&&realMethod.getAnnotation(PrintAll.class)==null){
                        return method.invoke(o,args);
                    }
                    long startTime = System.currentTimeMillis();
                    Object result = method.invoke(o,args);
                    long endTime = System.currentTimeMillis();
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+clazz.getName()+":"+method.getName()+" 方法执行耗时为"+(endTime-startTime));
                    return result;
                }
            });
        }
    }
}
