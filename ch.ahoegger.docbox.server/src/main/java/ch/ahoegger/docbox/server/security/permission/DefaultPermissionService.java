package ch.ahoegger.docbox.server.security.permission;

import java.util.HashSet;
import java.util.Set;

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

  public Set<UserPermission> getDefaultPermissions() {
    Set<UserPermission> result = new HashSet<UserPermission>();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(USERNAME, PERMISSION)).append(" FROM ").append(TABLE_NAME);
    Object[][] rawResult = SQL.select(statementBuilder.toString());
    for (Object[] up : rawResult) {
      result.add(new UserPermission(TypeCastUtility.castValue(up[0], String.class), TypeCastUtility.castValue(up[1], Integer.class)));
    }
    return result;
  }
}
