package org.base.websocket.session;

import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.base.websocket.thread.model.multiplex.io.EventLoopGroup;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class BindThreadAsyncWebSocketSession extends AbstractAsyncWebSocketSession {
    public BindThreadAsyncWebSocketSession(WebSocketSession session) {
        super(session, EventLoopGroup.next());
        log.info("[{}]session:{}, bind io-thread:{}", TraceTagConstants.BIND_THREAD, getId(), getName());
    }
}
