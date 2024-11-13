package com.zlc.gulu.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 投稿视频表
 * @TableName gl_video
 */
@TableName(value ="gl_video")
@Data
public class VideoEntity implements Serializable {
    /**
     * 视频ID
     */
    @TableId(type = IdType.AUTO)
    private Integer videoId;

    /**
     * 投稿用户ID
     */
    private Integer userId;

    /**
     * 视频标题(不能超过40字)
     */
    private String title;

    /**
     * 视频总时长(单位:s)
     */
    private Integer duration;

    /**
     * 视频封面url
     */
    private String coverUrl;

    /**
     * 视频url
     */
    private String videoUrl;

    /**
     * 视频类型(0:自制,1:转载)
     */
    private Integer type;

    /**
     * 视频标签
     */
    private String tags;

    /**
     * 简介(不能超过2000字)
     */
    private String description;

    /**
     * 视频状态(0:正常，1：禁用，2：审核中)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}