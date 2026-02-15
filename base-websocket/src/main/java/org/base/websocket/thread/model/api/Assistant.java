package org.base.websocket.thread.model.api;

import org.springframework.lang.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 辅助执行接口
 */
public interface Assistant {
    /**
     * 获取组件名
     */
    String getName();

    /**
     * 是否为非正常状态
     */
    boolean isAbnormal();

    /**
     * 提交推送任务
     * @param supplier 回调
     */
    <U> CompletableFuture<U> submit(@NonNull Supplier<U> supplier);

    /**
     * 任务积压量
     */
    int backlog();

    void beforeExit();
}
