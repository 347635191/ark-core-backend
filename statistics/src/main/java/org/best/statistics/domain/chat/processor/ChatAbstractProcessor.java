package org.best.statistics.domain.chat.processor;

import org.best.statistics.gateway.ws.config.ChatIdentifyProperties;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ChatAbstractProcessor<T> implements ChatProcessor<T> {
    @Autowired
    protected ChatIdentifyProperties chatIdentifyProperties;
}
