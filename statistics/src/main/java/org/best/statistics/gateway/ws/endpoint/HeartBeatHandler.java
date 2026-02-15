package org.best.statistics.gateway.ws.endpoint;

import org.best.statistics.gateway.ws.constants.EventType;
import org.best.statistics.gateway.ws.facade.model.WsResponse;

public class HeartBeatHandler {
    public static final WsResponse<?> RESPONSE;

    static {
        RESPONSE = WsResponse.builder().messageType(EventType.HEART_BEAT).build();
    }
}
