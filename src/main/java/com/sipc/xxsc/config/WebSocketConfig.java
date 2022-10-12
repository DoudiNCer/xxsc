package com.sipc.xxsc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfig{
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        // 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解声明的 websocket endpoint
        return new ServerEndpointExporter();
    }
}
