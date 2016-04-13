package com.athena.meerkat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.athena.meerkat.controller.web.provisioning.log.LogWebSocketHandler;
import com.athena.meerkat.controller.web.provisioning.log.TestWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
	@Autowired
	private LogWebSocketHandler logWebSocketHandler;

	@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(logWebSocketHandler, "/provi/taillog");
        registry.addHandler(testWebsocketHandler(), "/provi/test");
    }

    @Bean
    public WebSocketHandler testWebsocketHandler() {
        return new TestWebSocketHandler();
    }
    

}