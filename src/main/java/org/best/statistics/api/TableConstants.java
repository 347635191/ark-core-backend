package org.best.statistics.api;

import java.util.Arrays;
import java.util.List;

public interface TableConstants {
    String TEAM_MEMBER = "团员";
    String TEAM = "团队";
    String ENEMY_TEAM_NAME = "对方团名";
    String OUT_TEAM_STATS = "本方战绩";
    String RANK = "战后排名";
    List<String> TEAM_COLUMNS = Arrays.asList(ENEMY_TEAM_NAME, OUT_TEAM_STATS, RANK);
}
