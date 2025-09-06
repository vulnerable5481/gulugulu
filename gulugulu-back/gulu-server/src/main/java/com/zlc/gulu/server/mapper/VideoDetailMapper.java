package com.zlc.gulu.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.gulu.pojo.entity.VideoDetailEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 赵联城
* @description 针对表【gl_video_detail(关联视频评论和点赞数量的表)】的数据库操作Mapper
* @createDate 2025-03-03 15:36:13
* @Entity com.zlc.gulu.generate.VideoDetailEntity
*/
@Mapper
public interface VideoDetailMapper extends BaseMapper<VideoDetailEntity> {

}




