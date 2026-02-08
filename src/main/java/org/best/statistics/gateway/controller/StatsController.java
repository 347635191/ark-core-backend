package org.best.statistics.gateway.controller;

import lombok.AllArgsConstructor;
import org.best.statistics.domain.stat.entity.TeamRankEcharts;
import org.best.statistics.domain.stat.service.StatService;
import org.best.statistics.domain.stat.vo.TeamRecordStatVo;
import org.best.statistics.domain.stat.vo.UserRecordStatVo;
import org.best.statistics.gateway.rest.ResultVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class StatsController {
    private final StatService statService;

    @RequestMapping("/stat/user/query")
    public ResultVo<UserRecordStatVo> queryUserStat() {
        return ResultVo.success(statService.queryUserStat());
    }

    @RequestMapping("/stat/team/query")
    public ResultVo<TeamRecordStatVo> queryTeamStat() {
        return ResultVo.success(statService.queryTeamStat());
    }

    @RequestMapping("/echarts/team/rank/query")
    public ResultVo<TeamRankEcharts> queryTeamRank(String month) {
        return ResultVo.success(statService.queryTeamRank(month));
    }
}
