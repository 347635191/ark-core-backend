package org.best.statistics.domain.menu.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItem {
    private Long id;
    /**
     * 菜单名
     */
    private String name;
    private Long parentId;
    private String icon;
    private String frontPath;
    private Integer level;
    /**
     * 状态 0-删除，1-正常
     */
    private Integer state;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<MenuItem> childList;
}

