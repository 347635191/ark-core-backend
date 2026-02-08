package org.best.statistics.domain.stat.team.endpoint;

import lombok.RequiredArgsConstructor;
import org.best.statistics.api.TraceTagConstants;
import org.best.statistics.domain.facade.template.AbstractDBInitRunner;
import org.best.statistics.domain.stat.team.entity.TeamBattleRecord;
import org.best.statistics.domain.stat.team.mapper.TeamBattleRecordMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TeamBattleRecordLoader extends AbstractDBInitRunner {
    private final TeamBattleRecordMapper teamBattleRecordMapper;

    @Override
    protected Integer init() {
        List<TeamBattleRecord> teamBattleRecords = teamBattleRecordMapper.queryAll();
        TeamBattleRecordDB.upset(teamBattleRecords);
        return teamBattleRecords.size();
    }

    @Override
    protected String tag() {
        return TraceTagConstants.BATTLE_SCORE_DB;
    }
}
