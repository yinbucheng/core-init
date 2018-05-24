package com.intellif.core.init;

import com.intellif.core.BaseUtils;
import com.intellif.core.ReflectUtils;

import javax.persistence.*;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * sql语句构造器
 */
public class TableBuider {

    private CoreConfig coreConfig;

    public TableBuider(CoreConfig coreConfig){
        this.coreConfig = coreConfig;
    }

    public void create(){
      boolean flag =  coreConfig.isCreateTable();
      if(flag){
          mysqlCreate();
          System.out.println(">>>>>>>>>>>>>>>>>>>表语句创建成功");
      }
    }

    private void mysqlCreate(){
      List<String> shortNames =   coreConfig.getClassNames();
      String domainPackge = coreConfig.getDomainPage();
      if(shortNames!=null&&shortNames.size()>0){
          //获取流文件
          String content = getResourceContent();
          StringBuilder sb  = new StringBuilder();
          sb.append("------------修改时间:"+ BaseUtils.formateDate(new Date())+"-----------------").append(PathConfig.newLine());
          for(String shortName:shortNames){
              String className = domainPackge+"."+shortName;
              try {
                  Class<?> clazz =Class.forName(className);
                 String sqlContent =  mysqlCreateTable(clazz,content);
                 sb.append(sqlContent);
              } catch (ClassNotFoundException e) {
                  e.printStackTrace();
              }
          }

          //保存流文件
          content+=sb.toString();
          saveResource(content);
      }
    }

    public void saveResource(String content){
        String path = PathConfig.getProjectPath()+"//"+coreConfig.getResourcePath()+"//"+coreConfig.getTableFile();
        BufferedWriter bw = null;
        try{
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
            bw.write(content);
            bw.flush();
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private String getResourceContent(){
        String path = PathConfig.getProjectPath()+"//"+coreConfig.getResourcePath()+"//"+coreConfig.getTableFile();
        File file = new File(path);
        if(!file.exists())
            return "";
       BufferedReader reader = null;
       try{
           StringBuilder sb = new StringBuilder();
           reader =new BufferedReader(new InputStreamReader( new FileInputStream(path)));
           String line = null;
           while((line=reader.readLine())!=null){
               sb.append(line).append(PathConfig.newLine());
           }
           return sb.toString();
       }catch (Exception e){
           throw new RuntimeException(e);
       }finally {
           if(reader!=null){
               try {
                   reader.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
    }

    private String mysqlCreateTable(Class clazz,String content){
        Annotation entity = clazz.getAnnotation(Entity.class);
        if(entity==null)
            return "";
        Table table = (Table) clazz.getAnnotation(Table.class);
        if(table==null)
            return "";
        String schemaName = table.schema();
        String tableName = table.name();
        if(content.contains(schemaName+"."+tableName)){
           return "";
        }
        if(content.contains(tableName)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if(schemaName!=null&&!schemaName.equals("")){
            sb.append("create database if not exists ").append(schemaName).append(";").append(PathConfig.newLine());
        }
        sb.append("create table if not exists ");
        if(schemaName!=null&&!schemaName.equals("")){
            sb.append(schemaName).append(".");
        }
        sb.append(tableName).append("(").append(PathConfig.newLine());
        //创建Mysql的主键
        creatMysqlId(clazz, sb);
        //设置其他字段
        String otherContent = createMysqlOther(clazz);
        sb.append(otherContent);
        sb.append(");").append(PathConfig.newLine());
        return sb.toString();
    }


    private String createMysqlOther(Class clazz){
        List<Field> fields = ReflectUtils.listAllFields(clazz);
        if(fields==null||fields.size()==0)
            return "";
        StringBuilder sb = new StringBuilder();
        for(Field field:fields){
            if(field.getAnnotation(Id.class)!=null||field.getAnnotation(Transient.class)!=null)
                continue;
            sb.append(getFieldName(field)).append(getMySqlType(field));
            sb.append(",");
            sb.append(PathConfig.newLine());
        }
        String content = sb.toString();
        content = content.substring(0,content.length()-3);
        content+=PathConfig.newLine();
        return content;
    }

    /**
     * 创建id
     * @param clazz
     * @param sb
     */
    private void creatMysqlId(Class clazz, StringBuilder sb) {
        //设置主键
        Field idField =  ReflectUtils.getIdField(clazz);
        GeneratedValue generatedValue = idField.getAnnotation(GeneratedValue.class);
        GenerationType strategy = generatedValue.strategy();
        String idName = getFieldName(idField);

        if(strategy.compareTo(GenerationType.IDENTITY)==0){
            sb.append(idName+getMySqlType(idField)).append(" primary key auto_increment,").append(PathConfig.newLine());
        }else{
            sb.append(idName).append(getMySqlType(idField)).append("primary key,").append(PathConfig.newLine());
        }
    }

    /**
     * 获取sql类型
     * @param field
     * @return
     */
    private String getMySqlType(Field field){
        Class clazz = field.getType();
        String type = " " +clazz.getSimpleName()+" ";
       if(clazz.equals(String.class)){
           type =" varchar(255) ";
       }else if(clazz.equals(Long.class)){
           type =" bigint ";
       }else if(clazz.equals(Integer.class)){
           type =" int ";
       }else if(clazz.equals(Date.class)){
           type =" datetime ";
       }else if(clazz.equals(Boolean.class)){
           type =" tinyint ";
       }else if(clazz.equals(Float.class)){
           type =" float  ";
       }else if(clazz.equals(Double.class)){
           type =" double ";
       }
       return type;
    }

    private String getFieldName(Field field){
        String name = field.getName();
       Column column =  field.getAnnotation(Column.class);
       if(column!=null)
           name = column.name();
       return name;
    }

    private void oracleCreate(){

    }
}
