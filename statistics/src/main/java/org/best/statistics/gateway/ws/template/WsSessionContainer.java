package org.best.statistics.gateway.ws.template;

import org.best.statistics.gateway.ws.session.EnhancedWebSocketSession;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

public interface WsSessionContainer {
    void checkIn(EnhancedWebSocketSession session);

    void checkOut(WebSocketSession session);

    void checkOut();

    Set<EnhancedWebSocketSession> getSessions();
}
