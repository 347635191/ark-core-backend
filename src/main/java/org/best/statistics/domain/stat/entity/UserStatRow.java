package org.best.statistics.domain.stat.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.best.statistics.domain.stat.score.entity.UserBattleScore;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserStatRow {
    private Long userId;
    private String userName;
    private List<UserBattleScore> scores;
}
