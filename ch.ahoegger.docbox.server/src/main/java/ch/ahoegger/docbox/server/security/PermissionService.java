package ch.ahoegger.docbox.server.security;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.security.permission.IPermissionService;
import ch.ahoegger.docbox.shared.security.permission.IPermissionTable;

/**
 * <h3>{@link PermissionService}</h3>
 *
 * @author aho
 */
public class PermissionService implements IPermissionService, IPermissionTable {

  @Override
  public boolean hasReadAccess(String userId, Long entityId) {
    List<Integer> permission = new ArrayList<Integer>();
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(PERMISSION, USERNAME, ENTITY_NR)).append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE 1 = 1");
    statementBuilder.append(" AND ").append(USERNAME).append(" LIKE :userId");
    statementBuilder.append(" AND ").append(ENTITY_NR).append(" = :entityId");
//    statementBuilder.append(" INTO :permission");
//    SQL.selectInto(statementBuilder.toString(), new NVPair("userId", userId), new NVPair("entityId", entityId), new NVPair("permission", permission));
    Object[][] result = SQL.select(statementBuilder.toString(), new NVPair("userId", userId), new NVPair("entityId", entityId), new NVPair("permission", permission));
    if (result.length > 0) {
      return ((Long) result[0][0] >= PERMISSION_READ);
    }
//    if (permission.size() > 0) {
//      return (permission.get(0).shortValue() & PERMISSION_READ) != 0;
//    }
    return false;
  }

  @Override
  public boolean hasWriteAccess(Long entityId) {
    return false;
  }
}
