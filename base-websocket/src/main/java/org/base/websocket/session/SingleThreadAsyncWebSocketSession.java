package org.base.websocket.session;

import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.base.websocket.thread.model.exclusive.io.WorkerDispatcher;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class SingleThreadAsyncWebSocketSession extends AbstractAsyncWebSocketSession {
    public SingleThreadAsyncWebSocketSession(WebSocketSession session) {
        super(session, WorkerDispatcher.next());
        log.info("[{}]session:{}, create single-thread:{}", TraceTagConstants.SINGLE_THREAD, getId(), getName());
    }
}
