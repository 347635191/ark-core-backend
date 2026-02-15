package org.base.websocket.config;

import lombok.Data;
import org.base.websocket.constants.WebSocketTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "base.websocket")
public class WebSocketProperties {
    private String type = WebSocketTypeEnum.SPRING.getType();
    private boolean killCloseConnectionImmediately = false;
    private int pushMessageQueueSize = 10000;
}
