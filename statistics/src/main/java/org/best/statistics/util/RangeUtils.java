package org.best.statistics.util;

import com.google.common.collect.Range;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.best.statistics.constants.TraceTagConstants;

import java.math.BigDecimal;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RangeUtils {
    private static final Range<BigDecimal> NONE = Range.open(new BigDecimal("-1"), BigDecimal.ZERO);

    public static Range<BigDecimal> toRange(String scope) {
        try {
            Boolean leftOpen = null;
            Boolean rightOpen = null;
            BigDecimal leftNum;
            BigDecimal rightNum;
            if (scope.startsWith("(")) {
                leftOpen = true;
            }
            if (scope.startsWith("[")) {
                leftOpen = false;
            }
            if (scope.endsWith(")")) {
                rightOpen = true;
            }
            if (scope.endsWith("]")) {
                rightOpen = false;
            }
            String[] split = scope.substring(1, scope.length() - 1).split(",");
            leftNum = new BigDecimal(split[0]);
            rightNum = new BigDecimal(split[1]);
            if (Boolean.TRUE.equals(leftOpen)) {
                return Boolean.TRUE.equals(rightOpen) ?
                        Range.open(leftNum, rightNum) : Range.openClosed(leftNum, rightNum);
            }
            if (Boolean.FALSE.equals(leftOpen)) {
                return Boolean.TRUE.equals(rightOpen) ?
                        Range.closedOpen(leftNum, rightNum) : Range.closed(leftNum, rightNum);
            }
        } catch (Exception e) {
            log.error("[{}]convert to range error, scope:{}", TraceTagConstants.RANGE_UTILS, scope);
            return NONE;
        }
        return NONE;
    }
}
