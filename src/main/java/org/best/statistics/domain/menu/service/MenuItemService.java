package org.best.statistics.domain.menu.service;

import org.best.statistics.domain.menu.entity.MenuItem;

import java.util.List;

public interface MenuItemService {
    /**
     * 以树型结构查询菜单
     */
    List<MenuItem> queryTree();
}
