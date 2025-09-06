package com.zlc.gulu.server.handler;

import com.zlc.gulu.common.utils.GuluUtils;
import com.zlc.gulu.common.utils.UserHolder;
import com.zlc.gulu.pojo.entity.DanmuEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class VideoWSHandler implements WebSocketHandler {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    // 存储ws对话
    private static final Map<String, WebSocketSession> sessionMap = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 保存会话
        String id = session.getId();
        sessionMap.put(id, session);
        // 从url中获取数据  假如是这样的  const ws = new WebSocket('ws://localhost:9090/countOnlineWs?vid=12345');
        URI uri = session.getUri();
        int vid = Integer.parseInt(uri.getQuery().split("=")[1]);
        // 实时在线人数+1
        String key = "online:" + vid;
        stringRedisTemplate.opsForSet().add(key, UserHolder.getUser().getUserId() + "");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 心跳检测，返回当前实时在线人数
        URI uri = session.getUri();
        int vid = Integer.parseInt(uri.getQuery().split("=")[1]);
        String key = "online:" + vid;
        Long size = stringRedisTemplate.opsForSet().size(key);
        session.sendMessage(new TextMessage(size.toString()));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // 删除会话
        sessionMap.remove(session.getId());
        // 实时在线人数-1
        URI uri = session.getUri();
        int vid = Integer.parseInt(uri.getQuery().split("=")[1]);
        String key = "online:" + vid;
        stringRedisTemplate.opsForSet().remove(key);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    public void promote(DanmuEntity danmu) {
        // 推流，将弹幕推给其他客户端
        sessionMap.forEach((id, webSocketSession) -> {
            try {
                webSocketSession.sendMessage(new TextMessage(danmu.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // 异步更新缓存
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                String key = danmu.getVideoId() + ":danmu:"; // 待定义
                String json = GuluUtils.beanToJson(danmu);
                // 根据时间戳排序，便于添加到动态数组
                stringRedisTemplate.opsForZSet().add(key, json, danmu.getTime());
            }
        });
    }
}
