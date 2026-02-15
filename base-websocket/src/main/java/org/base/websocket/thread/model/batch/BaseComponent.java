package org.base.websocket.thread.model.batch;

import org.base.websocket.config.WebSocketProperties;

import java.util.Objects;
import java.util.Optional;

public abstract class BaseComponent {
    private static WebSocketProperties webSocketProperties;

    public static void init(WebSocketProperties webSocketProperties) {
        BaseComponent.webSocketProperties = webSocketProperties;
    }

    protected boolean isKillCloseConnectionImmediately() {
        return Objects.nonNull(webSocketProperties) &&
                webSocketProperties.isKillCloseConnectionImmediately();
    }

    protected int getPushMessageQueueSize() {
        return Optional.ofNullable(webSocketProperties).map(WebSocketProperties::getPushMessageQueueSize).orElse(1000);
    }
}
