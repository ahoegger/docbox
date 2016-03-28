package ch.ahoegger.docbox.server.security.permission;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.security.permission.IDefaultPermissionTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link DefaultPermissionService}</h3>
 *
 * @author Andreas Hoegger
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

  /**
   * @param username
   * @param permission
   */
  public void updateDefaultPermission(String username, Integer permission) {
    Assertions.assertNotNull(username);

    deleteByUsername(username);
    createDefaultPermission(username, permission);
  }

  /**
   * @param value
   * @param value2
   */
  public void createDefaultPermission(String username, Integer permission) {
    Assertions.assertNotNull(username);

    if (permission != null) {
      StringBuilder statementBuilder = new StringBuilder();
      statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
      statementBuilder.append(SqlFramentBuilder.columns(USERNAME, PERMISSION)).append(" ) VALUES (");
      statementBuilder.append(":username, :defaultPermission");
      statementBuilder.append(")");

      SQL.insert(statementBuilder.toString(),
          new NVPair("username", username),
          new NVPair("defaultPermission", permission));

      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
    }

  }

  public void deleteByUsername(String username) {
    Assertions.assertNotNull(username);

    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(USERNAME).append(" = :username");
    SQL.delete(statementBuilder.toString(), new NVPair("username", username));

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

  }
}
