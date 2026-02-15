package org.base.websocket.session;

import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.base.websocket.thread.model.api.NoUseAssistant;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class SpringAsyncWebSocketSession extends AbstractAsyncWebSocketSession {
    public SpringAsyncWebSocketSession(WebSocketSession session) {
        super(session, NoUseAssistant.instance());
        log.info("[{}]session:{}, wrap decorator:{}", TraceTagConstants.SPRING, getId(), getName());
    }
}
