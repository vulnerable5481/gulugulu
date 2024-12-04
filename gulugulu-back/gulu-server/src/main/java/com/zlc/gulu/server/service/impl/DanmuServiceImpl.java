package com.zlc.gulu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.DanmuEntity;
import com.zlc.gulu.server.mapper.DanmuMapper;
import com.zlc.gulu.server.service.DanmuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author 赵联城
 * @description 针对表【gl_danmu(弹幕表)】的数据库操作Service实现
 * @createDate 2024-12-01 18:35:25
 */
@Service
public class DanmuServiceImpl extends ServiceImpl<DanmuMapper, DanmuEntity>
        implements DanmuService {

    /*
     *  发送弹幕
     * */
    @Override
    public Result sendDanmu(DanmuEntity danmu) {

        // todo: 暂时先直接存储到mysql ，后续的思路还需要加入到ES
        // 异步存储
        CompletableFuture.runAsync(() -> {
            // 保存Mysql一份
            this.save(danmu);

            // 保存到ES一份
        });

        return Result.success(); // 就算弹幕发送失败，也无需返回错误
    }

    /*
     *  获取弹幕列表
     * */
    @Override
    public Result listDanmu(Integer videoId) {
        // TODO: 就先这么简单的处理吧，直接简单返回列表，后续再利用ES和REDIS做优化
        List<DanmuEntity> list = this.list(
                new LambdaQueryWrapper<DanmuEntity>().eq(DanmuEntity::getVideoId, videoId).orderByAsc(DanmuEntity::getTime)
        );
        return Result.success(list);
    }
}




