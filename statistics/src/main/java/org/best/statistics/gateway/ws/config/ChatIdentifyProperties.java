package org.best.statistics.gateway.ws.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "chat-identify")
public class ChatIdentifyProperties {
    private List<String> calSpeed = new ArrayList<>();
    /**
     * 行动值区间 [6-120]
     */
    private String activeValue;
    /**
     * 乱速区间 [0-5]
     */
    private String messValue;
    /**
     * 己方速度 [121-300]
     */
    private String speed;
}
