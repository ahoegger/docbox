package ch.ahoegger.docbox.server.database.migration;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;

/**
 * <h3>{@link MigrationUtility}</h3>
 *
 * @author aho
 */
public class MigrationUtility {

  public static ITableDescription getTableDesription(String tableName) {
    TableDescription desc = new TableDescription();
    Object[][] result = SQL.select("SELECT TABLEID, TABLENAME FROM sys.systables WHERE TABLETYPE = :{tableType} AND TABLENAME = :{tableName}", new NVPair("tableType", "T"), new NVPair("tableName", tableName));
    if (result.length == 1) {
      return fillCollumns(desc.withTableId(TypeCastUtility.castValue(result[0][0], String.class)).withTableName(TypeCastUtility.castValue(result[0][1], String.class)));
    }
    else {
      return null;
    }
  }

  private static ITableDescription fillCollumns(TableDescription desc) {
    Object[][] result = SQL.select("SELECT COLUMNNAME FROM  sys.syscolumns WHERE  :{tableId} = REFERENCEID", new NVPair("tableId", desc.getTableId()));
    return desc.withColumns(Arrays.stream(result).map(row -> TypeCastUtility.castValue(row[0], String.class)).collect(Collectors.toSet()));
  }
}
