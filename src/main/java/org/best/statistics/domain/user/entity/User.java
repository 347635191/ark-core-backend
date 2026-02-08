package org.best.statistics.domain.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 状态 0-退团，1-在团
     */
    private Integer state;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

