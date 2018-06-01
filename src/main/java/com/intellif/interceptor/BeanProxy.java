package com.intellif.interceptor;

import com.intellif.annotation.Print;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class BeanProxy implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        Class clazz = o.getClass();
        if(clazz.getAnnotation(Print.class)==null)
            return o;
        Hander hander = new TimeUsingHander();
        Hander hander2 = new ArgsDetailHander();
        hander.setHander(hander2);
        return hander.hander(o,o);
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
      return o;
    }
}
