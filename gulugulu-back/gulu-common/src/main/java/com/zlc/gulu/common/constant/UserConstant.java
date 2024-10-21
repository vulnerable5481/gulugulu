package com.zlc.gulu.common.constant;

public class UserConstant {
    //用户默认头像
    public static final String USER_DEFAULT_AVATAR = "https://gulugulu-zlc.oss-cn-hangzhou.aliyuncs.com/avatar/avatar3.jpg";


    /*
  用户相关的错误统一返回错误码 10X
     */
    public enum UserLoginEnum {
        USER_ALREADY_EXIST(101, "用户名已存在"),
        USER_REGISTER_NAME_IS_NULL(102, "用户名不能为空"),
        USER_REGISTER_SECRET_IS_NULL(102, "密码不能为空"),
        USER_REGISTER_SECRET_NOT_SAME(102, "前后密码不一致"),
        USER_LOGIN_NAME_WRONG(103, "用户名错误"),
        USER_LOGIN_USER_IS_NULL(103, "该用户不存在！请前往注册！"),
        USER_LOGIN_SECRET_WRONG(103, "密码错误");

        private int code;
        private String msg;

        UserLoginEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
