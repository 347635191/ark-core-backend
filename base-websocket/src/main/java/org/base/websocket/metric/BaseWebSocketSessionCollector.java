package org.base.websocket.metric;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class BaseWebSocketSessionCollector {
    static final String METRIC_BACKLOG = "websocket_task_backlog";
    static final String METRIC_DELAY = "websocket_delay";
    static final String METRIC_BYTE = "websocket_byte_length_total";
    static final String METRIC_RATE = "websocket_rate";
    static final AtomicReference<Long> TASK_BACKLOG = new AtomicReference<>(0L);
    static final AtomicReference<Long> DELAY = new AtomicReference<>(0L);
    static final AtomicReference<Long> RATE = new AtomicReference<>(0L);
    static final AtomicReference<Long> BYTE_LENGTH_TOTAL = new AtomicReference<>(0L);

    /**
     * 收集指标
     */
    public static void register(Long... metric) {
        if (Objects.isNull(metric) || metric.length != 3) {
            return;
        }
        log.info("[{}]metric collected, backlog:{}, delay:{}, byteLength:{}", TraceTagConstants.METRIC_WS, metric[0],
                metric[1], metric[2]);
        // 单位：个
        TASK_BACKLOG.getAndUpdate(old -> Long.max(old, metric[0]));
        // 单位：毫秒
        DELAY.getAndUpdate(old -> Long.max(old, metric[1]));
        // 单位：字节
        BYTE_LENGTH_TOTAL.getAndUpdate(old -> old + metric[2]);
        // 单位：字节/秒
        RATE.getAndUpdate(old -> {
            if (metric[1] == 0L || metric[2] == 0L) {
                return old;
            }
            long now = metric[2] / metric[1];
            return now < old || old == 0L ? now : old;
        });
    }
}
