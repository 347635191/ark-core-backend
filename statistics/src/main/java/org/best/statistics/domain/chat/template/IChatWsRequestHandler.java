package org.best.statistics.domain.chat.template;

import org.best.statistics.domain.chat.entity.ChatWsRequest;
import org.best.statistics.gateway.ws.session.EnhancedWebSocketSession;
import org.springframework.web.socket.WebSocketSession;

public interface IChatWsRequestHandler {
    void onMessage(EnhancedWebSocketSession session, ChatWsRequest request);

    default void onMessage(WebSocketSession session, ChatWsRequest request) {
        if (session instanceof EnhancedWebSocketSession) {
            onMessage((EnhancedWebSocketSession) session, request);
        }
    }
}
