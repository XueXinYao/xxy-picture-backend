package com.xxy.xxypicturebackend.manager.websocket;

import com.xxy.xxypicturebackend.manager.websocket.model.PictureEditHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;
import java.net.http.WebSocket;

/**
 * websocket 配置(定义链接)
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Resource
    private PictureEditHandler pictureEditHandler;
    @Resource
    private  WsHandshakeInterceptor wsHandshakeInterceptor;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
            registry.addHandler(pictureEditHandler, "/ws/picture/edit")
                    .addInterceptors(wsHandshakeInterceptor)
                    .setAllowedOrigins("*");
    }
}
