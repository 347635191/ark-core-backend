package org.best.statistics.domain.chat.processor;

import org.best.statistics.gateway.ws.facade.model.WsResponse;

import java.util.List;

public interface ChatProcessor<T> {
    List<T> parse(String question);

    boolean matches(String question);

    WsResponse<T> buildResponse(List<T> message);
}
