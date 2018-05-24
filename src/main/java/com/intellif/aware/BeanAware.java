package com.intellif.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BeanAware implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
       // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>bean对象初始化前调用");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>bean对象初始化后调用");
        return bean;
    }
}
