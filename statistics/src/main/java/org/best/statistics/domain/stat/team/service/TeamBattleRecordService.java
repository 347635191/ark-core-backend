package org.best.statistics.domain.stat.team.service;

import org.best.statistics.domain.stat.team.entity.TeamBattleRecord;

import java.util.List;

public interface TeamBattleRecordService {
    /**
     * 查询佣兵团战绩
     */
    List<TeamBattleRecord> query();

    /**
     * 查询指定月份的佣兵团战绩
     */
    List<TeamBattleRecord> queryByMonth(String month);
}
