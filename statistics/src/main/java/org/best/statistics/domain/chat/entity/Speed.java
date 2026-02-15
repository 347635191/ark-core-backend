package org.best.statistics.domain.chat.entity;

import lombok.Data;
import org.best.statistics.constants.ChatConstants;
import org.best.statistics.util.NumberUtils;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Speed {
    /**
     * 己方速度
     */
    private BigDecimal ourSpeed;
    /**
     * 己方行动值
     */
    private BigDecimal ourActiveValue;
    /**
     * 已方乱速值
     */
    private BigDecimal ourMessValue;
    /**
     * 对方行动值
     */
    private BigDecimal enemyActiveValue;
    /**
     * 对方乱速值
     */
    private BigDecimal enemyMessValue;
    /**
     * 对方速度
     */
    private BigDecimal enemySpeed;

    public static Speed convert(List<BigDecimal> activeValueCollect, List<BigDecimal> messValueCollect,
                                List<BigDecimal> ourSpeedCollect) {
        Speed speed = new Speed();
        if (activeValueCollect.size() < 2 || ourSpeedCollect.isEmpty()) {
            return null;
        }
        speed.setOurActiveValue(activeValueCollect.get(0));
        speed.setEnemyActiveValue(activeValueCollect.get(1));
        speed.setOurSpeed(ourSpeedCollect.get(0));
        speed.setOurMessValue(NumberUtils.getOrDefault(messValueCollect, 0, BigDecimal.ZERO));
        speed.setEnemyMessValue(NumberUtils.getOrDefault(messValueCollect, 1, BigDecimal.ZERO));
        return speed;
    }

    /**
     * reply
     */
    public String convert() {
        return String.format(ChatConstants.CALCULATE_SPEED_TEMPLATE,
                ourSpeed.stripTrailingZeros().toPlainString(),
                ourActiveValue.stripTrailingZeros().toPlainString(),
                ourMessValue.stripTrailingZeros().toPlainString(),
                enemyActiveValue.stripTrailingZeros().toPlainString(),
                enemyMessValue.stripTrailingZeros().toPlainString(),
                enemySpeed.stripTrailingZeros().toPlainString());
    }
}
