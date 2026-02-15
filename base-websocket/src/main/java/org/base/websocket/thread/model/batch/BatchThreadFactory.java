package org.base.websocket.thread.model.batch;

import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.springframework.lang.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class BatchThreadFactory<T extends BatchThread> extends BaseComponent implements ThreadFactory {
    protected static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);
    protected final String namePrefix;

    protected BatchThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public final T newThread(@NonNull Runnable r) {
        T thread = newThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.warn("[{}]{} io task exec shutdown hook", TraceTagConstants.BATCH_THREAD, thread.getName());
            thread.interrupt();
        }));
        return thread;
    }

    protected abstract T newThread();
}
