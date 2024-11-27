package com.zlc.gulu.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zlc.gulu.pojo.entity.CommentEntity;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo {
    private Integer commentId;
    private Integer videoId;
    private Integer rootId;
    private String content;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer isTop;
    private Integer status;
    private String createTime;
    /* 评论者相关信息 */
    private Integer userId;
    private String userName;
    private String userAvatar;
    /* 回复对象用户信息 */
    private Integer replyId;
    private String replyName;
    /*
     *  子评论
     * */
    List<CommentVo> children;
    // 子评论的长度
    private long length;

}
