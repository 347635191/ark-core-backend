package org.best.statistics.gateway.ws.interceptor;

import org.best.statistics.util.ClientUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class ChatWebSocketInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler
            , Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
        String ip = ClientUtils.getIp(httpServletRequest);
        attributes.put("clientIp", ip);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
