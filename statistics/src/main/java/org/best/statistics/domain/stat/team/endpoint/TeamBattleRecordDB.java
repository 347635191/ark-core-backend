package org.best.statistics.domain.stat.team.endpoint;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.unique.UniqueIndex;
import com.googlecode.cqengine.resultset.ResultSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.best.statistics.domain.stat.team.entity.TeamBattleRecord;

import java.util.List;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamBattleRecordDB {
    private static final IndexedCollection<TeamBattleRecord> CACHE;

    static {
        CACHE = new ConcurrentIndexedCollection<>();
        //添加索引
        CACHE.addIndex(UniqueIndex.onAttribute(Index.ID));
        //添加索引
        CACHE.addIndex(RadixTreeIndex.onAttribute(Index.BATTLE_DATE));
    }

    private static final class Index {
        //创建属性的访问者对象
        private static final Attribute<TeamBattleRecord, Long> ID = attribute("ID", TeamBattleRecord::getId);
        private static final Attribute<TeamBattleRecord, String> BATTLE_DATE = attribute("BATTLE_DATE", TeamBattleRecord::getBattleDate);
    }

    public static void upset(List<TeamBattleRecord> teamBattleRecords) {
        if (CollectionUtils.isEmpty(teamBattleRecords)) {
            return;
        }
        List<Long> ids = teamBattleRecords.stream().map(TeamBattleRecord::getId).collect(Collectors.toList());
        List<TeamBattleRecord> toDel;
        try (ResultSet<TeamBattleRecord> result = CACHE.retrieve(in(Index.ID, ids))) {
            toDel = result.stream().collect(Collectors.toList());
        }
        CACHE.update(toDel, teamBattleRecords);
    }

    public static List<TeamBattleRecord> queryAll() {
        try (ResultSet<TeamBattleRecord> result = CACHE.retrieve(all(TeamBattleRecord.class))) {
            return result.stream().collect(Collectors.toList());
        }
    }

    public static List<TeamBattleRecord> queryByMonth(String month) {
        try (ResultSet<TeamBattleRecord> result = CACHE.retrieve(startsWith(Index.BATTLE_DATE, month))) {
            return result.stream().collect(Collectors.toList());
        }
    }

    public static TeamBattleRecord queryById(Long id) {
        try (ResultSet<TeamBattleRecord> result = CACHE.retrieve(equal(Index.ID, id))) {
            return result.uniqueResult();
        }
    }
}
