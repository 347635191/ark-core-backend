package org.best.statistics.domain.stat.team.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamBattleRecord {
    private Long id;
    /**
     * 对方团名
     */
    private String enemyTeamName;
    /**
     * 团战日期
     */
    private String battleDate;
    /**
     * 状态 0-失败，1-胜率
     */
    private Integer state;
    /**
     * 团排名
     */
    private Integer teamRank;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
