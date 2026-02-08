package org.best.statistics.domain.facade.template;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.best.statistics.util.TraceUtils;
import org.slf4j.MDC;
import org.springframework.context.SmartLifecycle;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractDBInitRunner implements SmartLifecycle {
    protected abstract Integer init();

    protected abstract String tag();

    private volatile boolean running = false;

    @Override
    public void start() {
        long start = System.currentTimeMillis();
        try (MDC.MDCCloseable ignored = MDC.putCloseable(TraceUtils.KEY, TraceUtils.create())) {
            log.info("[{}]db init started.", tag());
            Integer count = init();
            log.info("[{}]db init completed, size:{}, it takes {}ms", tag(), count, System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("[{}]db init failed.", tag(), e);
        }
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
