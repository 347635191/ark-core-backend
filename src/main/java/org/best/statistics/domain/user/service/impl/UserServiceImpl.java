
package org.best.statistics.domain.user.service.impl;

import org.best.statistics.domain.user.endpoint.UserDB;
import org.best.statistics.domain.user.entity.User;
import org.best.statistics.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    /**
     * 查询所有在团的用户id
     */
    @Override
    public List<User> queryActiveUsers() {
        return UserDB.queryAllActiveUser();
    }
}
