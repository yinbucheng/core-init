package com.intellif.utils;

import java.io.*;

/**
 * MySQL数据库备份
 */
public class DbBackUtils {

    /**
     * 备份数据库
     * @param ip 数据库ip地址
     * @param port 端口
     * @param userName 用户名
     * @param password 密码
     * @param savePath 保存路径
     * @param fileName 文件名
     * @param dataBaseName 库名
     * @return
     */
    public static boolean dbBackup(String ip,int port,String userName,String password,String savePath,String fileName,String dataBaseName){
        File saveFile = new File(savePath);
        if(!saveFile.exists()){
            saveFile.mkdirs();
        }
        if(!savePath.endsWith(File.separator)){
            savePath = savePath+File.separator;
        }
        try {
           OutputStream os = new FileOutputStream(savePath + fileName);
            Process process = Runtime.getRuntime().exec("mysqldump -h " + ip + " -P "
                    + port + " -u " + userName + " -p" + password + " --set-charset=UTF8 " + dataBaseName);
           InputStream in = process.getInputStream();
           FileUtils.saveText(in,os);
           if(process.waitFor() == 0){//0 表示线程正常终止。
               return true;
           }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 执行sql文件
     *
     * @param savePath
     * @throws Exception
     */
    public static boolean dbRecover(String dbHost, int dbPort, String dbUser,
                                 String dbPass, String dbName, String savePath){
        try {
            // 获取操作数据库的相关属性
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mysql -h" + dbHost + " -P " + dbPort
                    + " -u " + dbUser + " -p" + dbPass
                    + " --default-character-set=utf8 " + dbName);
            OutputStream os = process.getOutputStream();
            InputStream in = new FileInputStream(savePath);
            FileUtils.saveText(in,os);
            if(process.waitFor() == 0){//0 表示线程正常终止。
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 备份数据库
     * @param ip 数据库ip地址
     * @param port 端口
     * @param userName 用户名
     * @param password 密码
     * @param savePath 保存路径
     * @param fileName 文件名
     * @param dataBaseName 库名
     * @return
     */
    public static boolean tableBackup(String ip,int port,String userName,String password,String savePath,String fileName,String dataBaseName,String tableName){
        File saveFile = new File(savePath);
        if(!saveFile.exists()){
            saveFile.mkdirs();
        }
        if(!savePath.endsWith(File.separator)){
            savePath = savePath+File.separator;
        }
        try{
           OutputStream os = new FileOutputStream(savePath + fileName);
            Process process = Runtime.getRuntime().exec("mysqldump -h " + ip + " -P "
                    + port + " -u " + userName + " -p" + password + " --set-charset=UTF8 " + dataBaseName
                    + " " + tableName);
            InputStream in = process.getInputStream();
            FileUtils.saveText(in,os);
            if(process.waitFor() == 0){//0 表示线程正常终止。
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
//        if(dbBackup("39.108.230.81",3306,"root","123456","D://backUP","test.sql","teacher_db")){
//            System.out.println("备份成功");
//        }else{
//            System.out.println("备份失败");
//        }
        if(dbRecover("39.108.230.81",3306,"root","123456","teacher_db","D://backUP//test.sql"))
            System.out.println("导入成功");
        else
            System.out.println("导入失败");
    }
}
