package org.best.statistics.domain.chat.processor;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.best.statistics.constants.TraceTagConstants;
import org.best.statistics.domain.chat.endpoint.Answer;
import org.best.statistics.domain.chat.entity.Speed;
import org.best.statistics.gateway.ws.constants.EventType;
import org.best.statistics.gateway.ws.facade.model.WsResponse;
import org.best.statistics.util.SpeedCalculatorUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CalculateSpeedProcessor extends ChatAbstractProcessor<String> {
    @Override
    public List<String> parse(String question) {
        Speed speed = SpeedCalculatorUtils.getSpeed(question, chatIdentifyProperties.getActiveValue()
                , chatIdentifyProperties.getMessValue(), chatIdentifyProperties.getSpeed());
        if (Objects.isNull(speed)) {
            log.warn("[{}]can not calculate speed, question:{}", TraceTagConstants.CHAT_PARSE, question);
            return null;
        }
        return Lists.newArrayList(speed.convert());
    }

    @Override
    public boolean matches(String question) {
        return StringUtils.containsAny(question, chatIdentifyProperties.getCalSpeed().toArray(new String[0]));
    }

    @Override
    public WsResponse<String> buildResponse(List<String> message) {
        return CollectionUtils.isEmpty(message) ? Answer.CALCULATE_SPEED_UNKNOWN :
                WsResponse.<String>builder().data(message).messageType(EventType.CHAT).build();
    }

}
