package com.intellif.core.init;

import java.io.*;

public abstract class PathConfig {

    //获取当前项目路径 如G:\spring-boot-first\spring-boot-first
    public static String getProjectPath(){
       String path = System.getProperty("user.dir");
       return path.replace("\\","//");
    }

   //换行
   public static String newLine(){
        return System.getProperty("line.separator");
   }

   public static void copyToFile(File file, String content){
       BufferedWriter bw = null;
       try {
           bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
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
}
