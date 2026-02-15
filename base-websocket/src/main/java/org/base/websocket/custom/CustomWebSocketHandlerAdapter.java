package org.base.websocket.custom;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.springframework.web.socket.*;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.handler.WebSocketSessionDecorator;

import javax.websocket.*;
import java.nio.ByteBuffer;

@Slf4j
public class CustomWebSocketHandlerAdapter extends Endpoint {
    private final WebSocketHandler handler;
    private final WebSocketSession session;

    CustomWebSocketHandlerAdapter(WebSocketHandler handler, WebSocketSession session) {
        this.handler = handler;
        this.session = session;
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        WebSocketSession standardSession = this.session;
        while (standardSession instanceof WebSocketSessionDecorator) {
            standardSession = ((WebSocketSessionDecorator) standardSession).getDelegate();
        }
        if (standardSession instanceof StandardWebSocketSession) {
            StandardWebSocketSession delegate = (StandardWebSocketSession) standardSession;
            delegate.initializeNativeSession(session);
        }
        // 此处不可用lambda简写
        if (this.handler.supportsPartialMessages()) {
            session.addMessageHandler(new MessageHandler.Partial<String>() {
                @Override
                public void onMessage(String message, boolean isLast) {
                    handleTextMessage(message, isLast);
                }
            });
            session.addMessageHandler(new MessageHandler.Partial<ByteBuffer>() {
                @Override
                public void onMessage(ByteBuffer byteBuffer, boolean isLast) {
                    handleBinaryMessage(byteBuffer, isLast);
                }
            });
        } else {
            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    handleTextMessage(message, true);
                }
            });
            session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
                @Override
                public void onMessage(ByteBuffer byteBuffer) {
                    handleBinaryMessage(byteBuffer, true);
                }
            });
        }
        session.addMessageHandler(new MessageHandler.Whole<javax.websocket.PongMessage>() {
            @Override
            public void onMessage(javax.websocket.PongMessage pongMessage) {
                handlePongMessage(pongMessage.getApplicationData());
            }
        });
        try {
            this.handler.afterConnectionEstablished(this.session);
        } catch (Exception e) {
            ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.session, e);
        }
    }

    private void handleTextMessage(String payload, boolean isLast) {
        TextMessage textMessage = new TextMessage(payload, isLast);
        try {
            this.handler.handleMessage(this.session, textMessage);
        } catch (Exception e) {
            ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.session, e);
        }
    }

    private void handleBinaryMessage(ByteBuffer payload, boolean isLast) {
        BinaryMessage binaryMessage = new BinaryMessage(payload, isLast);
        try {
            this.handler.handleMessage(this.session, binaryMessage);
        } catch (Exception e) {
            ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.session, e);
        }
    }

    private void handlePongMessage(ByteBuffer payload) {
        PongMessage pongMessage = new PongMessage(payload);
        try {
            this.handler.handleMessage(this.session, pongMessage);
        } catch (Exception e) {
            ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.session, e);
        }
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        CloseStatus closeStatus = new CloseStatus(closeReason.getCloseCode().getCode(), closeReason.getReasonPhrase());
        try {
            this.handler.afterConnectionClosed(this.session, closeStatus);
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("[{}]unhandled on-close exception for {}", TraceTagConstants.CUSTOM_ADAPTOR, session, e);
            }
        }
    }

    @Override
    public void onError(Session session, Throwable throwable) {
        try {
            this.handler.handleTransportError(this.session, throwable);
        } catch (Exception e) {
            ExceptionWebSocketHandlerDecorator.tryCloseWithError(this.session, e);
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class ExceptionWebSocketHandlerDecorator {
        private static void tryCloseWithError(WebSocketSession session, Throwable exception) {
            if (log.isErrorEnabled()) {
                log.error("[{}]closing session due to exception for {}", TraceTagConstants.CUSTOM_ADAPTOR, session,
                        exception);
            }
            if (session.isOpen()) {
                try {
                    session.close(CloseStatus.SERVER_ERROR);
                } catch (Exception ignored) {
                }
            }
        }
    }
}
