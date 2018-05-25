package com.intellif.aware;

import com.intellif.aware.BeanAware;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;

public class BeanRegister {

    public static void registerBean(ApplicationContext applicationContext){
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        Class clazz = BeanWrapper.class;
        beanDefinition.setBeanClassName(clazz.getName());
        beanFactory.registerBeanDefinition(clazz.getName(), beanDefinition);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>注册成功");
    }
}
