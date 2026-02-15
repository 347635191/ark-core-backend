package org.best.statistics.domain.metric.schedule;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.best.statistics.constants.TraceTagConstants;
import org.best.statistics.domain.metric.constants.RedisConstants;
import org.best.statistics.domain.metric.endpoint.UserTraceCollector;
import org.best.statistics.domain.metric.endpoint.UserTraceLoader;
import org.best.statistics.domain.pool.facade.ScheduledPoolGenerator;
import org.best.statistics.util.DateUtils;
import org.best.statistics.util.MdcTaskDecorator;
import org.springframework.context.SmartLifecycle;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSyncScheduler implements SmartLifecycle {
    private static Long REQUEST_COUNT_PREV;
    private static Set<String> USER_IP_PREV;
    private static final ScheduledThreadPoolExecutor executor = ScheduledPoolGenerator.next();
    private boolean running = false;

    private final RedisTemplate<String, String> redisTemplate;
    private final UserTraceLoader userTraceLoader;

    @Override
    public void start() {
        executor.scheduleWithFixedDelay(MdcTaskDecorator.decorate(this::syncRedis), 0, 10, TimeUnit.SECONDS);
        running = true;
    }

    @Override
    public void stop() {
        //兜底同步
        syncRedis();
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public static void initRequestPrev(Long requestCount) {
        REQUEST_COUNT_PREV = requestCount;
    }

    public static void initUserIpPrev(Set<String> userIps) {
        USER_IP_PREV = userIps == null ? new HashSet<>() : new HashSet<>(userIps);
    }

    private void syncRedis() {
        if (!userTraceLoader.isRunning()) {
            log.warn("[{}]waiting for data to load", TraceTagConstants.REDIS_SYNC);
            return;
        }
        syncRequestIfNecessary();
        syncUserIpIfNecessary();
    }

    private void syncUserIpIfNecessary() {
        Set<String> prevUserIps = USER_IP_PREV;
        Set<String> curUserIps = UserTraceCollector.getUserIp();
        Sets.SetView<String> difference = Sets.difference(curUserIps, prevUserIps);
        if (CollectionUtils.isEmpty(difference)) {
            return;
        }
        String[] userIps = difference.toArray(new String[0]);
        //日终失效
        redisTemplate.executePipelined((RedisCallback<?>) connection -> {
            StringRedisConnection conn = (StringRedisConnection) connection;
            conn.sAdd(RedisConstants.USER_IP, userIps);
            conn.expire(RedisConstants.USER_IP, DateUtils.endOfDayDuration().getSeconds());
            return null;
        });
        prevUserIps.addAll(difference);
        log.info("[{}]request sync completed. key:[{}], value:[{}]", TraceTagConstants.REDIS_SYNC,
                RedisConstants.USER_IP, userIps);
    }

    private void syncRequestIfNecessary() {
        Long prevRequest = REQUEST_COUNT_PREV;
        Long curRequest = UserTraceCollector.getRequest();
        if (Objects.equals(prevRequest, curRequest)) {
            return;
        }
        //日终失效
        redisTemplate.opsForValue().set(RedisConstants.USER_REQUEST, String.valueOf(curRequest),
                DateUtils.endOfDayDuration());
        REQUEST_COUNT_PREV = curRequest;
        log.info("[{}]user ip sync completed. key:[{}], value:[{}]", TraceTagConstants.REDIS_SYNC,
                RedisConstants.USER_REQUEST, curRequest);
    }
}
