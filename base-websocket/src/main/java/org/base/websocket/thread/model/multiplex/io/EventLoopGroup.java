package org.base.websocket.thread.model.multiplex.io;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EventLoopGroup {
    private static final AtomicInteger COUNT = new AtomicInteger(1);
    private static final int MAX_COUNT = 4;
    private static final Map<Integer, EventLoop> GROUP = new ConcurrentHashMap<>();

    /**
     * 获取io线程
     * 1. 创建io线程数小于阈值，创建线程并返回
     * 2. 创建io线程大于等于阈值，取模后返回对应位置的已创建线程
     */
    public static EventLoop next() {
        int index = COUNT.getAndIncrement() & (MAX_COUNT - 1);
        GROUP.compute(index, (idx, thread) -> {
            EventLoop eventLoop = thread;
            if (eventLoop == null || eventLoop.isAbnormal()) {
                eventLoop = EventLoopFactory.defaultThreadFactory().newThread();
                eventLoop.start();
            }
            return eventLoop;
        });
        return GROUP.get(index);
    }
}
