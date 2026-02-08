package org.best.statistics.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Pattern {
        public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE_DOT_PATTER = "yyyy.MM.dd";
        public static final String DATE_PATTER = "yyyy-MM-dd";
    }

    static final Map<String, DateTimeFormatter> FORMATTER = new ConcurrentHashMap<>();
    static final Map<String, DateFormat> DATE_FORMATTER = new ConcurrentHashMap<>();

    public static DateTimeFormatter formatter(@NonNull String pattern) {
        return FORMATTER.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
    }

    public static DateFormat dateFormatter(@NonNull String pattern) {
        return DATE_FORMATTER.computeIfAbsent(pattern, SimpleDateFormat::new);
    }

    public static Duration endOfDayDuration() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.with(LocalTime.MAX);
        return Duration.between(now, endOfDay);
    }

    /**
     * 日期->字符串
     */
    public static String format(LocalDate date, String toPattern) {
        DateFormat formatter = dateFormatter(toPattern);
        return formatter.format(date);
    }

    /**
     * 字符串->字符串
     */
    public static String dateConvert(String date,String fromPatter, String toPattern) {
        DateTimeFormatter fromFormatter = formatter(fromPatter);
        LocalDate from = LocalDate.parse(date, fromFormatter);
        return from.format(formatter(toPattern));
    }
}
