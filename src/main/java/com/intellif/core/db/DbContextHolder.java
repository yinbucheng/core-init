package com.intellif.core.db;

public class DbContextHolder {

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();

    public enum DbType{
        MASTER("master"),SLAVE("slave");
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        DbType(String value) {
            this.value = value;
        }
    }

    public static void setDbType(DbType dbType){
        contextHolder.set(dbType);
    }

    public static DbType getDbType(){
        DbType dbType = contextHolder.get();
        if(dbType==null)
            dbType = DbType.MASTER;
        return dbType;
    }

    public static void clearDbType(){
        contextHolder.remove();
    }

}
