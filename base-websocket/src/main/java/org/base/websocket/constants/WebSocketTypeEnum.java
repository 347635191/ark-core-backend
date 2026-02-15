package org.base.websocket.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum WebSocketTypeEnum {
    SINGLE_POOL("SINGLE_POOL"),
    SINGLE_THREAD("SINGLE_THREAD"),
    BIND_THREAD("BIND_THREAD"),
    SPRING("SPRING"),
    ;

    private final String type;

    private static final Map<String, WebSocketTypeEnum> values = Arrays.stream(WebSocketTypeEnum.values())
            .collect(Collectors.toMap(WebSocketTypeEnum::getType, Function.identity()));

    public static WebSocketTypeEnum fromType(String type) {
        return values.get(type);
    }
}
