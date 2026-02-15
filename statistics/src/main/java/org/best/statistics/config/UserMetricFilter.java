package org.best.statistics.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.best.statistics.constants.TraceTagConstants;
import org.best.statistics.domain.metric.endpoint.UserTraceCollector;
import org.best.statistics.util.ClientUtils;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMetricFilter extends OncePerRequestFilter implements PriorityOrdered {

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        UserTraceCollector.registerRequest();
        String ip = ClientUtils.getIp(request);
        if (StringUtils.isBlank(ip)) {
            log.warn("[{}] Unable to retrieve user IP address. Request URI: {}, Method: {}, Session ID: {}",
                    TraceTagConstants.USER_TRACE,
                    request.getRequestURI(),
                    request.getMethod(),
                    request.getRequestedSessionId());
        } else {
            log.info("[{}] get ip success. Request URI: {}, Method: {}, ip: {}",
                    TraceTagConstants.USER_TRACE, request.getRequestURI(), request.getMethod(), ip);
            UserTraceCollector.registerUserIp(ip);
        }
        filterChain.doFilter(request, response);
    }
}
