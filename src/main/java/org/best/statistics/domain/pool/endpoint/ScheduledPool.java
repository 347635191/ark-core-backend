package org.best.statistics.domain.pool.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.best.statistics.api.TraceTagConstants;
import org.best.statistics.domain.pool.template.Assistant;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ScheduledPool extends ScheduledThreadPoolExecutor implements Assistant {
    private static final String NAME_PREFIX = "scheduled-pool-";
    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    private final String name;

    ScheduledPool() {
        super(1, new ThreadPoolExecutor.CallerRunsPolicy());
        name = NAME_PREFIX + POOL_NUMBER.getAndIncrement();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isAbnormal() {
        if (isShutdown() || isTerminated() || isTerminating()) {
            log.warn("[{}]{} is not working.", TraceTagConstants.SCHEDULED_POOL, getName());
            return true;
        }
        return false;
    }
}
