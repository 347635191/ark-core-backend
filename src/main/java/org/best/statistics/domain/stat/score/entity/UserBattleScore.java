package org.best.statistics.domain.stat.score.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBattleScore {
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 团战日期
     */
    private String battleDate;
    /**
     * 出刀得分 胜+1，平+0，负-1
     */
    private Integer score;
    /**
     * 是否出完刀，1-未出刀，2-未出完刀，3-满刀
     */
    private Integer completeAttack;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
