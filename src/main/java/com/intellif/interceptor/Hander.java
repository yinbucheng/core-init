package com.intellif.interceptor;

public abstract class Hander {
    Hander hander = null;
    public abstract  Object procced(Object target,Object sourceObject);

    /**
     * 链式调用方法
     * @param target 代理的对象
     * @param sourceObject 原始的对象
     * @return
     */
    public Object hander(Object target,Object sourceObject){
        Object proxy = procced(target,sourceObject);
        if(hander!=null){
            proxy = hander.procced(proxy,sourceObject);
        }
        return proxy;
    }

    public Hander getHander() {
        return hander;
    }

    public void setHander(Hander hander) {
        this.hander = hander;
    }
}
