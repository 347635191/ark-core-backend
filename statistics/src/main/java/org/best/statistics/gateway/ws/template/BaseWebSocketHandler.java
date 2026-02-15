package org.best.statistics.gateway.ws.template;

import lombok.extern.slf4j.Slf4j;
import org.best.statistics.constants.TraceTagConstants;
import org.best.statistics.gateway.ws.constants.EventType;
import org.best.statistics.gateway.ws.endpoint.HeartBeatHandler;
import org.best.statistics.gateway.ws.facade.model.WsRequest;
import org.best.statistics.gateway.ws.session.EnhancedWebSocketSession;
import org.best.statistics.util.TraceUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.List;

@Slf4j
public abstract class BaseWebSocketHandler<T extends WsRequest> extends AbstractWebSocketHandler {
    private List<WsSessionContainer> containers;

    @Autowired
    public void setObserverList(List<WsSessionContainer> containers) {
        this.containers = containers;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[{}]connection established, id:{}", TraceTagConstants.WEB_SOCKET, session.getId());
        containers.forEach(observer -> observer.checkOut(session));
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MDC.put(TraceUtils.KEY, TraceUtils.create());
        log.info("[{}]message received, id:{}, content:{}", TraceTagConstants.WEB_SOCKET, session.getId(),
                message.getPayload());
        T wsRequest = decode(message.getPayload());
        EnhancedWebSocketSession webSocketSession = (EnhancedWebSocketSession) session;
        if (EventType.HEART_BEAT.equals(wsRequest.getMessageType())) {
            webSocketSession.sendMessage(HeartBeatHandler.RESPONSE);
        } else {
            receiveTextMessage(webSocketSession, wsRequest);
        }
    }

    protected abstract T decode(String message);

    protected abstract void receiveTextMessage(EnhancedWebSocketSession session, T wsRequest);
}
