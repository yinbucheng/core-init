# core-init
## 它能简化什么？
它能让你只需编写实体对象也就domain下面添加想要的对象。自动生成dao,service,web层
并且它还能够根据编写的domain生成初始化表的sql语句

## 生成初始化语句注意什么
你的对象只需要满足jpa注解规范就可以了,比如@Entity @Table @Column @Transient

## 如何只需配置生成

CoreConfig coreConfig = new CoreConfig();

   coreConfig.setCorePage("com.intellif.core");
   
   coreConfig.setDomainPage("com.intellif.domain");
   
   coreConfig.setParentPacke("com.intellif");
   
   coreConfig.setJavaShortPath("src/main/java");
   
   coreConfig.setResourcePath("src/main/resources");
   
   coreConfig.setOpenController(true);
   
   coreConfig.openCreateTable();
   
   coreConfig.setTableFile("schema-mysql.sql");
   
   coreConfig.setAuthor("步程");
   
   FacedeBuilder facedeBuilder = new FacedeBuilder(coreConfig);
   
   facedeBuilder.create();
   
   
上面配置就能生成dao，service，web和创建sql语句

## 如何使用
1.修改成自己的数据的配置

3.创建你想要的实体对象

4.运行CoreCreateMain

5.在web层编写想要的逻辑

6.运行CoreInitApplication启动spring-boot

