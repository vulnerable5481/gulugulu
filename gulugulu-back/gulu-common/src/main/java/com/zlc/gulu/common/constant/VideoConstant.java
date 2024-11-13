package com.zlc.gulu.common.constant;

public class VideoConstant {
    public static final int VIDEO_STATUS_SUCCESS = 0; // 视频正常
    public static final int VIDEO_STATUS_FAIL = 1; // 禁用视频
    public static final int VIDEO_STATUS_REVIEW = 2; // 视频审核中

    public enum VideoEnum {
        VIDEO_UPLOAD_COVER_FAIL(404, "上传视频封面失败");

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
