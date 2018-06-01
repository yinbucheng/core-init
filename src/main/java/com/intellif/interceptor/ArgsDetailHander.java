package com.intellif.interceptor;

import com.intellif.annotation.Print;
import com.intellif.annotation.PrintAll;
import com.intellif.annotation.PrintArgsDetail;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.apache.log4j.Logger;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

public class ArgsDetailHander extends Hander {
    private Logger logger = Logger.getLogger(ArgsDetailHander.class);
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
                public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                    Method sourceMethod = clazz.getMethod(method.getName(),method.getParameterTypes());
                    if(sourceMethod.getAnnotation(PrintArgsDetail.class)==null&&sourceMethod.getAnnotation(PrintAll.class)==null){
                        return method.invoke(o,args);
                    }
                    StringBuilder sb = getAgrsDetails(method, args, clazz);
                    logger.info(sb.toString());
                    Object result = methodProxy.invoke(o,args);
                    return result;
                }
            });
            return enhancer.create();
        }else{
            return Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Method realMethod = clazz.getMethod(method.getName(),method.getParameterTypes());
                    if(realMethod.getAnnotation(PrintArgsDetail.class)==null&&realMethod.getAnnotation(PrintAll.class)==null){
                        return method.invoke(o,args);
                    }
                    StringBuilder sb = getAgrsDetails(method, args, clazz);
                    logger.info(sb.toString());
                    return method.invoke(o,args);
                }
            });
        }
    }

    private StringBuilder getAgrsDetails(Method method, Object[] args, Class clazz) throws NoSuchMethodException {
        String[] ignore = null;
        Method sourceMethod = clazz.getMethod(method.getName(),method.getParameterTypes());
        PrintArgsDetail annotation = sourceMethod.getAnnotation(PrintArgsDetail.class);
        if(annotation!=null){
            ignore = annotation.ignore();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>参数封装 "+clazz.getName()+":"+method.getName()+"(");
        List<String> paramNames = listParamNames(clazz,method.getName());
        if(paramNames!=null){
            int length = paramNames.size();
            StringBuilder sb2 = new StringBuilder();
            for (int i=0;i<length;i++){
                String name = paramNames.get(i);
                if(ignore(ignore,name))
                    continue;
                sb2.append(name).append("=").append(args[i]);
                sb2.append(",");
            }
            String content = sb2.toString();
            if(content.endsWith(",")){
                content = content.substring(0,content.length()-1);
            }
           sb.append(content);
        }
        sb.append(")");
        return sb;
    }

    private boolean ignore(String[] ignore,String name){
        if(ignore==null||ignore.length==0)
            return false;
        for(String temp:ignore){
            if(temp.equals(name))
                return true;
        }
        return false;
    }

    public static List<String> listParamNames(Class target, String methodName){
        ClassPool pool = ClassPool.getDefault();
        try{
            CtClass ctClass = pool.get(target.getName());
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);
            MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                    .getAttribute(LocalVariableAttribute.tag);
            List<String> paramNames = new LinkedList<String>();
            if (attr != null) {
                int len = ctMethod.getParameterTypes().length;
                int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
                for (int i = 0; i < len; i++) {
                    String key = attr.variableName(i + pos);
                    paramNames.add(key);
                }
            }
            return paramNames;

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
