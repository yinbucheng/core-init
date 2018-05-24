package com.intellif.core;

import java.io.Serializable;

public  class ServerResult implements Serializable {
    private int code;
    private Object msg;
    private ServerResult(int code,Object msg){
         this.code=code;
         this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public static ServerResult success(Object msg){
        return new ServerResult(200,msg);
    }

    public static ServerResult success(){
        return success("操作成功");
    }

    public static ServerResult fail(Object msg){
        return new ServerResult(500,msg);
    }

    public static ServerResult fail(){
        return fail("操作失败");
    }
}
