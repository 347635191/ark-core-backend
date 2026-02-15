package org.base.websocket.thread.model.pool;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.base.websocket.thread.model.batch.BaseComponent;

import java.util.concurrent.TimeUnit;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExclusivePoolFactory extends BaseComponent {

    public static ExclusivePoolFactory instance() {
        return Creator.INSTANCE;
    }

    public ExclusivePool newPool() {
        ExclusivePool exclusivePool = new ExclusivePool(isKillCloseConnectionImmediately(), getPushMessageQueueSize());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.warn("[{}]{} task exec shutdown hook.", TraceTagConstants.SINGLE_POOL, exclusivePool.getName());
            shutDown(exclusivePool);
        }));
        return exclusivePool;
    }

    private void shutDown(ExclusivePool exclusivePool) {
        String name = exclusivePool.getName();
        exclusivePool.shutdown();
        try {
            if (!exclusivePool.awaitTermination(20, TimeUnit.SECONDS)) {
                exclusivePool.shutdownNow();
                log.warn("[{}]{} shutdownNow, all workers stopped.", TraceTagConstants.SINGLE_POOL, name);
            }
        } catch (InterruptedException e) {
            exclusivePool.shutdownNow();
            log.warn("[{}}]{} shutdownNow again.", TraceTagConstants.SINGLE_POOL, name);
            Thread.currentThread().interrupt();
        }
    }

    private static final class Creator {
        private static final ExclusivePoolFactory INSTANCE;

        static {
            INSTANCE = new ExclusivePoolFactory();
        }

        private Creator() {
        }
    }
}
