package org.best.statistics.domain.stat.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamRankEcharts {
    private List<String> dateX;
    private List<Integer> rankY;
}
