package org.best.statistics.domain.chat.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.best.statistics.gateway.ws.facade.model.WsRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChatWsRequest extends WsRequest {
    private static final long serialVersionUID = -7749363157864470240L;
    private String question;
}
