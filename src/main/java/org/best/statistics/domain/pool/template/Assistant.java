package org.best.statistics.domain.pool.template;

/**
 * 辅助执行接口
 */
public interface Assistant {
    /**
     * 获取组件名
     */
    String getName();

    /**
     * 是否为非正常状态
     */
    boolean isAbnormal();
}
