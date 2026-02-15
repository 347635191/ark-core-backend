package org.best.statistics.util;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientUtils {
    private static final List<String> IP_HEADERS = Lists.newArrayList(
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR",
            "X-Real-IP");

    public static String getIp(HttpServletRequest request) {
        // TODO to remove
        Map<String, String> logData = new HashMap<>();
        //判断是否为WebSocket握手请求
        String upgrade = request.getHeader("Upgrade");
        if ("websocket".equals(upgrade)) {
            logData.put("isWs", "true");
        } else {
            logData.put("isWs", "false");
        }
        logData.put("x-forwarded-for", request.getHeader("x-forwarded-for"));
        logData.put("Proxy-Client-IP", request.getHeader("Proxy-Client-IP"));
        logData.put("WL-Proxy-Client-IP", request.getHeader("WL-Proxy-Client-IP"));
        logData.put("HTTP_CLIENT_IP", request.getHeader("HTTP_CLIENT_IP"));
        logData.put("HTTP_X_FORWARDED_FOR", request.getHeader("HTTP_X_FORWARDED_FOR"));
        logData.put("X-Real-IP", request.getHeader("X-Real-IP"));
        logData.put("remoteAddr", request.getRemoteAddr());
        log.info("location, map:{}", logData);
        return Stream.concat(IP_HEADERS.stream().map(request::getHeader), Stream.of(request.getRemoteAddr()))
                .filter(ip -> StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip))
                .findFirst()
                .map(ip -> ip.split(",")[0])
                .map(String::trim).orElse(StringUtils.EMPTY);
    }
}
