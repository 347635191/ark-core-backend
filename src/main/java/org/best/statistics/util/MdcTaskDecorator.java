package org.best.statistics.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.best.statistics.api.TraceTagConstants;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MdcTaskDecorator {

    public static Runnable decorate(Runnable runnable) {
        Map<String, String> ctx = MDC.getCopyOfContextMap();
        return () -> {
            try {
                Map<String, String> map = Optional.ofNullable(ctx).orElseGet(HashMap::new);
                map.putIfAbsent(TraceUtils.KEY, TraceUtils.create());
                MDC.setContextMap(map);
                runnable.run();
            } catch (Exception e) {
                log.warn("[{}]runnable run failed.", TraceTagConstants.TASK_RUN, e);
            } finally {
                MDC.clear();
            }
        };
    }
}