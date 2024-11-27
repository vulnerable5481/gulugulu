package com.zlc.gulu.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.CommentEntity;
import com.zlc.gulu.pojo.vo.CommentSendVo;

/**
 * @author 赵联城
 * @description 针对表【gl_comment(评论表)】的数据库操作Service
 * @createDate 2024-11-19 18:32:00
 */
public interface CommentService extends IService<CommentEntity> {

    Result likeComment(Integer commentId);

    Result getCommentList(Integer videoId,int offset,int type);

    Result sendComment(CommentSendVo commentSendVo);
}
