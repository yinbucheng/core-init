package com.intellif.utils;

import java.io.*;

public class FileUtils {

    /**
     * 数据保存
     * @param writer
     * @param reader
     */
    public static void saveText(BufferedReader reader,PrintWriter writer){
        try{
            String line = null;
            while((line = reader.readLine())!=null){
                writer.println(line);
            }
            writer.flush();
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if(writer!=null){
                writer.close();
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveText(InputStream in,OutputStream os){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(os,"utf-8"));
            saveText(reader,writer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
