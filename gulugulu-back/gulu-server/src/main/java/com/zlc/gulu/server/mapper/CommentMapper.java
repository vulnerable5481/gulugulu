package com.zlc.gulu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.gulu.pojo.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 赵联城
 * @description 针对表【gl_comment(评论表)】的数据库操作Mapper
 * @createDate 2024-11-19 18:32:00
 * @Entity com.zlc.gulu.generate.CommentEntity
 */
@Mapper
public interface CommentMapper extends BaseMapper<CommentEntity> {

    List<CommentEntity> listTenRootsOrderByTime(@Param("videoId") Integer videoId, @Param("rootId") int rootId,
                                                @Param("status") int status, @Param("offset") int offset,
                                                @Param("number") int number);

    List<CommentEntity> listTenRootsOrderByLikeCount(@Param("videoId") Integer videoId, @Param("rootId") int rootId,
                                                     @Param("status") int status, @Param("offset") int offset,
                                                     @Param("number") int number);
}




