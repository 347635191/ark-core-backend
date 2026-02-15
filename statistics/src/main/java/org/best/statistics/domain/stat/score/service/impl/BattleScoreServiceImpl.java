package org.best.statistics.domain.stat.score.service.impl;

import org.best.statistics.domain.stat.score.endpoint.BattleScoreDB;
import org.best.statistics.domain.stat.score.entity.UserBattleScore;
import org.best.statistics.domain.stat.score.service.BattleScoreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BattleScoreServiceImpl implements BattleScoreService {
    @Override
    public List<UserBattleScore> queryByIds(List<Long> ids) {
        return BattleScoreDB.queryByIds(ids);
    }
}
