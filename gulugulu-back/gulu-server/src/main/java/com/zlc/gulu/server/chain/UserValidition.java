package com.zlc.gulu.server.chain;

import com.zlc.gulu.pojo.vo.UserLoginVo;

public class UserValidition extends Handler{
    @Override
    public void process(UserLoginVo userLoginVo) {
        // 验证用户信息

        // 最后还要调用
        handler.process(userLoginVo);
    }
}
