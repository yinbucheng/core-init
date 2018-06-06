package com.intellif;

import com.intellif.core.init.CoreConfig;
import com.intellif.core.init.FacedeBuilder;

public class CoreCreateMain {
    public static void main(String[] args) {
        CoreConfig coreConfig = new CoreConfig();
        coreConfig.setCorePage("com.intellif.core");
        coreConfig.setDomainPage("com.intellif.domain");
        coreConfig.setParentPacke("com.intellif");
        coreConfig.setJavaShortPath("src/main/java");
        //指定resource目录
        coreConfig.setResourcePath("src/main/resources");
        //开启生成controller层
        coreConfig.openController();
        //开启生成创建表语句
        coreConfig.openCreateTable();
        //制定要生成表的文件名
        coreConfig.setTableFile("schema-mysql.sql");
        coreConfig.setAuthor("步程");
        FacedeBuilder facedeBuilder = new FacedeBuilder(coreConfig);
        facedeBuilder.create();
    }
}
