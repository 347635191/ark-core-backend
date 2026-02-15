package org.best.statistics.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.best.statistics.domain.user.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> queryAll();
}