package org.best.statistics.gateway.ws.facade.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WsResponse<T> {
    private final List<T> data;
    private final String messageType;
    @JSONField(serialize = false)
    private final Map<String, String> mdc;
}
