package com.zlc.gulu.common.constant;

import java.util.concurrent.TimeUnit;


/*
 *  评论相关错误统一返回50x
 * */
public class CommentConstant {
    public static final String COMMENT_VIDEO_HOT = "comment:video:hot:";
    public static final String COMMENT_VIDEO_NEWEST = "comment:video:newest:";
    public static final String COMMENT_REPLY = "comment:reply:";

    public static final long COMMENT_VIDEO_EXPIRE = 60 * 60;
    public static final TimeUnit COMMENT_VIDEO_TIMEUNIT = TimeUnit.SECONDS;

    public enum CommentNum {
        COMMENT_LOAD_FINISHED(501, "评论已全部加载");

        CommentNum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        private int code;
        private String msg;

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
