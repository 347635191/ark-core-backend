package org.best.statistics.domain.menu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.best.statistics.domain.menu.entity.MenuItem;

import java.util.List;

@Mapper
public interface MenuItemMapper {
    List<MenuItem> queryAll();
}