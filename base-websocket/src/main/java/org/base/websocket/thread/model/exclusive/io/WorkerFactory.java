package org.base.websocket.thread.model.exclusive.io;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.base.websocket.thread.model.batch.BatchThreadFactory;

public class WorkerFactory extends BatchThreadFactory<Worker> {
    private static final String NAME_PREFIX = "exclusive-io-batch-";

    private WorkerFactory() {
        super(NAME_PREFIX);
    }

    static WorkerFactory defaultThreadFactory() {
        return Creator.INSTANCE;
    }

    @Override
    protected Worker newThread() {
        String threadName = namePrefix + THREAD_NUMBER.getAndIncrement();
        return new Worker(threadName, getPushMessageQueueSize());
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class Creator {
        private static final WorkerFactory INSTANCE;

        static {
            INSTANCE = new WorkerFactory();
        }
    }
}
