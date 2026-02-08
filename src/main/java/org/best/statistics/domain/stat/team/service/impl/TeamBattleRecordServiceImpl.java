package org.best.statistics.domain.stat.team.service.impl;

import org.best.statistics.domain.stat.team.endpoint.TeamBattleRecordDB;
import org.best.statistics.domain.stat.team.entity.TeamBattleRecord;
import org.best.statistics.domain.stat.team.service.TeamBattleRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamBattleRecordServiceImpl implements TeamBattleRecordService {
    @Override
    public List<TeamBattleRecord> query() {
        return TeamBattleRecordDB.queryAll();
    }

    @Override
    public List<TeamBattleRecord> queryByMonth(String month) {
        return TeamBattleRecordDB.queryByMonth(month);
    }
}
