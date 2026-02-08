package org.best.statistics.domain.pool.facade;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.best.statistics.domain.pool.endpoint.ScheduledPool;
import org.best.statistics.domain.pool.endpoint.ScheduledPoolFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduledPoolGenerator {
    public static ScheduledPool next() {
        ScheduledPool scheduledPool = ScheduledPoolFactory.instance().newPool();
        // 初始化启动核心线程
        scheduledPool.prestartCoreThread();
        return scheduledPool;
    }
}
