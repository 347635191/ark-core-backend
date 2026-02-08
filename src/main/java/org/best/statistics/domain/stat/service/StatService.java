package org.best.statistics.domain.stat.service;

import org.best.statistics.domain.stat.entity.TeamRankEcharts;
import org.best.statistics.domain.stat.vo.TeamRecordStatVo;
import org.best.statistics.domain.stat.vo.UserRecordStatVo;

public interface StatService {
    /**
     * 查询团员战绩
     */
    UserRecordStatVo queryUserStat();

    /**
     * 查询团队战绩
     */
    TeamRecordStatVo queryTeamStat();

    /**
     * 查询指定月份的团排名
     */
    TeamRankEcharts queryTeamRank(String month);
}
