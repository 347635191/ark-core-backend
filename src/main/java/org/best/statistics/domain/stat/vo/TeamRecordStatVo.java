package org.best.statistics.domain.stat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.best.statistics.domain.stat.team.entity.TeamBattleRecord;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamRecordStatVo {
    private List<String> statHead;
    private List<TeamBattleRecord> teamBattleRows;
    private List<String> teamColumns;
}
