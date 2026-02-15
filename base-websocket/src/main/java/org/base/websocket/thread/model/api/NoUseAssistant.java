package org.base.websocket.thread.model.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.base.websocket.util.AsyncUtil;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoUseAssistant implements Assistant {
    public static NoUseAssistant instance() {
        return Creator.INSTANCE;
    }

    @Override
    public String getName() {
        return "NoUse";
    }

    @Override
    public boolean isAbnormal() {
        return false;
    }

    @Override
    public <U> CompletableFuture<U> submit(Supplier<U> supplier) {
        CompletableFuture<U> future = new CompletableFuture<>();
        AsyncUtil.execute(future, supplier);
        return future;
    }

    @Override
    public int backlog() {
        return 0;
    }

    @Override
    public void beforeExit() {}

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class Creator {
        private static final NoUseAssistant INSTANCE;

        static {
            INSTANCE = new NoUseAssistant();
        }
    }
}
