package com.zlc.gulu.common.constant;

/*
 * 没有具体划分的错误字段 【实际是懒得分类了】
 * */
public class ErrorConstant {


    // 约定 使用 201 - 300 作为无划分错误字段
    public enum ErrorEnum {
        UPLOAD_FAIL_CREATEFILE(201, "创建上传文件夹失败"),
        UPLOADS_FAIL_MERGEFILE(202, "合并分片失败");

        ErrorEnum(int code, String msg) {
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
