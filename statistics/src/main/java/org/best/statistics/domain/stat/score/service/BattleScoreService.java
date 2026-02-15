package org.best.statistics.domain.stat.score.service;

import org.best.statistics.domain.stat.score.entity.UserBattleScore;

import java.util.List;

public interface BattleScoreService {
    /**
     * 查询用户战绩
     */
    List<UserBattleScore> queryByIds(List<Long> ids);
}
