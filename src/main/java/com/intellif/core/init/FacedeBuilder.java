package com.intellif.core.init;

import java.io.File;

public class FacedeBuilder {
    private ControllerBuilder controllerBuilder;
    private ServiceBuilder serviceBuilder ;
    private DaoBuilder daoBudiler;
    private TableBuider tableBuider;

    private CoreConfig coreConfig;

    public FacedeBuilder(CoreConfig config){
        this.coreConfig = config;
        controllerBuilder = new ControllerBuilder(config);
        serviceBuilder = new ServiceBuilder(config);
        daoBudiler = new DaoBuilder(config);
        tableBuider = new TableBuider(coreConfig);
    }

    private void loadClassName(){
       String path = coreConfig.getDomainRealPath();
        File filePath = new File(path);
        System.out.println(filePath);
        if(!filePath.exists())
            throw new RuntimeException("文件路径不存在请检测是否配置正确");
       File[] files = filePath.listFiles();
       if(files!=null){
           for(File file:files){
               if(file.isDirectory())
                   continue;
               String fileName = file.getName();
               if(fileName.endsWith(".java")){
                   String data = fileName.replace(".java","");
                   coreConfig.addClassName(data);
               }
           }
       }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>加载类名完成>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private void check(){
        if (coreConfig.getCorePage()==null){
            throw new RuntimeException("请配置核心包地方");
        }
        if(coreConfig.getParentPacke()==null&&coreConfig.getServicePage()==null&&coreConfig.getDaoPage()==null&&coreConfig.getControllerPage()==null)
            throw new RuntimeException("请配置生成dao，service，controller的包名");
        if(coreConfig.getJavaShortPath()==null){
            throw new RuntimeException("请配置java的短路径");
        }
        if(coreConfig.getDomainPage()==null){
            throw new RuntimeException("请配置java对象存放的包");
        }
        if(coreConfig.getDaoPage()==null){
            String daoPage = coreConfig.getParentPacke()+".dao";
            coreConfig.setDaoPage(daoPage);
        }
        if(coreConfig.getServicePage()==null){
            String servicePage = coreConfig.getParentPacke()+".service";
            coreConfig.setServicePage(servicePage);
        }
        if(coreConfig.getControllerPage()==null){
            String webPage = coreConfig.getParentPacke()+".web";
            coreConfig.setControllerPage(webPage);
        }
    }


    public void create(){
        check();
        loadClassName();
        if(coreConfig.isOpenDao()){
            daoBudiler.create();
        }
        if(coreConfig.isOpenService()){
            serviceBuilder.create();
        }
        if(coreConfig.isOpenController()){
            controllerBuilder.create();
        }
        if(coreConfig.isCreateTable()){
            tableBuider.create();
        }
    }
}
