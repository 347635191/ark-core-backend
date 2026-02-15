package org.best.statistics.gateway.ws.session;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.best.statistics.constants.TraceTagConstants;
import org.best.statistics.gateway.ws.facade.model.WsResponse;
import org.slf4j.MDC;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketSessionDecorator;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class EnhancedWebSocketSession extends WebSocketSessionDecorator {
    public EnhancedWebSocketSession(WebSocketSession session) {
        super(session);
    }

    public void sendMessage(WsResponse<?> wsResponse) {
        pushMessage(wsResponse);
    }

    private void pushMessage(WsResponse<?> response) {
        if (Objects.isNull(response)) {
            return;
        }
        try {
            String text = JSON.toJSONString(response, SerializerFeature.DisableCircularReferenceDetect);
            super.sendMessage(new TextMessage(text));
            Optional.ofNullable(response.getMdc()).ifPresent(MDC::setContextMap);
            log.info("[{}]session push success, id:{}, response:{}", TraceTagConstants.WEB_SOCKET, getId(), response);
        } catch (Exception e) {
            log.error("[{}]session push failed, id:{}, response:{}", TraceTagConstants.WEB_SOCKET, getId(), response);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        EnhancedWebSocketSession that = (EnhancedWebSocketSession) obj;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
