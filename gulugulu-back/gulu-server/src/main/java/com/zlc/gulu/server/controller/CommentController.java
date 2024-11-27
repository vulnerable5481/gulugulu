package com.zlc.gulu.server.controller;

import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.vo.CommentSendVo;
import com.zlc.gulu.server.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 获取评论列表,每次获取十条根评论
     *
     * @param offset 分页偏移量 (已经获取的根评论数)
     * @param type   0:热度排行；1：最新排行
     */
    @GetMapping("/tree")
    public Result getCommentList(@RequestParam("videoId") Integer videoId, @RequestParam("offset") int offset
            , @RequestParam("type") int type) {
        return commentService.getCommentList(videoId, offset, type);
    }

    /**
     * 发送单个评论,最大长度为2000字以内
     *
     * @Param videoId 视频ID
     * @Param content 评论内容
     */
    @PostMapping("/send")
    public Result sendComment(@RequestBody CommentSendVo commentSendVo) {
        return commentService.sendComment(commentSendVo);
    }

    /*
     *  点赞评论
     * */
    @PostMapping("/like")
    public Result likeComment(@RequestParam("commentId") Integer commentId) {
        return commentService.likeComment(commentId);
    }
}
