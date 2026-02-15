package org.best.statistics.gateway.ws.endpoint;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.best.statistics.domain.chat.entity.ChatWsRequest;
import org.best.statistics.gateway.ws.facade.model.WsRequest;
import org.best.statistics.gateway.ws.session.EnhancedWebSocketSession;
import org.best.statistics.gateway.ws.template.BaseWebSocketHandler;
import org.best.statistics.domain.chat.template.IChatWsRequestHandler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends BaseWebSocketHandler<ChatWsRequest> {
    private final Map<String, IChatWsRequestHandler> handlerMap;

    @Override
    protected ChatWsRequest decode(String message) {
        return JSON.parseObject(message, ChatWsRequest.class);
    }

    @Override
    protected void receiveTextMessage(EnhancedWebSocketSession session, ChatWsRequest wsRequest) {
        //根据messageType分发
        Optional.ofNullable(wsRequest).map(WsRequest::getMessageType).map(handlerMap::get)
                .ifPresent(handler -> handler.onMessage(session, wsRequest));
    }
}
