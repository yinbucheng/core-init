package com.intellif.core.init;

import java.io.File;
import java.util.List;

public class ServiceBuilder {
    private CoreConfig coreConfig;
    private String servicePage;

    public ServiceBuilder(CoreConfig config){
        coreConfig = config;
        if(config.getServicePage()!=null){
            servicePage = config.getServicePage();
        }else{
            servicePage = config.getParentPacke()+".service";
        }
    }

    public void create(){
        List<String> classNames = coreConfig.lisAllClassName();
        if(classNames!=null){
            for(String className:classNames){
                String servicePath =  coreConfig.getServiceRealPath();
                String serviceName = "I"+className+"Service";
                File path = new File(servicePath);
                File path2 =null;
                if(coreConfig.getServicePath()==null) {
                    path2 = new File(servicePath + "//impl");
                }else{
                    path2 = new File(coreConfig.getServiecImplRealPath());
                }
                if(!path.exists()) {
                    path.mkdirs();
                }
                if(!path2.exists()){
                    path2.mkdirs();
                }
                File file = new File(servicePath+"//"+serviceName+".java");
                if(file.exists())
                    continue;
                StringBuilder sb = new StringBuilder("package "+servicePage+";").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getCorePage()+".*;").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getDomainPage()+"."+className+";").append(PathConfig.newLine())//
                        .append(coreConfig.getAuthor())//
                        .append("public interface "+serviceName+" extends IService<"+className+">{").append(PathConfig.newLine())//
                        .append("}");// ;
                PathConfig.copyToFile(file,sb.toString());


                String serviceImplPath = servicePath+"//impl";
                String serviceImplName = className+"ServiceImpl";
                file = new File(serviceImplPath+"//"+serviceImplName+".java");
                sb = new StringBuilder("package "+servicePage+".impl;").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getCorePage()+".*;").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getDomainPage()+"."+className+";").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getDaoPage()+"."+className+"Dao;").append(PathConfig.newLine())//
                        .append("import org.springframework.stereotype.Service;").append(PathConfig.newLine())//
                        .append("import "+servicePage+"."+serviceName+";").append(PathConfig.newLine())//
                        .append(coreConfig.getAuthor())//
                        .append("@Service").append(PathConfig.newLine())//
                        .append("public class "+serviceImplName+" extends ServiceImpl<"+className+"Dao,"+className+"> implements "+serviceName+"{").append(PathConfig.newLine())//
                        .append("}");
                PathConfig.copyToFile(file,sb.toString());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>生成 "+serviceImplName+" 完成");
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>service层代码生成完成>>>>>>>>>>>>>>>>>>>");
    }

}
