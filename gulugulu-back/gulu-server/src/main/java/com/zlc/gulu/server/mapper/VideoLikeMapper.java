package com.zlc.gulu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.gulu.pojo.entity.VideoLikeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 赵联城
* @description 针对表【gl_video_like(关联用户点赞表)】的数据库操作Mapper
* @createDate 2025-03-03 13:42:01
* @Entity com.zlc.gulu.generate.VideoLikeEntity
*/
@Mapper
public interface VideoLikeMapper extends BaseMapper<VideoLikeEntity> {

}




