package com.intellif.interceptor;

public abstract class Hander {
    Hander hander = null;
    public abstract  Object procced(Object target);

    /**
     * 链式调用方法
     * @param target 代理的对
     * @return
     */
    public Object hander(Object target){
        Object proxy = procced(target);
        if(hander!=null){
            proxy = hander.procced(proxy);
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
