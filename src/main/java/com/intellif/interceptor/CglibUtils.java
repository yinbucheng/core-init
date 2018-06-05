package com.intellif.interceptor;

import java.lang.annotation.Annotation;

public class CglibUtils {

    /**
     * 获取类上是否存在摸个注解
     * @param clazz
     * @param annClass
     * @return
     */
    public static Annotation getAnnotation(Class clazz, Class annClass){
        Annotation annotation =  clazz.getAnnotation(annClass);
        if(annotation!=null){
            return annotation;
        }
        if(clazz.getSuperclass()!=null){
            return getAnnotation(clazz.getSuperclass(),annClass);
        }
        return null;
    }


    /**
     * 判断当前接口是否存在cglib特定接口
     * @param datas
     * @return
     */
    public static boolean containsCgilib(Class[] datas){
        if(datas==null)
            return false;
        for(Class clazz:datas){
            if(clazz.equals(org.springframework.cglib.proxy.Factory.class)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取原始的基础类
     * @param clazz
     * @return
     */
    public static Class getBaseClass(Class clazz){
        if(clazz.getSuperclass().equals(Object.class))
            return clazz;
        return getBaseClass(clazz.getSuperclass());
    }
}
