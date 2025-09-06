-- 创建用户表
CREATE TABLE IF NOT EXISTS `gl_user`
(
    `user_id`     INT AUTO_INCREMENT COMMENT '用户唯一标识符 (UUID)',
    `user_name`   VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL COMMENT '用户密码',
    `salt`        VARCHAR(100) COMMENT '密码加盐',
    `gender`      TINYINT COMMENT '性别 (0是男，1是女,2是保密)',
    `sign`        VARCHAR(100) DEFAULT '咕噜咕噜~~~~~' COMMENT '个性签名',
    `birth_date`  DATE COMMENT '出生日期',
    `avatar`      VARCHAR(255) COMMENT '用户头像 URL',
    `mobile`      VARCHAR(50) UNIQUE COMMENT '手机号码',
    `status`      TINYINT COMMENT '用户状态 (例如：0为正常，1为禁用)',
    `role_level`  TINYINT      NOT NULL COMMENT '角色级别 (0是普通用户，1是大会员，2是年度大会员)',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='用户表';

-- 创建视频详情表
CREATE TABLE IF NOT EXISTS `gl_video`
(
    `video_id`    INT AUTO_INCREMENT COMMENT '视频ID',
    `user_id`     INT          NOT NULL COMMENT '投稿用户ID',
    `title`       VARCHAR(120) NOT NULL COMMENT '视频标题(不能超过40字)',
    `duration`    INT          NOT NULL COMMENT '视频总时长(单位:s)',
    `cover_url`   VARCHAR(255) NOT NULL COMMENT '视频封面url',
    `video_url`   VARCHAR(255) NOT NULL UNIQUE COMMENT '视频url',
    `type`        TINYINT      NOT NULL COMMENT '视频类型(0:自制,1:转载)',
    `mp_id`       INT          NOT NULL COMMENT '主分区ID',
    `sp_id`       INT          NOT NULL COMMENT '次分区ID',
    `tags`        VARCHAR(600) COMMENT '视频标签',
    `description` VARCHAR(6000) COMMENT '简介(不能超过2000字)',
    `status`      TINYINT      NOT NULL COMMENT '视频状态(0:正常，1：禁用，2：审核中)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`video_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 100
  DEFAULT CHARSET = utf8 COMMENT ='投稿视频表';

-- 创建分类表
CREATE TABLE IF NOT EXISTS `gl_catagory`
(
    `catry_id`   INT AUTO_INCREMENT COMMENT '分类主键',
    `parent_id`  INT DEFAULT 0 COMMENT '父类ID(根分类为0)',
    `tags`       VARCHAR(600) COMMENT '标签(使用逗号隔离,必须英文逗号)',
    `catry_name` VARCHAR(60) NOT NULL COMMENT '分类名称',
    PRIMARY KEY (`catry_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='分类表';

-- 创建视频数据表
CREATE TABLE IF NOT EXISTS `gl_video_detail`
(
    `id`            INT AUTO_INCREMENT COMMENT '主键',
    `video_id`      INT COMMENT '关联视频ID',
    `comment_count` INT NOT NULL COMMENT '评论数',
    `like_count`    INT NOT NULL comment '点赞数',
    `create_time`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='关联视频评论和点赞数量的表';

-- 创建评论表
CREATE TABLE IF NOT EXISTS `gl_comment`
(
    `comment_id`    INT AUTO_INCREMENT COMMENT '评论主键',
    `video_id`      INT           NOT NULL COMMENT '所属视频ID',
    `user_id`       INT           NOT NULL COMMENT '评论用户ID',
    `root_id`       INT                    DEFAULT 0 COMMENT '根节点ID(0表示根节点，其他指向根节点)',
    `user_name`     VARCHAR(300) COMMENT '回复用户名称',
    `user_avatar`   VARCHAR(255) COMMENT '回复用户头像',
    `content`       VARCHAR(1500) NOT NULL COMMENT '评论内容(最多500字)',
    `like_count`    INT                    DEFAULT 0 COMMENT '点赞数',
    `dislike_count` INT                    DEFAULT 0 COMMENT '点踩数',
    `is_top`        TINYINT                DEFAULT 0 COMMENT '是否置顶',
    `status`        TINYINT       NOT NULL DEFAULT 0 COMMENT '状态(0:正常,1:软删除，2:已删除)',
    `create_time`   DATETIME               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`comment_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='评论表';

-- 创建视频点赞表
CREATE TABLE IF NOT EXISTS `gl_user_like`
(
    `id`          INT AUTO_INCREMENT COMMENT '主键',
    `video_id`    INT NOT NULL COMMENT '视频ID',
    `user_id`     INT NOT NULL COMMENT '用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='视频点赞表';

-- 创建弹幕表
CREATE TABLE IF NOT EXISTS `gl_danmu`
(
    `danmu_id`    INT AUTO_INCREMENT COMMENT '弹幕主键',
    `user_id`     INT          NOT NULL COMMENT '用户ID',
    `video_id`    INT          NOT NULL COMMENT '视频ID',
    `content`     VARCHAR(300) NOT NULL COMMENT '弹幕内容',
    `time`        DOUBLE       NOT NULL COMMENT '弹幕发送时间',
    `type`        TINYINT      NOT NULL DEFAULT 1 COMMENT '1：滚动，2：顶部，3:底部',
    `color`       VARCHAR(30)  NOT NULL DEFAULT 'FFFFFF' COMMENT '弹幕颜色,默认为白色',
    `status`      TINYINT      NOT NULL DEFAULT 0 COMMENT '0:正常，1：被举报待审核，2：违规弹幕',
    `create_time` DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`danmu_id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='弹幕表';


























































