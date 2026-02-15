package org.best.statistics.domain.menu.endpoint;

import lombok.RequiredArgsConstructor;
import org.best.statistics.constants.TraceTagConstants;
import org.best.statistics.domain.facade.template.AbstractDBInitRunner;
import org.best.statistics.domain.menu.entity.MenuItem;
import org.best.statistics.domain.menu.mapper.MenuItemMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuItemLoader extends AbstractDBInitRunner {
    private final MenuItemMapper menuItemMapper;

    @Override
    protected Integer init() {
        List<MenuItem> menuItems = menuItemMapper.queryAll();
        MenuItemDB.init(menuItems);
        return menuItems.size();
    }

    @Override
    protected String tag() {
        return TraceTagConstants.MENU_DB;
    }
}
