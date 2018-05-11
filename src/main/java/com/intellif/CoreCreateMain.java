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
        coreConfig.setOpenController(true);
        coreConfig.setAuthor("步程");
        FacedeBuilder facedeBuilder = new FacedeBuilder(coreConfig);
        facedeBuilder.create();
    }
}
