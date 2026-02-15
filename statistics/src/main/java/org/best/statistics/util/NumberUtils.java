package org.best.statistics.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtils {
    /**
     * 取出字符串内的数字和空格
     */
    public static String keepOnlyDigitsAndSpaces(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || Character.isWhitespace(c) || c == '.') {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static BigDecimal parseBigDecimal(String s) {
        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal getOrDefault(List<BigDecimal> list, int index, BigDecimal defaultValue) {
        try {
            return list.get(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
