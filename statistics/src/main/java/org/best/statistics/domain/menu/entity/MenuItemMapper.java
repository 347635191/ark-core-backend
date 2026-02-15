package org.best.statistics.domain.menu.entity;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuItemMapper {
    MenuItemMapper INSTANCE = Mappers.getMapper(MenuItemMapper.class);
    MenuItem covert(MenuItem mp3T);
}
