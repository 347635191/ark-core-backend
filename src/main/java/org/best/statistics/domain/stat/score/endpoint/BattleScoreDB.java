package org.best.statistics.domain.stat.score.endpoint;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.best.statistics.domain.stat.score.entity.UserBattleScore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BattleScoreDB {
    private static final IndexedCollection<UserBattleScore> CACHE;

    static {
        CACHE = new ConcurrentIndexedCollection<>();
        //添加索引
        CACHE.addIndex(UniqueIndex.onAttribute(Index.ID));
        CACHE.addIndex(HashIndex.onAttribute(Index.USER_ID));
    }

    private static final class Index {
        //创建属性的访问者对象
        private static final Attribute<UserBattleScore, Long> ID = attribute("ID", UserBattleScore::getId);
        private static final Attribute<UserBattleScore, Long> USER_ID = attribute("USER_ID",
                UserBattleScore::getUserId);
    }

    public static void upset(List<UserBattleScore> userBattleScores) {
        if (CollectionUtils.isEmpty(userBattleScores)) {
            return;
        }
        List<Long> ids = userBattleScores.stream().map(UserBattleScore::getId).collect(Collectors.toList());
        List<UserBattleScore> toDel;
        try (ResultSet<UserBattleScore> result = CACHE.retrieve(in(Index.ID, ids))) {
            toDel = result.stream().collect(Collectors.toList());
        }
        CACHE.update(toDel, userBattleScores);
    }

    public static List<UserBattleScore> queryAll() {
        try (ResultSet<UserBattleScore> result = CACHE.retrieve(all(UserBattleScore.class))) {
            return result.stream().collect(Collectors.toList());
        }
    }

    public static UserBattleScore queryById(Long id) {
        try (ResultSet<UserBattleScore> result = CACHE.retrieve(equal(Index.ID, id))) {
            return result.uniqueResult();
        }
    }

    public static List<UserBattleScore> queryByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        try (ResultSet<UserBattleScore> result = CACHE.retrieve(in(Index.USER_ID, ids))) {
            return result.stream().collect(Collectors.toList());
        }
    }
}
