package com.intellif.aop;

import com.intellif.annotation.PrintArgsDetail;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
public class PrintArgDetailAop {

    private Logger logger = Logger.getLogger(PrintArgDetailAop.class);

    private static Map<String,Object> cache = new ConcurrentHashMap<>();

    public static boolean open = false;

    @Pointcut("@annotation(com.intellif.annotation.PrintArgsDetail)||@annotation(com.intellif.annotation.PrintAll)")
    private void testAop(){}

    @Before("testAop()")
    public void printArgDetail(JoinPoint joinPoint){
        if(!open){
            return;
        }

        try {
            Object[] args = joinPoint.getArgs();
            Object target = joinPoint.getTarget();
            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                MethodSignature ms = (MethodSignature) signature;
                Method currentMethod = target.getClass().getMethod(ms.getName(), ms.getParameterTypes());
                StringBuilder sb = getAgrsDetails(currentMethod, args, target.getClass());
                logger.info(sb.toString());
            }
        }catch (Exception e){
            throw new RuntimeException(e);
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
        List<String> paramNames = (List<String>) cache.get(clazz.getName()+":"+method.getName());
        if(paramNames==null) {
            paramNames = listParamNames(clazz, method.getName());
            cache.put(clazz.getName()+":"+method.getName(),paramNames);
        }
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
