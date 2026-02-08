package org.best.statistics.domain.user.service;

import org.best.statistics.domain.user.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 查询所有在团的用户id
     */
    List<User> queryActiveUsers();
}
