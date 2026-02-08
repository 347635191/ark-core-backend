
package org.best.statistics.domain.menu.service.impl;

import org.best.statistics.domain.menu.endpoint.MenuItemDB;
import org.best.statistics.domain.menu.entity.MenuItem;
import org.best.statistics.domain.menu.service.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {
    /**
     * 以树型结构查询菜单
     */
    @Override
    public List<MenuItem> queryTree() {
        return MenuItemDB.queryTree();
    }
}
