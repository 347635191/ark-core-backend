package org.best.statistics.gateway.ws.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.best.statistics.constants.TraceTagConstants;
import org.best.statistics.gateway.ws.session.EnhancedWebSocketSession;
import org.best.statistics.gateway.ws.template.WsSessionContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ChatWsSessionContainer implements WsSessionContainer {
    private final Set<EnhancedWebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void checkIn(EnhancedWebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void checkOut(WebSocketSession session) {
        sessions.removeIf(session::equals);
    }

    @Override
    public void checkOut() {
        sessions.forEach(session -> {
            try {
                session.close();
            } catch (Exception e) {
                log.warn("[{}]session close failed", TraceTagConstants.WEB_SOCKET, e);
            }
        });
        sessions.clear();
    }

    @Override
    public Set<EnhancedWebSocketSession> getSessions() {
        return sessions;
    }
}
