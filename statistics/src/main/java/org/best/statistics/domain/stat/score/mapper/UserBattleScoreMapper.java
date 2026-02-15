package org.best.statistics.domain.stat.score.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.best.statistics.domain.stat.score.entity.UserBattleScore;

import java.util.List;

@Mapper
public interface UserBattleScoreMapper {
    List<UserBattleScore> queryAll();
}