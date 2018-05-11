package com.intellif.core;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类
 * create by yinchong
 */
public abstract class ReflectUtils {

    //缓存类的字段
    private static Map<String,List<Field>> cache = new ConcurrentHashMap<>();

    //缓存类中id字段
    private static Map<String,Field> idsCache = new ConcurrentHashMap<>();

    public static Map<String,Object> createMapForNotNull(Object bean){
        List<Field> fields = listAllFields(bean.getClass());
        try {
            Map<String, Object> map = new HashMap<>();
            if (fields != null) {
                for (Field field : fields) {
                    Object value = field.get(bean);
                    if(value!=null){
                        String name = field.getName();
                        map.put(name,value);
                    }
                }
            }
            return map;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过反射获取制定的字段值
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getFiledValue(Object object,String fieldName){
        Class clazz = object.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取id注解的字段
     * @param clazz
     * @return
     */
    public static Field getIdField(Class clazz){
        String className = clazz.getName();
        Field field = idsCache.get(className);
        if(field!=null){
            return field;
        }
         List<Field> fields = listAllFields(clazz);
         if(fields==null){
             return null;
         }
         for(Field temp:fields){
             Id id = temp.getAnnotation(Id.class);
             if(id!=null){
                 idsCache.put(className,temp);
                 return temp;
             }
         }
         return null;
    }



    /**
     * 浅层次将对象转变为map
     * @param bean
     * @return
     */
    public static Map<String,Object> simpleReflectBeanToMap(Object bean)throws Exception{
        List<Field> fields = listAllFields(bean.getClass());
        if (fields == null || fields.size() == 0)
            return null;
        Map<String, Object> map = new HashMap<>();
        for (Field field : fields) {
            String name = field.getName();
            Object value = field.get(bean);
            if (isEmpty(value))
                continue;
            map.put(name, value);
        }
        return map;
    }

    /**
     * 深层次将对象转变为Map递归下去
     *
     * @param bean
     * @return
     * @throws Exception
     */
    public static Map<String, Object> reflectBeanToMap(Object bean) throws Exception {
        List<Field> fields = listAllFields(bean.getClass());
        if (fields == null || fields.size() == 0)
            return null;
        Map<String, Object> map = new HashMap<>();
        for (Field field : fields) {
            String name = field.getName();
            Object value = field.get(bean);
            if (isEmpty(value))
                continue;
            if (isSimpleType(field.getType())) {// 如果为基本类型
                map.put(name, value);
            } else {
                Map<String, Object> tempMap = reflectBeanToMap(value);
                map.put(name, tempMap);
            }
        }
        return map;
    }

    /**
     * 是否为null1
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(Object value) {
        return value == null;
    }

    /**
     * 是否为基本类型
     *
     * @param clazz
     * @return
     */
    public static boolean isSimpleType(Class clazz) {
        if (clazz.equals(int.class) || clazz.equals(Integer.class))
            return true;
        if (clazz.equals(String.class))
            return true;
        if (clazz.equals(long.class) || clazz.equals(Long.class))
            return true;
        if (clazz.equals(float.class) || clazz.equals(Float.class))
            return true;
        if (clazz.equals(double.class) || clazz.equals(Double.class))
            return true;
        if (clazz.equals(short.class) || clazz.equals(Short.class))
            return true;
        if (clazz.equals(char.class) || clazz.equals(Character.class))
            return true;
        if (clazz.equals(boolean.class) || clazz.equals(Boolean.class))
            return true;
        return clazz.equals(byte.class) || clazz.equals(Byte.class);

    }

    /**
     * 列出该类及其子类下面所以得非静态字段
     *
     * @param clazz
     * @return
     */
    public static List<Field> listAllFields(Class clazz) {
        List<Field> result = cache.get(clazz.getName());
        if(result!=null)
            return result;
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0)
            return null;
        result = new LinkedList<>();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            field.setAccessible(true);
            result.add(field);
        }
        if (clazz.getSuperclass() != null) {
            List<Field> temps = listAllFields(clazz.getSuperclass());
            if(temps!=null)
                result.addAll(temps);
        }
        cache.put(clazz.getName(), result);
        return result;
    }

    /**
     * 将简单的map映射成bean对象
     * @param data
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T simpleChangeMapToBean(Map<String,Object> data,Class clazz)throws Exception{
        List<Field> fields = listAllFields(clazz);
        T bean = (T) clazz.newInstance();
        if(fields!=null&&fields.size()>0){
            for(Field field:fields){
                String name = field.getName();
                Object value = data.get(name);
                if(isEmpty(value))
                    continue;
                field.set(bean, value);
            }
        }
        return bean;
    }

    /**
     * 深层次将map中的数据映射到bean中递归下去
     * @param data
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T changeMapToBean(Map<String,Object> data,Class clazz)throws Exception{
        List<Field> fields = listAllFields(clazz);
        T bean = (T) clazz.newInstance();
        if(fields!=null&&fields.size()>0){
            for(Field field:fields){
                String name = field.getName();
                Object value = data.get(name);
                if(isEmpty(value))
                    continue;
                if(isSimpleType(field.getType())){
                    field.set(bean, value);
                }else{
                    Map<String,Object> tempMap = (Map<String, Object>) value;
                    Object resultValue = changeMapToBean(tempMap,field.getType());
                    field.set(bean, resultValue);
                }
            }
        }
        return bean;
    }

    /**
     * 经对象上非静态熟悉映射到另一个对象属性上去
     * @param from 从哪个对象开始
     * @param to  到哪个对象
     */
    public static void reflectBean2Bean(Object from,Object to){
        Class fromClazz = from.getClass();
        Class toClazz = to.getClass();
       List<Field> fromFields =  listAllFields(fromClazz);
       List<Field> toFields = listAllFields(toClazz);

       try {
           if (fromFields != null && fromFields.size() != 0) {
               for (Field fromField : fromFields) {
                   Field toField = getFieldByNameAndTypeSame(toFields, fromField);
                   if (toField!=null) {
                       Object value = fromField.get(from);
                       toField.set(to,value);
                   }
               }
           }
       }catch (Exception e){
           throw new RuntimeException(e);
       }
    }


    private static Field getFieldByNameAndTypeSame(List<Field> datas,Field field){
        if(datas==null||datas.size()==0)
            return null;
        String name = field.getName();
        Class type = field.getType();
        for(Field data:datas){
            if(!data.getName().equals(name))
                continue;
            if(!data.getType().equals(type))
                continue;
            return data;
        }
        return null;
    }

}
