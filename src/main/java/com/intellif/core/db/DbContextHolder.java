package com.intellif.core.db;

public class DbContextHolder {

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();

    public enum DbType{
        MASTER,SLAVE
    }

    public static void setDbType(DbType dbType){
        contextHolder.set(dbType);
    }

    public static DbType getDbType(){
        return contextHolder.get();
    }

    public static void clearDbType(){
        contextHolder.remove();
    }

}
