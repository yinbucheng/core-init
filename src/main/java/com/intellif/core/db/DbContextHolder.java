package com.intellif.core.db;

import java.util.*;

/**
 * 确定使用那个数据库
 */
public class DbContextHolder {
    //10分钟,失败后10分钟后重试
    public static int TRYTIME = 10;

    private static String defaultSource ="master";
    //存放当前线程使用的sourceName
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
   //存放slave数据源
    private static List<Defination> slaveSource  = new LinkedList<>();
   //存放master数据源
    private static List<Defination> masterSource = new LinkedList<>();

    /**
     * 初始化slave和master
     * @param data
     */
    public static void initSource(Map<Object,Object> data){
        if(data==null||data.size()==0){
            return;
        }
      Set<Object> keys = data.keySet();
        for(Object temp:keys){
            String key = (String) temp;
            if(key.startsWith("slave")||key.startsWith("SLAVE"))
               slaveSource.add(new Defination(null,-1,key));
            else
                masterSource.add(new Defination(null,-1,key));
        }
    }

    /**
     * 使用有效的slave连接
     */
    public static void slave(){
        if(slaveSource.size()==0)
            throw new RuntimeException("slave中连接池没有建立 请确定您的source命名是slave开通");
        contextHolder.set(getAvalibeSlave());
    }

    /**
     * 获取可用的slave数据库池
     * @return
     */
    private static String getAvalibeSlave(){
        int len = slaveSource.size();
        int count = 0;
        while(true){
            int index = new Random().nextInt(len);
           Defination defination = slaveSource.get(index);
           if(defination.outTime==-1)
               return defination.sourceName;
           long time = System.currentTimeMillis();
           if(defination.lastTime+defination.outTime*1000*60<time){
               return defination.sourceName;
           }
           if(count>20)
               throw new RuntimeException("slave 中无可用资源");
           count++;
        }
    }

    /**
     *使用有效的master
     */
    public static void master(){
        if(masterSource.size()==0)
            throw new RuntimeException("slave中连接池没有建立 请确定您的source命名是master开通");
        contextHolder.set(getAvalibeMaster());
    }


    /**
     * 获取可用的master数据库池
     * @return
     */
    private static String getAvalibeMaster(){
        int len = slaveSource.size();
        int count = 0;
        while(true){
            int index = new Random().nextInt(len);
            Defination defination = masterSource.get(index);
            if(defination.outTime==-1)
                return defination.sourceName;
            long time = System.currentTimeMillis();
            if(defination.lastTime+defination.outTime*1000*60<time){
                return defination.sourceName;
            }
            if(count>20)
                throw new RuntimeException("slave 中无可用资源");
            count++;
        }
    }


    public static String getSource(){
        String source = contextHolder.get();
        if(source==null)
            source =defaultSource ;
        return source;
    }

    public static void clear(){
        contextHolder.remove();
    }

    public static class Defination{
        //上次访问时间
       volatile  private Long lastTime;
        //过期时间
       volatile  private Integer outTime;
        //sourceName
        private String sourceName;

        public Defination(Long lastTime, Integer outTime, String sourceName) {
            this.lastTime = lastTime;
            this.outTime = outTime;
            this.sourceName = sourceName;
        }
    }


    public static void fail(String sourceName){
        for(Defination temp:slaveSource){
            if(temp.sourceName.equals(sourceName)){
                temp.outTime = TRYTIME;
                temp.lastTime = System.currentTimeMillis();
                break;
            }
        }

        for(Defination temp:masterSource){
            if(temp.sourceName.equals(sourceName)){
                temp.outTime = TRYTIME;
                temp.lastTime = System.currentTimeMillis();
                break;
            }
        }
    }

    public static void success(String sourceName){
        for(Defination temp:slaveSource){
            if(temp.sourceName.equals(sourceName)){
                temp.outTime = -1;
                temp.lastTime = null;
                break;
            }
        }

        for(Defination temp:masterSource){
            if(temp.sourceName.equals(sourceName)){
                temp.outTime = -1;
                temp.lastTime = null;
                break;
            }
        }
    }

}
