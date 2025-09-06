package com.zlc.gulu.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.gulu.pojo.entity.VideoDetailEntity;
import com.zlc.gulu.server.mapper.VideoDetailMapper;
import com.zlc.gulu.server.service.VideoDetailService;
import org.springframework.stereotype.Service;

/**
* @author 赵联城
* @description 针对表【gl_video_detail(关联视频评论和点赞数量的表)】的数据库操作Service实现
* @createDate 2025-03-03 15:36:13
*/
@Service
public class VideoDetailServiceImpl extends ServiceImpl<VideoDetailMapper, VideoDetailEntity>
    implements VideoDetailService {

}




