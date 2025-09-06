package com.zlc.gulu.server.chain;

import com.zlc.gulu.pojo.vo.UserLoginVo;

public abstract class Handler {

    protected Handler handler;

    public void setNext(Handler handler){
        this.handler = handler;
    }

    /*
    * 处理过程
    * 子类进行实现
    * */
    public abstract void process(UserLoginVo userLoginVo);
}
