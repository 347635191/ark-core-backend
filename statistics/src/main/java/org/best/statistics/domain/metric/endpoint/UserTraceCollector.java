package org.best.statistics.domain.metric.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.best.statistics.domain.facade.template.AbstractMetricCollector;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class UserTraceCollector extends AbstractMetricCollector {
    private static final AtomicLong REQUEST_COUNT = new AtomicLong(0L);
    private static final Set<String> USER_IP_SET = new HashSet<>();

    /**
     * 收集日请求数指标
     */
    public static void registerRequest() {
        REQUEST_COUNT.incrementAndGet();
    }

    /**
     * 收集日用户数指标
     */
    public static void registerUserIp(String ip) {
        USER_IP_SET.add(ip);
    }

    public static void initRequest(Long requestCount) {
        REQUEST_COUNT.addAndGet(requestCount);
    }

    public static void initUserIp(Set<String> userIps) {
        if (userIps == null) {
            return;
        }
        USER_IP_SET.addAll(userIps);
    }

    public static Long getRequest() {
        return REQUEST_COUNT.get();
    }

    public static Set<String> getUserIp() {
        return USER_IP_SET;
    }

    @Override
    public void afterPropertiesSet() {
        createGauge(Config.METRIC_DAILY_REQUEST_COUNT, REQUEST_COUNT);
        createGauge(Config.METRIC_DAILY_USER_COUNT, USER_IP_SET);
    }

    private static final class Config {
        private static final String METRIC_DAILY_REQUEST_COUNT = "daily_request_count";
        private static final String METRIC_DAILY_USER_COUNT = "daily_user_count";
    }
}
