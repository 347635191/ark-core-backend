package org.best.statistics.domain.chat.endpoint;

import com.google.common.collect.Lists;
import org.best.statistics.constants.ChatConstants;
import org.best.statistics.gateway.ws.constants.EventType;
import org.best.statistics.gateway.ws.facade.model.WsResponse;

import java.util.List;

public class Answer {
    public static final WsResponse<String> CHAT_UNKNOWN;
    public static final WsResponse<String> CALCULATE_SPEED_UNKNOWN;

    static {
        List<String> message1 = Lists.newArrayList(ChatConstants.CHAT_UNKNOWN);
        CHAT_UNKNOWN = WsResponse.<String>builder().data(message1).messageType(EventType.CHAT).build();

        List<String> message2 = Lists.newArrayList(ChatConstants.CALCULATE_SPEED_UNKNOWN);
        CALCULATE_SPEED_UNKNOWN = WsResponse.<String>builder().data(message2).messageType(EventType.CHAT).build();
    }
}
