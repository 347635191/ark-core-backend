package org.best.statistics.domain.user.endpoint;

import lombok.RequiredArgsConstructor;
import org.best.statistics.constants.TraceTagConstants;
import org.best.statistics.domain.facade.template.AbstractDBInitRunner;
import org.best.statistics.domain.user.entity.User;
import org.best.statistics.domain.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserLoader extends AbstractDBInitRunner {
    private final UserMapper userMapper;

    @Override
    protected Integer init() {
        List<User> users = userMapper.queryAll();
        UserDB.upset(users);
        return users.size();
    }

    @Override
    protected String tag() {
        return TraceTagConstants.USER_DB;
    }
}
