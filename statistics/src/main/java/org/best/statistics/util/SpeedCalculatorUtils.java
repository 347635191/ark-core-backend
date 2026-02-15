package org.best.statistics.util;

import com.google.common.collect.Range;
import org.apache.commons.lang3.StringUtils;
import org.best.statistics.domain.chat.entity.Speed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SpeedCalculatorUtils {
    /**
     * 测速
     *
     * @param text             测速文本
     * @param activeValueScope 行动值区间
     * @param messValueScope   乱速区间
     * @param speedScope       速度区间
     * @return 对方速度文本
     */
    public static Speed getSpeed(String text, String activeValueScope, String messValueScope, String speedScope) {
        text = NumberUtils.keepOnlyDigitsAndSpaces(text);
        String[] splitArr = text.split(StringUtils.SPACE);

        //行动值区间
        Range<BigDecimal> activeValueRange = RangeUtils.toRange(activeValueScope);
        List<BigDecimal> activeValueCollect = new ArrayList<>();
        //乱速区间
        Range<BigDecimal> messValueRange = RangeUtils.toRange(messValueScope);
        List<BigDecimal> messValueCollect = new ArrayList<>();
        //速度区间
        Range<BigDecimal> speedRange = RangeUtils.toRange(speedScope);
        List<BigDecimal> ourSpeedCollect = new ArrayList<>();
        Arrays.stream(splitArr).map(NumberUtils::parseBigDecimal).filter(Objects::nonNull)
                .map(num -> num.compareTo(BigDecimal.ONE) < 0 ? num.multiply(new BigDecimal("100")) : num)
                .forEach(num -> {
                    if (activeValueRange.contains(num)) {
                        activeValueCollect.add(num);
                        return;
                    }
                    if (messValueRange.contains(num)) {
                        messValueCollect.add(num);
                        return;
                    }
                    if (speedRange.contains(num)) {
                        ourSpeedCollect.add(num);
                    }
                });
        Speed speed = Speed.convert(activeValueCollect, messValueCollect, ourSpeedCollect);
        calculateSpeed(speed);
        return speed;
    }

    private static void calculateSpeed(Speed speed) {
        if (speed == null) {
            return;
        }
        // 净行动值 = 行动值 - 乱速值
        BigDecimal ourRealActiveValue = speed.getOurActiveValue().subtract(speed.getOurMessValue());
        BigDecimal enemyRealActiveValue = speed.getEnemyActiveValue().subtract(speed.getEnemyMessValue());
        // 对方速度 = 【已方速度 * 对方净行动值 / 乙方净行动值】向上取整
        BigDecimal enemySpeed = speed.getOurSpeed().multiply(enemyRealActiveValue)
                .divide(ourRealActiveValue, 0, RoundingMode.CEILING);
        speed.setEnemySpeed(enemySpeed);
    }
}
