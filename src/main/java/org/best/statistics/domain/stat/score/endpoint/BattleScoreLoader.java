package org.best.statistics.domain.stat.score.endpoint;

import lombok.RequiredArgsConstructor;
import org.best.statistics.api.TraceTagConstants;
import org.best.statistics.domain.facade.template.AbstractDBInitRunner;
import org.best.statistics.domain.stat.score.entity.UserBattleScore;
import org.best.statistics.domain.stat.score.mapper.UserBattleScoreMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BattleScoreLoader extends AbstractDBInitRunner {
    private final UserBattleScoreMapper battleScoreMapper;

    @Override
    protected Integer init() {
        List<UserBattleScore> battleScores = battleScoreMapper.queryAll();
        BattleScoreDB.upset(battleScores);
        return battleScores.size();
    }

    @Override
    protected String tag() {
        return TraceTagConstants.TEAM_BATTLE_RECORD_DB;
    }
}
