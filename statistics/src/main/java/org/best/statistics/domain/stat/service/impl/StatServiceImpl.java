package org.best.statistics.domain.stat.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.best.statistics.constants.TableConstants;
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
import org.springframework.util.CollectionUtils;

import java.util.*;
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
            //之前未入团的团员补充虚拟战绩
            fillVirtualRecord(userId, users, scores, statHead);
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

    private void fillVirtualRecord(Long userId, List<User> users, List<UserBattleScore> scores, List<String> statHead) {
        int curScoreSize;
        String userName;
        int battleDateSize = statHead.size() - 1;
        if (CollectionUtils.isEmpty(scores)) {
            curScoreSize = 0;
            userName = users.stream().filter(user -> Objects.equals(userId, user.getId()))
                    .findAny().map(User::getName).orElse(null);
        } else {
            curScoreSize = scores.size();
            userName = scores.get(0).getName();
        }
        if (curScoreSize == battleDateSize) {
            return;
        }
        for (int i = battleDateSize - curScoreSize; i > 0; i--) {
            UserBattleScore virtual = new UserBattleScore();
            virtual.setUserId(userId);
            virtual.setName(userName);
            virtual.setScore(100);
            virtual.setBattleDate(statHead.get(i));
            virtual.setCompleteAttack(5);
            scores.add(0, virtual);
        }
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
