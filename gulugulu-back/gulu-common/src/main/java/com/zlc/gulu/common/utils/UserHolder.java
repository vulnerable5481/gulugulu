package com.zlc.gulu.common.utils;

import com.zlc.gulu.pojo.vo.UserVo;

public class UserHolder {

    public static final ThreadLocal<UserVo> tl = new ThreadLocal<>();

    public static void saveUser(UserVo userVo) {
        tl.set(userVo);
    }

    public static UserVo getUser() {
        return tl.get();
    }

    public static void rmUser(){
        tl.remove();
    }
}
