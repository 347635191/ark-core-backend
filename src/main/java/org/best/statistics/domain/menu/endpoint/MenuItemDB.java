package org.best.statistics.domain.menu.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.best.statistics.domain.menu.entity.MenuItem;
import org.best.statistics.domain.menu.entity.MenuItemMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuItemDB {
    private static List<MenuItem> menuItemList = new ArrayList<>();
    private static List<MenuItem> menuItemTree = new ArrayList<>();

    public static void init(List<MenuItem> menuItems) {
        menuItemList = menuItems;

        List<MenuItem> menuItemsFromCopy = menuItems.stream().map(MenuItemMapper.INSTANCE::covert).collect(Collectors.toList());
        Map<Long, List<MenuItem>> childrenByParentId =
                menuItemsFromCopy.stream().filter(menuItem -> Objects.equals(menuItem.getState(), 1))
                        .filter(menuItem -> Objects.nonNull(menuItem.getParentId()))
                        .collect(Collectors.groupingBy(MenuItem::getParentId));
        menuItemsFromCopy.forEach(menuItem -> {
            Long id = menuItem.getId();
            if (childrenByParentId.containsKey(id)) {
                menuItem.setChildList(childrenByParentId.get(id));
            }
        });
        menuItemTree = menuItemsFromCopy.stream().filter(menuItem -> Objects.equals(menuItem.getLevel(), 1))
                .collect(Collectors.toList());
    }

    public static List<MenuItem> queryAll() {
        return menuItemList;
    }

    public static List<MenuItem> queryTree() {
        return menuItemTree;
    }
}
