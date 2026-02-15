package org.base.websocket.session;

import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.base.websocket.thread.model.pool.ExclusivePoolGenerator;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class SinglePoolAsyncWebSocketSession extends AbstractAsyncWebSocketSession {
    public SinglePoolAsyncWebSocketSession(WebSocketSession session) {
        super(session, ExclusivePoolGenerator.next());
        log.info("[{}]session:{}, create single-thread pool:{}", TraceTagConstants.SINGLE_POOL, getId(), getName());
    }
}
