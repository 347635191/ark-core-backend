package org.best.statistics.domain.user.endpoint;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.best.statistics.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDB {
    private static final IndexedCollection<User> CACHE;

    static {
        CACHE = new ConcurrentIndexedCollection<>();
        //添加索引
        CACHE.addIndex(UniqueIndex.onAttribute(Index.ID));
        CACHE.addIndex(HashIndex.onAttribute(Index.STATE));
    }

    private static final class Index {
        //创建属性的访问者对象
        private static final Attribute<User, Long> ID = attribute("ID", User::getId);
        private static final Attribute<User, Integer> STATE = attribute("STATE", User::getState);
    }

    public static void upset(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return;
        }
        List<Long> ids = users.stream().map(User::getId).collect(Collectors.toList());
        List<User> toDel;
        try (ResultSet<User> result = CACHE.retrieve(in(Index.ID, ids))) {
            toDel = result.stream().collect(Collectors.toList());
        }
        CACHE.update(toDel, users);
    }

    public static List<User> queryAll() {
        try (ResultSet<User> result = CACHE.retrieve(all(User.class))) {
            return result.stream().collect(Collectors.toList());
        }
    }

    public static User queryById(Long id) {
        try (ResultSet<User> result = CACHE.retrieve(equal(Index.ID, id))) {
            return result.uniqueResult();
        }
    }

    public static List<User> queryAllActiveUser() {
        try (ResultSet<User> result = CACHE.retrieve(equal(Index.STATE, 1))) {
            return result.stream().collect(Collectors.toList());
        }
    }
}
