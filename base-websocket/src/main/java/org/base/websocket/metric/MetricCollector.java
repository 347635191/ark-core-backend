package org.base.websocket.metric;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.base.websocket.constants.TraceTagConstants;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.ToDoubleFunction;

@Slf4j
public class MetricCollector extends BaseWebSocketSessionCollector implements InitializingBean {
    private final MeterRegistry meterRegistry;

    public MetricCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        log.info("[{}]metric collector init...", TraceTagConstants.METRIC_WS);
    }

    /**
     * 注册指标
     */
    private <T> void createGauge(String metric, AtomicReference<T> state,
                                 ToDoubleFunction<AtomicReference<T>> measure) {
        Gauge.builder(metric, state, measure).description(metric).register(meterRegistry);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        createGauge(METRIC_BACKLOG, TASK_BACKLOG, ele -> ele.getAndSet(0L));
        createGauge(METRIC_DELAY, DELAY, ele -> ele.getAndSet(0L));
        createGauge(METRIC_BYTE, BYTE_LENGTH_TOTAL, ele -> ele.getAndSet(0L));
        createGauge(METRIC_RATE, RATE, ele -> ele.getAndSet(0L));
    }
}
