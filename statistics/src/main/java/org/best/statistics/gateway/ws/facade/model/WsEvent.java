package org.best.statistics.gateway.ws.facade.model;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class WsEvent extends ApplicationEvent {
    private static final long serialVersionUID = -2378912909102312686L;
    private final transient WsResponse<?> wsResponse;

    public WsEvent(WsResponse<?> wsResponse) {
        super(serialVersionUID);
        this.wsResponse = wsResponse;
    }

    public static <T> WsEvent accept(List<T> data, String messageType) {
        WsResponse<T> response = WsResponse.<T>builder().data(data).messageType(messageType).build();
        return new WsEvent(response);
    }
}
