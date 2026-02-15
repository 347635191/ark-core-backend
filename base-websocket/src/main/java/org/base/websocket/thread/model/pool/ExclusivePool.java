package org.base.websocket.thread.model.pool;

import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.base.websocket.thread.model.api.Assistant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Slf4j
public class ExclusivePool extends ThreadPoolExecutor implements Assistant {
    private static final String NAME_PREFIX = "exclusive-pool-";
    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    private final boolean killCloseConnectionImmediately;
    private final String name;

    ExclusivePool(boolean killCloseConnectionImmediately, int pushMessageQueueSize) {
        super(1, 1, 0L, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(pushMessageQueueSize));
        this.killCloseConnectionImmediately = killCloseConnectionImmediately;
        name = NAME_PREFIX + POOL_NUMBER.getAndIncrement();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isAbnormal() {
        if (isShutdown() || isTerminated() || isTerminating()) {
            log.warn("[{}]{} is not working.", TraceTagConstants.SINGLE_POOL, getName());
            return true;
        }
        return false;
    }

    @Override
    public <U> CompletableFuture<U> submit(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, this);
    }

    @Override
    public int backlog() {
        return getQueue().size();
    }

    @Override
    public void beforeExit() {
        if (killCloseConnectionImmediately) {
            shutdownNow();
        } else {
            shutdown();
        }
        log.warn("[{}]{} shutdown", TraceTagConstants.SINGLE_POOL, getName());
    }
}
