package com.zlc.gulu.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 弹幕表
 * @TableName gl_danmu
 */
@TableName(value ="gl_danmu")
@Data
public class DanmuEntity implements Serializable {
    /**
     * 弹幕主键
     */
    @TableId(type = IdType.AUTO)
    private Integer danmuId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 视频ID
     */
    private Integer videoId;

    /**
     * 弹幕内容
     */
    private String content;

    /**
     * 弹幕发送时间
     */
    private Double time;

    /**
     * 1：滚动，2：顶部，3:底部
     */
    private Integer type;

    /**
     * 弹幕颜色,默认为白色
     */
    private String color;

    /**
     * 0:正常，1：被举报待审核，2：违规弹幕
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