package com.intellif.config;

import com.intellif.annotation.EnableLoggerConfiguration;
import com.intellif.aop.PrintArgDetailAop;
import com.intellif.aop.TimeUseAop;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志打印控制
 */
@Configuration
@ConditionalOnBean(annotation = {EnableLoggerConfiguration.class})
public class LogPrintAutoConfig {

    @Bean
    public BeanPostProcessor beanPostProcessor(){
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                Class objClz;
                if(AopUtils.isAopProxy(bean)){
                    objClz = AopUtils.getTargetClass(bean);
                }else{
                    objClz = bean.getClass();
                }
                if(objClz.getAnnotation(SpringBootApplication.class)!=null) {
                    PrintArgDetailAop.open=true;
                    TimeUseAop.open= true;
                }
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }
        };
    }
}
