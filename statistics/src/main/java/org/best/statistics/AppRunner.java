package org.best.statistics;

import org.best.statistics.util.TraceUtils;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AppRunner {
    static {
        MDC.put(TraceUtils.KEY, TraceUtils.create());
    }

    public static void main(String[] args) {
        SpringApplication.run(AppRunner.class, args);
    }
}