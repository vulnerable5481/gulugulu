package com.zlc.gulu.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ProcessWsHandle implements WebSocketHandler {

    // 用于存储每个 WebSocket 会话对应的 session
    private static final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String id = session.getId();
        sessionMap.put(id, session);
        session.sendMessage(new TextMessage(id));
        log.info("视频上传进度条ws开始:" + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("视频上传进度条ws发生错误");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessionMap.remove(session.getId());
        log.info("视频上传进度条ws关闭");
    }

    // 支持分片消息
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // 推送进度条进度
    public void sendProcess(int id, int total, String sessionId) throws IOException {
        double res = (double) id / (double) total * 100;
        int progress = (int) Math.round(res);
        WebSocketSession session = sessionMap.get(sessionId);
        session.sendMessage(new TextMessage(progress + ""));
    }
}
