package org.best.statistics.domain.stat.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.best.statistics.api.TableConstants;
import org.best.statistics.domain.stat.entity.TeamRankEcharts;
import org.best.statistics.domain.stat.entity.UserStatRow;
import org.best.statistics.domain.stat.score.entity.UserBattleScore;
import org.best.statistics.domain.stat.score.service.BattleScoreService;
import org.best.statistics.domain.stat.service.StatService;
import org.best.statistics.domain.stat.team.entity.TeamBattleRecord;
import org.best.statistics.domain.stat.team.service.TeamBattleRecordService;
import org.best.statistics.domain.stat.vo.TeamRecordStatVo;
import org.best.statistics.domain.stat.vo.UserRecordStatVo;
import org.best.statistics.domain.user.entity.User;
import org.best.statistics.domain.user.service.UserService;
import org.best.statistics.util.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final UserService userService;
    private final BattleScoreService battleScoreService;
    private final TeamBattleRecordService teamBattleRecordService;

    @Override
    public UserRecordStatVo queryUserStat() {
        List<User> users = userService.queryActiveUsers();
        List<Long> ids = users.stream().map(User::getId).collect(Collectors.toList());
        List<UserBattleScore> userBattleScores = battleScoreService.queryByIds(ids).stream()
                .sorted(Comparator.comparing(UserBattleScore::getBattleDate)).collect(Collectors.toList());

        //表头
        List<String> statHead = Lists.newArrayList(TableConstants.TEAM_MEMBER);
        statHead.addAll(userBattleScores.stream().map(UserBattleScore::getBattleDate)
                .map(battleDate -> DateUtils.dateConvert(battleDate, DateUtils.Pattern.DATE_PATTER,
                        DateUtils.Pattern.DATE_DOT_PATTER)).distinct()
                .collect(Collectors.toList()));

        //用户战绩
        Map<Long, List<UserBattleScore>> scoreMap = userBattleScores.stream()
                .collect(Collectors.groupingBy(UserBattleScore::getUserId));
        List<UserStatRow> userStatRows = new ArrayList<>();
        scoreMap.forEach((userId, scores) -> {
            UserStatRow userStatRow = new UserStatRow();
            userStatRow.setUserId(userId);
            userStatRow.setUserName(scores.get(0).getName());
            userStatRow.setScores(scores);
            userStatRows.add(userStatRow);
        });

        //构建
        UserRecordStatVo userRecordStatVo = new UserRecordStatVo();
        userRecordStatVo.setStatHead(statHead);
        userRecordStatVo.setUserStatRows(userStatRows);
        return userRecordStatVo;
    }

    @Override
    public TeamRecordStatVo queryTeamStat() {
        //团战绩
        List<TeamBattleRecord> teamBattleRecords = teamBattleRecordService.query().stream()
                .sorted(Comparator.comparing(TeamBattleRecord::getBattleDate)).collect(Collectors.toList());

        //表头
        List<String> statHead = Lists.newArrayList(TableConstants.TEAM);
        statHead.addAll(teamBattleRecords.stream().map(TeamBattleRecord::getBattleDate)
                .map(battleDate -> DateUtils.dateConvert(battleDate, DateUtils.Pattern.DATE_PATTER,
                        DateUtils.Pattern.DATE_DOT_PATTER)).distinct().collect(Collectors.toList()));

        TeamRecordStatVo teamRecordStatVo = new TeamRecordStatVo();
        teamRecordStatVo.setStatHead(statHead);
        teamRecordStatVo.setTeamColumns(TableConstants.TEAM_COLUMNS);
        teamRecordStatVo.setTeamBattleRows(teamBattleRecords);
        return teamRecordStatVo;
    }

    @Override
    public TeamRankEcharts queryTeamRank(String month) {
        if (StringUtils.isEmpty(month)) {
            return new TeamRankEcharts();
        }
        List<TeamBattleRecord> teamBattleRecords = teamBattleRecordService.queryByMonth(month);
        List<String> dateX = new ArrayList<>();
        List<Integer> rankY = new ArrayList<>();
        teamBattleRecords.stream().sorted(Comparator.comparing(TeamBattleRecord::getBattleDate)).forEach(teamBattleRecord -> {
            dateX.add(teamBattleRecord.getBattleDate());
            rankY.add(teamBattleRecord.getTeamRank());
        });
        //构建
        TeamRankEcharts echarts = new TeamRankEcharts();
        echarts.setDateX(dateX);
        echarts.setRankY(rankY);
        return echarts;
    }
}
