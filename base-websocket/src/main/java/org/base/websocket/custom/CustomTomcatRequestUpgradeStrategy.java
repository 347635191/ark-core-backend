package org.base.websocket.custom;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.adapter.standard.WebSocketToStandardExtensionAdapter;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;

import javax.websocket.Endpoint;
import javax.websocket.Extension;
import java.net.InetSocketAddress;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class CustomTomcatRequestUpgradeStrategy extends TomcatRequestUpgradeStrategy {
    private final UnaryOperator<WebSocketSession> wsSessionWrapper;

    CustomTomcatRequestUpgradeStrategy(UnaryOperator<WebSocketSession> wsSessionWrapper) {
        this.wsSessionWrapper = wsSessionWrapper;
    }

    @Override
    public void upgrade(ServerHttpRequest request, ServerHttpResponse response, String selectedProtocol,
                        List<WebSocketExtension> selectedExtensions, Principal user, WebSocketHandler wsHandler,
                        Map<String, Object> attrs) throws HandshakeFailureException {
        HttpHeaders headers = request.getHeaders();
        InetSocketAddress localAddress = null;
        try {
            localAddress = request.getLocalAddress();
        } catch (Exception ignored) {
        }
        InetSocketAddress remoteAddress = null;
        try {
            remoteAddress = request.getRemoteAddress();
        } catch (Exception ignored) {
        }
        StandardWebSocketSession session = new StandardWebSocketSession(headers, attrs, localAddress, remoteAddress,
                user);
        WebSocketSession asyncSession = wsSessionWrapper.apply(session);
        Endpoint endpoint = new CustomWebSocketHandlerAdapter(wsHandler, asyncSession);

        List<Extension> extensions = new ArrayList<>();
        for (WebSocketExtension extension : selectedExtensions) {
            extensions.add(new WebSocketToStandardExtensionAdapter(extension));
        }
        upgradeInternal(request, response, selectedProtocol, extensions, endpoint);
    }
}
