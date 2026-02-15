package org.best.statistics.config;

import org.base.websocket.custom.EnhancedHandShakeHandler;
import org.best.statistics.gateway.ws.session.EnhancedWebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.UnaryOperator;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public EnhancedHandShakeHandler enhancedHandShakeHandler(@Autowired UnaryOperator<WebSocketSession> wsSessionWrapper) {
        UnaryOperator<WebSocketSession> customWrapper =
                session -> new EnhancedWebSocketSession(wsSessionWrapper.apply(session));
        return new EnhancedHandShakeHandler(customWrapper);
    }
}
