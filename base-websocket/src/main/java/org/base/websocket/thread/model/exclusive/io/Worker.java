package org.base.websocket.thread.model.exclusive.io;

import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.base.websocket.thread.model.batch.BatchThread;

@Slf4j
public class Worker extends BatchThread {
    Worker(String name, int pushMessageQueueSize) {
        super(name, pushMessageQueueSize);
    }

    @Override
    public void beforeExit() {
        log.warn("[{}]{} interrupted, session:{}", TraceTagConstants.SINGLE_THREAD, getName(), getId());
        interrupt();
    }
}
