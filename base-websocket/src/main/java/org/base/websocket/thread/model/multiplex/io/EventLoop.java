package org.base.websocket.thread.model.multiplex.io;

import org.base.websocket.thread.model.batch.BatchThread;

public class EventLoop extends BatchThread {
    EventLoop(String name, int pushMessageQueueSize) {
        super(name, pushMessageQueueSize);
    }

    @Override
    public void beforeExit() {}
}
