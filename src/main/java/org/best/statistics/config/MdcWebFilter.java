package org.best.statistics.config;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.best.statistics.util.TraceUtils;
import org.slf4j.MDC;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MdcWebFilter extends OncePerRequestFilter implements PriorityOrdered {

    @Override
    public int getOrder() {
        return -2;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = StringUtils.defaultIfBlank(request.getHeader(TraceUtils.KEY), TraceUtils.create());
            MDC.put(TraceUtils.KEY, traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
