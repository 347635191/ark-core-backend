package org.base.websocket.session;

import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.base.websocket.metric.BaseWebSocketSessionCollector;
import org.base.websocket.thread.model.api.Assistant;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketSessionDecorator;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class AbstractAsyncWebSocketSession extends WebSocketSessionDecorator {
    private final Assistant assistant;

    public AbstractAsyncWebSocketSession(WebSocketSession session, Assistant assistant) {
        super(session);
        this.assistant = assistant;
    }

    protected String getName() {
        return assistant.getName();
    }

    @Override
    public final void sendMessage(@NonNull WebSocketMessage<?> message) {
        if (onError() || assistant.isAbnormal()) {
            log.warn("[{}]sending rejected", TraceTagConstants.ASYNC_WS);
            destroy();
            return;
        }
        long start = System.currentTimeMillis();
        CompletableFuture<Integer> future = assistant.submit(() -> doSend(message));
        future.whenComplete((length, e) -> {
            if (Objects.nonNull(e)) {
                log.warn("[{}]sending failed, session:{}, msg:{}", TraceTagConstants.ASYNC_WS, getId(), message, e);
                length = 0;
            } else if (log.isDebugEnabled()) {
                log.debug("[{}]sending completed, session:{}, msg:{}", TraceTagConstants.ASYNC_WS, getId(), message);
            }
            metric(assistant.backlog(), start, length);
        });
    }

    private boolean onError() {
        return Objects.isNull(assistant);
    }

    protected final int doSend(@NonNull WebSocketMessage<?> message) {
        if (!isOpen()) {
            log.debug("[{}]sending discarded, session:{}, msg:{}", TraceTagConstants.ASYNC_WS, getId(), message);
            return 0;
        }
        try {
            super.sendMessage(message);
        } catch (IllegalStateException e) {
            log.warn("[{}]sending interrupted, err:{}", TraceTagConstants.ASYNC_WS, e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        //返回消息文本字节数
        return message.getPayloadLength();
    }

    @Override
    public final void close() throws IOException {
        Optional.ofNullable(assistant).ifPresent(Assistant::beforeExit);
        super.close();
    }

    @Override
    public final void close(@NonNull CloseStatus status) throws IOException {
        Optional.ofNullable(assistant).ifPresent(Assistant::beforeExit);
        super.close(status);
    }

    private void metric(int backlog, long start, long length) {
        long delay = System.currentTimeMillis() - start;
        BaseWebSocketSessionCollector.register((long) backlog, delay, length);
        log.info("[{}]websocket:{}, uri:{}, metric:[taskBacklog:{}|delay:{}|byteLength:{}]",
                TraceTagConstants.ASYNC_WS, getId(), getUri(), backlog, delay, length);
    }

    private void destroy() {
        try {
            close();
        } catch (Exception ignored) {
        }
    }
}
