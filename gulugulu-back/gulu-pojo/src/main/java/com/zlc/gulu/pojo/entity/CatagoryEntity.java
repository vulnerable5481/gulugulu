package com.zlc.gulu.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分类表
 *
 * @TableName gl_catagory
 */
@TableName(value = "gl_catagory")
@Data
public class CatagoryEntity implements Serializable {
    /**
     * 分类主键
     */
    @TableId(type = IdType.AUTO)
    private Integer catryId;

    /**
     * 父类ID(根分类为0)
     */
    private Integer parentId;

    /**
     * 标签(使用逗号隔离)
     */
    private String tags;

    /**
     * 分类名称
     */
    private String catryName;

    /**
     * 子分类
     */
    @TableField(exist = false)
    private List<CatagoryEntity> children;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}