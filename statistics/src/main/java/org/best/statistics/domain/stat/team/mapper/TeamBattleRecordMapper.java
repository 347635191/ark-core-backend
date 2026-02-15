package org.best.statistics.domain.stat.team.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.best.statistics.domain.stat.team.entity.TeamBattleRecord;

import java.util.List;

@Mapper
public interface TeamBattleRecordMapper {
    List<TeamBattleRecord> queryAll();
}