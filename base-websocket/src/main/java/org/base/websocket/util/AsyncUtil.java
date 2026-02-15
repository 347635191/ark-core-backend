package org.base.websocket.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsyncUtil {
    /**
     * 异步任务执行，返回执行结果
     * @param future 异步回调结果
     * @param supplier 回调函数
     * @param <U> 泛型
     */
    public static <U> void execute(@NonNull CompletableFuture<U> future, @NonNull Supplier<U> supplier) {
        try {
            U result = supplier.get();
            future.complete(result);
        } catch (Throwable ex) {
            future.completeExceptionally(ex);
        }
    }
}
