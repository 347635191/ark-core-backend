package org.base.websocket.thread.model.pool;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExclusivePoolGenerator {
    public static ExclusivePool next() {
        ExclusivePool exclusivePool = ExclusivePoolFactory.instance().newPool();
        // 初始化启动核心线程
        exclusivePool.prestartCoreThread();
        return exclusivePool;
    }
}
