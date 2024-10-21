package com.zlc.gulu.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class UserEntity implements Serializable {
    /**
     * 用户唯一标识符 (UUID)
     */
    @TableId(type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 密码加盐
     */
    private String salt;

    /**
     * 性别 (0是男，1是女,2是保密)
     */
    private Integer gender;

    /**
     * 个性签名
     */
    private String sign;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 用户头像 URL
     */
    private String avatar;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户状态 (例如：0为正常，1为禁用)
     */
    private Integer status;

    /**
     * 角色级别 (0是普通用户，1是大会员，2是年度大会员)
     */
    private Integer roleLevel;

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