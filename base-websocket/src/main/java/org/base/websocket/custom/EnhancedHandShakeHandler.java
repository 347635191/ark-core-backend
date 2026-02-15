package org.base.websocket.custom;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.function.UnaryOperator;

public class EnhancedHandShakeHandler extends DefaultHandshakeHandler {
    public EnhancedHandShakeHandler(UnaryOperator<WebSocketSession> wsSessionWrapper) {
        super(new CustomTomcatRequestUpgradeStrategy(wsSessionWrapper));
    }
}
