package com.zlc.gulu.common.constant;

public class VideoConstant {
    public static final int VIDEO_STATUS_SUCCESS = 0; // 视频正常
    public static final int VIDEO_STATUS_FAIL = 1; // 禁用视频
    public static final int VIDEO_STATUS_REVIEW = 2; // 视频审核中

    public enum VideoEnum {
        /* 懒死我了，直接统一返回404错误码得了 */
        VIDEO_UPLOAD_COVER_FAIL(404, "上传视频封面失败"),
        VIDEO_QUERY_NULL(404, "视频已不存在"),
        VIDEO_QUERY_DISABLED(404, "视频涉嫌违规，已被下架!");

        VideoEnum(int code, String msg) {
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
