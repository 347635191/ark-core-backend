package org.best.statistics.gateway.controller;

import lombok.AllArgsConstructor;
import org.best.statistics.domain.menu.entity.MenuItem;
import org.best.statistics.domain.menu.service.MenuItemService;
import org.best.statistics.gateway.rest.ResultVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class MenuItemController {
    private final MenuItemService menuItemService;

    @RequestMapping("/menu/query")
    public ResultVo<List<MenuItem>> queryStat() {
        return ResultVo.success(menuItemService.queryTree());
    }
}
