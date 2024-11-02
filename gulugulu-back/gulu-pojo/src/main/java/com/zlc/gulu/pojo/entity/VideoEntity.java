package com.zlc.gulu.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName video
 */
@TableName(value ="video")
@Data
public class VideoEntity implements Serializable {
    /**
     * 视频ID
     */
    @TableId(type = IdType.AUTO)
    private Integer videoId;

    /**
     * 视频作者
     */
    private Integer userId;

    /**
     * 视频hash标识符
     */
    private String hashValue;

    /**
     * url地址
     */
    private String url;

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