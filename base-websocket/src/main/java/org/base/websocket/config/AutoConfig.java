package org.base.websocket.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.base.websocket.constants.WebSocketTypeEnum;
import org.base.websocket.custom.EnhancedHandShakeHandler;
import org.base.websocket.metric.BaseWebSocketSessionCollector;
import org.base.websocket.metric.MetricCollector;
import org.base.websocket.session.BindThreadAsyncWebSocketSession;
import org.base.websocket.session.SinglePoolAsyncWebSocketSession;
import org.base.websocket.session.SingleThreadAsyncWebSocketSession;
import org.base.websocket.session.SpringAsyncWebSocketSession;
import org.base.websocket.thread.model.batch.BaseComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.UnaryOperator;

@AutoConfiguration
@EnableConfigurationProperties(WebSocketProperties.class)
public class AutoConfig {
    private final WebSocketProperties webSocketProperties;

    @Autowired
    public AutoConfig(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
        BaseComponent.init(webSocketProperties);
    }

    @Bean
    public UnaryOperator<WebSocketSession> wsSessionWrapper() {
        WebSocketTypeEnum typeEnum = WebSocketTypeEnum.fromType(webSocketProperties.getType());
        switch (typeEnum) {
            case BIND_THREAD:
                return BindThreadAsyncWebSocketSession::new;
            case SINGLE_THREAD:
                return SingleThreadAsyncWebSocketSession::new;
            case SINGLE_POOL:
                return SinglePoolAsyncWebSocketSession::new;
            case SPRING:
            default:
                return SpringAsyncWebSocketSession::new;
        }
    }

    @Bean
    @ConditionalOnMissingBean(EnhancedHandShakeHandler.class)
    public EnhancedHandShakeHandler enhancedHandShakeHandler(@Autowired UnaryOperator<WebSocketSession> wsSessionWrapper) {
        return new EnhancedHandShakeHandler(wsSessionWrapper);
    }

    @Bean
    @ConditionalOnClass(name = "io.micrometer.core.instrument.MeterRegistry")
    public BaseWebSocketSessionCollector baseWebSocketSessionCollector(@Autowired MeterRegistry meterRegistry) {
        return new MetricCollector(meterRegistry);
    }
}
