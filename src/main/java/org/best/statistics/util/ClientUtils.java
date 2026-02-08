package org.best.statistics.util;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientUtils {
    private static final Set<String> IP_HEADERS = Sets.newHashSet(
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR");

    public static String getIp(HttpServletRequest request) {
        return Stream.concat(IP_HEADERS.stream().map(request::getHeader), Stream.of(request.getRemoteAddr()))
                .filter(ip -> StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip))
                .findFirst()
                .map(ip -> ip.split(",")[0])
                .map(String::trim).orElse(StringUtils.EMPTY);
    }
}
