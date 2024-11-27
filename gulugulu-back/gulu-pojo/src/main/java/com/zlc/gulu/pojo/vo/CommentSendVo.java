package com.zlc.gulu.pojo.vo;

import lombok.Data;

/*
 *  前端发送的评论
 * */
@Data
public class CommentSendVo {
    private Integer videoId;
    private Integer rootId;
    private Integer userId;
    private Integer replyId;
    private String content;
}
