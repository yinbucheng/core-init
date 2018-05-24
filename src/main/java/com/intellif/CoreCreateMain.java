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
        coreConfig.setResourcePath("src/main/resources");
        coreConfig.openController();
        coreConfig.openCreateTable();
        coreConfig.setTableFile("schema-mysql.sql");
        coreConfig.setAuthor("步程");
        FacedeBuilder facedeBuilder = new FacedeBuilder(coreConfig);
        facedeBuilder.create();
    }
}
