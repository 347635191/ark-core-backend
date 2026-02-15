package org.base.websocket.thread.model.multiplex.io;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.base.websocket.thread.model.batch.BatchThreadFactory;

public class EventLoopFactory extends BatchThreadFactory<EventLoop> {
    private static final String NAME_PREFIX = "multiplex-io-batch-";

    private EventLoopFactory() {
        super(NAME_PREFIX);
    }

    static EventLoopFactory defaultThreadFactory() {
        return Creator.INSTANCE;
    }

    @Override
    protected EventLoop newThread() {
        String threadName = namePrefix + THREAD_NUMBER.getAndIncrement();
        return new EventLoop(threadName, getPushMessageQueueSize());
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class Creator {
        private static final EventLoopFactory INSTANCE;

        static {
            INSTANCE = new EventLoopFactory();
        }
    }
}
