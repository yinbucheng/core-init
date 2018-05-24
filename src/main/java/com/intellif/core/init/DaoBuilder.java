package com.intellif.core.init;

import java.io.File;
import java.util.List;

public class DaoBuilder {
    private CoreConfig coreConfig;
    private String daoPage;

    public DaoBuilder(CoreConfig config){
        coreConfig = config;
        if(coreConfig.getDaoPage()!=null){
            this.daoPage = coreConfig.getDaoPage();
        }else{
            daoPage = coreConfig.getParentPacke()+".dao";
        }
    }

    public void create(){
        List<String> classNames = coreConfig.lisAllClassName();
        if(classNames!=null){
            for(String className:classNames){
               String daoPath =  coreConfig.getDaoRealPath();
               String daoName = className+"Dao";
               File path = new File(daoPath);
               File path2 = new File(daoPath+"//impl");
               if(!path.exists()) {
                   path.mkdirs();
               }
               if(!path2.exists()){
                   path2.mkdirs();
               }
               File file = new File(daoPath+"//"+daoName+".java");
               if(file.exists())
                   continue;
               StringBuilder sb = new StringBuilder("package "+daoPage+";").append(PathConfig.newLine())//
                       .append("import "+coreConfig.getCorePage()+".*;").append(PathConfig.newLine())//
                       .append("import "+coreConfig.getDomainPage()+"."+className+";").append(PathConfig.newLine())//
                       .append(coreConfig.getAuthor())//
                       .append("public interface "+daoName+" extends BaseDao<"+className+">{").append(PathConfig.newLine())//
                       .append("}");// ;
                PathConfig.copyToFile(file,sb.toString());

                //添加dao中内容
                String daoImplPath = daoPath+"//impl";
                String daoImplName = className+"DaoImpl";
                file = new File(daoImplPath+"//"+daoImplName+".java");
               sb = new StringBuilder("package "+daoPage+".impl;").append(PathConfig.newLine())//
                .append("import "+coreConfig.getCorePage()+".*;").append(PathConfig.newLine())//
                .append("import "+coreConfig.getDomainPage()+"."+className+";").append(PathConfig.newLine())//
                .append("import org.springframework.stereotype.Repository;").append(PathConfig.newLine())//
                 .append("import "+daoPage+"."+daoName+";")    .append(PathConfig.newLine())//
                 .append(coreConfig.getAuthor())//
                .append("@Repository").append(PathConfig.newLine())//
                .append("public class "+daoImplName+" extends BaseDaoImpl<"+className+"> implements "+daoName+"{").append(PathConfig.newLine())//
                .append("}");
               PathConfig.copyToFile(file,sb.toString());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>生成 "+daoImplName+" 完成");
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>dao层代码生成完成>>>>>>>>>>>>>>>>>>>");
    }
}
