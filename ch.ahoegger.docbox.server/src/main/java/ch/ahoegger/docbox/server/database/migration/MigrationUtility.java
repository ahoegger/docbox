package ch.ahoegger.docbox.server.database.migration;

import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.sys.tables.Syscolumns;
import org.ch.ahoegger.docbox.server.or.sys.tables.Systables;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.Record2;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * <h3>{@link MigrationUtility}</h3>
 *
 * @author aho
 */
public class MigrationUtility {

  public static ITableDescription getTableDesription(String tableName) {
    TableDescription desc = new TableDescription();
    Systables systables = Systables.SYSTABLES;
    Record2<String, String> fetchOne = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(systables.TABLEID, systables.TABLENAME)
        .from(systables)
        .where(systables.TABLETYPE.eq("T"))
        .and(systables.TABLENAME.eq(tableName))
        .fetchOne();
    if (fetchOne == null) {
      return null;
    }
    return fetchOne.map(rec -> fillCollumns(desc.withTableId(rec.get(systables.TABLEID)).withTableName(rec.get(systables.TABLENAME))));

  }

  private static ITableDescription fillCollumns(TableDescription desc) {
    Syscolumns syscolumns = Syscolumns.SYSCOLUMNS;
    Systables systables = Systables.SYSTABLES;
    desc.withColumns(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(syscolumns.COLUMNNAME)
        .from(syscolumns)
        .leftOuterJoin(systables).on(syscolumns.REFERENCEID.eq(systables.TABLEID))
        .where(systables.TABLENAME.eq(desc.getTableName()))
        .fetch()
        .map(rec -> rec.get(syscolumns.COLUMNNAME))
        .stream().collect(Collectors.toSet()));
    return desc;
  }
}
