package org.base.websocket.thread.model.batch;

import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.base.websocket.thread.model.api.Assistant;
import org.base.websocket.util.AsyncUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
public abstract class BatchThread extends Thread implements Assistant {
    private final BlockingQueue<Runnable> queue;

    protected BatchThread(String name, int pushMessageQueueSize) {
        super(name);
        if (isDaemon()) {
            setDaemon(false);
        }
        if (getPriority() != Thread.NORM_PRIORITY) {
            setPriority(Thread.NORM_PRIORITY);
        }
        this.queue = new ArrayBlockingQueue<>(pushMessageQueueSize);
    }

    @Override
    public boolean isAbnormal() {
        if (this.isInterrupted()) {
            log.warn("[{}]{} is interrupted", TraceTagConstants.BATCH_THREAD, getName());
            return true;
        }
        return false;
    }

    @Override
    public <U> CompletableFuture<U> submit(Supplier<U> supplier) {
        CompletableFuture<U> future = new CompletableFuture<>();
        if (!queue.offer(() -> AsyncUtil.execute(future, supplier))) {
            log.warn("[{}]{} failed to submit the task", TraceTagConstants.BATCH_THREAD, getName());
            future.cancel(true);
        }
        return future;
    }

    @Override
    public int backlog() {
        return queue.size();
    }

    /**
     * 批量执行任务
     */
    @Override
    public final void run() {
        long count = 0L;
        while (!Thread.currentThread().isInterrupted()) {
            count++;
            try {
                Runnable ioTask = queue.take();
                ioTask.run();
                if (count % 1000L == 0) {
                    Thread.yield();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("[{}]{} io task interrupt", TraceTagConstants.BATCH_THREAD, getName(), e);
            } catch (Exception e) {
                log.warn("[{}]{} io task run failed", TraceTagConstants.BATCH_THREAD, getName(), e);
            }
        }
    }
}
