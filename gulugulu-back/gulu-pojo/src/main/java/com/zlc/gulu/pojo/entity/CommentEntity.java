package com.zlc.gulu.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论表
 *
 * @TableName gl_comment
 */
@TableName(value = "gl_comment")
@Data
public class CommentEntity implements Serializable {
    /**
     * 评论主键
     */
    @TableId(type = IdType.AUTO)
    private Integer commentId;

    /**
     * 所属视频ID
     */
    private Integer videoId;

    /**
     * 根节点ID(0表示根节点，其他指向根节点)
     */
    private Integer rootId;

    /**
     * 评论用户ID
     */
    private Integer userId;

    /**
     * 回复用户ID(0:回复根节点/本身就是根节点)
     */
    private Integer replyId;

    /**
     * 评论内容(最多500字)
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 点踩数
     */
    private Integer dislikeCount;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 状态(0:正常,1:软删除，2:已删除)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /*
     *  子评论
     * */
    @TableField(exist = false)
    List<CommentEntity> children;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}