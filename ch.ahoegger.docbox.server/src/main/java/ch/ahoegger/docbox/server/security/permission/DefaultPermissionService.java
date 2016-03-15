package ch.ahoegger.docbox.server.security.permission;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.security.permission.IDefaultPermissionTable;

/**
 * <h3>{@link DefaultPermissionService}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public class DefaultPermissionService implements IDefaultPermissionTable {

  public Map<String, Integer> getDefaultPermissions() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(USERNAME, PERMISSION)).append(" FROM ").append(TABLE_NAME);
    Object[][] rawResult = SQL.select(statementBuilder.toString());
    return Arrays.stream(rawResult).collect(Collectors.toMap(
        row -> (String) TypeCastUtility.castValue(row[0], String.class),
        row -> (Integer) TypeCastUtility.castValue(row[1], Integer.class),
        (p1, p2) -> Math.max(p1, p2)));
  }
}
