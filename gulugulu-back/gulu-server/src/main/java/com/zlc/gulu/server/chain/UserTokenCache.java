package com.zlc.gulu.server.chain;

import com.zlc.gulu.pojo.vo.UserLoginVo;

public class UserTokenCache extends Handler{
    @Override
    public void process(UserLoginVo userLoginVo) {
        // 将用户信息和Token保存到redis

        handler.process(userLoginVo);
    }
}
