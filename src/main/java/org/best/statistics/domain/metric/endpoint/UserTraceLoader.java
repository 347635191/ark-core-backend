package org.best.statistics.domain.metric.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.best.statistics.api.TraceTagConstants;
import org.best.statistics.domain.metric.constants.RedisConstants;
import org.best.statistics.domain.metric.schedule.RedisSyncScheduler;
import org.best.statistics.util.MathUtils;
import org.springframework.context.SmartLifecycle;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserTraceLoader implements SmartLifecycle {
    private volatile boolean running;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void start() {
        init();
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

    public void init() {
        //请求数
        Long requestCount = MathUtils.parseNullZero(redisTemplate.opsForValue().get(RedisConstants.USER_REQUEST));
        UserTraceCollector.initRequest(requestCount);
        RedisSyncScheduler.initRequestPrev(requestCount);

        //用户ip
        Set<String> userIps = redisTemplate.opsForSet().members(RedisConstants.USER_IP);
        UserTraceCollector.initUserIp(userIps);
        RedisSyncScheduler.initUserIpPrev(userIps);
        log.info("[{}]load completed. requestCount:{}, userIps:{}", TraceTagConstants.USER_TRACE, requestCount,
                userIps);
    }
}
