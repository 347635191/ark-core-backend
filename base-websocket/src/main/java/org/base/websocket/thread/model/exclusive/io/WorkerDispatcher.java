package org.base.websocket.thread.model.exclusive.io;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkerDispatcher {
    /**
     * 每次调用创建新线程
     */
    public static Worker next() {
        Worker worker = WorkerFactory.defaultThreadFactory().newThread();
        //初始化启动核心线程
        worker.start();
        return worker;
    }
}
