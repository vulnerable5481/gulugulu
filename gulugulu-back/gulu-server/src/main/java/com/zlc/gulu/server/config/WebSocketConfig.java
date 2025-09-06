package com.zlc.gulu.server.config;

import com.zlc.gulu.server.handler.VideoWSHandler;
import com.zlc.gulu.server.handler.ProcessWsHandle;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/*
 *  websocket配置类
 * */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private ProcessWsHandle processWsHandle;
    @Resource
    private VideoWSHandler countOnlineUserWSHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 其实这里可以通过Spring上下文来避免冗余的操作
        String processSsUrl = "/processWs";
        registry.addHandler(processWsHandle, processSsUrl)
                .setAllowedOriginPatterns("http://localhost:9090");
        String countOnlineUrl = "/countOnlineWs";
        registry.addHandler(countOnlineUserWSHandler, countOnlineUrl)
                .setAllowedOriginPatterns("http://localhost:9090");
    }
}
