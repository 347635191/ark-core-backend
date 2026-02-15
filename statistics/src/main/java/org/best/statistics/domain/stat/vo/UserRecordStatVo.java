package org.best.statistics.domain.stat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.best.statistics.domain.stat.entity.UserStatRow;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRecordStatVo {
    private List<String> statHead;
    private List<UserStatRow> userStatRows;
}
