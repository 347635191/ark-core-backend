package org.best.statistics.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.base.websocket.custom.EnhancedHandShakeHandler;
import org.best.statistics.constants.TraceTagConstants;
import org.best.statistics.gateway.ws.endpoint.ChatWebSocketHandler;
import org.best.statistics.gateway.ws.interceptor.ChatWebSocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Slf4j
@EnableWebSocket
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final EnhancedHandShakeHandler enhancedHandShakeHandler;
    private final ChatWebSocketInterceptor chatWebSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/statistics/chatWs")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(enhancedHandShakeHandler)
                .addInterceptors(chatWebSocketInterceptor);
        log.info("[{}]handler registered", TraceTagConstants.WEB_SOCKET);
    }
}
