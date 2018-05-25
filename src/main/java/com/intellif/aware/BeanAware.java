package com.intellif.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class BeanAware implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>bean对象初始化前调用");
        return bean;

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
       Annotation annotation =  clazz.getAnnotation(RestController.class);
       if(annotation==null)
        return bean;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
              RequestMapping requestMapping =   method.getAnnotation(RequestMapping.class);
                System.out.println(">>>>>>>>>>>>>>>方法调用前:"+method.getName()+" requestMapping:"+requestMapping.value()[0]);
                Object result = methodProxy.invokeSuper(o,objects);
                System.out.println(">>>>>>>>>>>>>>>>>>方法调用后");
                return result;
            }
        });
        return enhancer.create();
    }
}
