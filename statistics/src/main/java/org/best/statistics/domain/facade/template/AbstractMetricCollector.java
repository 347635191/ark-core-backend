package org.best.statistics.domain.facade.template;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractMetricCollector implements InitializingBean {

    private MeterRegistry meterRegistry;

    @Autowired
    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * 注册指标
     *
     * @param metric 指标名称
     * @param state  指标收集实体
     */
    protected void createRealTimeGauge(String metric, AtomicLong state) {
        Gauge.builder(metric, state, obj -> obj.getAndSet(0L)).description(metric).register(meterRegistry);
    }

    /**
     * 注册指标
     *
     * @param metric 指标名称
     * @param state  指标收集实体
     */
    protected void createGauge(String metric, AtomicLong state) {
        Gauge.builder(metric, state, AtomicLong::get).description(metric).register(meterRegistry);
    }

    /**
     * 注册指标
     *
     * @param metric 指标名称
     * @param state  指标收集实体
     */
    protected void createGauge(String metric, Collection<?> state) {
        Gauge.builder(metric, state, Collection::size).description(metric).register(meterRegistry);
    }

    /**
     * 注册指标
     *
     * @param metric 指标名称
     * @param state  指标收集实体
     */
    protected void createRealTimeGauge(String metric, BlockingQueue<Long> state) {
        Gauge.builder(metric, state, queue -> {
            Set<Long> current = new HashSet<>();
            queue.drainTo(current);
            return current.isEmpty() ? 0 : Collections.max(current);
        }).description(metric).register(meterRegistry);
    }
}
