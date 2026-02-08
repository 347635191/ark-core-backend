package org.best.statistics.domain.pool.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.best.statistics.api.TraceTagConstants;
import org.best.statistics.domain.pool.template.BasePool;

import java.util.concurrent.TimeUnit;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScheduledPoolFactory extends BasePool {

    public static ScheduledPoolFactory instance() {
        return Creator.INSTANCE;
    }

    public ScheduledPool newPool() {
        ScheduledPool scheduledPool = new ScheduledPool();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.warn("[{}]{} task exec shutdown hook.", tag(), scheduledPool.getName());
            shutDown(scheduledPool);
        }));
        return scheduledPool;
    }

    private void shutDown(ScheduledPool scheduledPool) {
        String name = scheduledPool.getName();
        scheduledPool.shutdown();
        try {
            if (!scheduledPool.awaitTermination(20, TimeUnit.SECONDS)) {
                scheduledPool.shutdownNow();
                log.warn("[{}]{} shutdownNow, all workers stopped.", tag(), name);
            }
        } catch (InterruptedException e) {
            scheduledPool.shutdownNow();
            log.warn("[{}}]{} shutdownNow again.", tag(), name);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected String tag() {
        return TraceTagConstants.SCHEDULED_POOL;
    }

    private static final class Creator {
        private static final ScheduledPoolFactory INSTANCE;

        static {
            INSTANCE = new ScheduledPoolFactory();
        }

        private Creator() {
        }
    }
}
