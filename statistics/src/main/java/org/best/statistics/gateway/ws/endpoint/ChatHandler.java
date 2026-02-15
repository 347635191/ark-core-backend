package org.best.statistics.gateway.ws.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.best.statistics.domain.chat.endpoint.Answer;
import org.best.statistics.domain.chat.entity.ChatWsRequest;
import org.best.statistics.domain.chat.processor.ChatAbstractProcessor;
import org.best.statistics.domain.chat.template.IChatWsRequestHandler;
import org.best.statistics.gateway.ws.constants.EventType;
import org.best.statistics.gateway.ws.facade.model.WsResponse;
import org.best.statistics.gateway.ws.session.EnhancedWebSocketSession;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component(EventType.CHAT)
@RequiredArgsConstructor
public class ChatHandler implements IChatWsRequestHandler {
    private final List<ChatAbstractProcessor<String>> processors;
    private final ChatWsSessionContainer chatWsSessionContainer;

    @Override
    public void onMessage(EnhancedWebSocketSession session, ChatWsRequest request) {
        chatWsSessionContainer.checkIn(session);
        String question = request.getQuestion();
        if (StringUtils.isBlank(question)) {
            return;
        }
        WsResponse<String> response = processors.stream().filter(processor -> processor.matches(question))
                .findFirst()
                .map(processor -> {
                    List<String> message = processor.parse(question);
                    return processor.buildResponse(message);
                }).orElse(Answer.CHAT_UNKNOWN);
        session.sendMessage(response);
    }
}
