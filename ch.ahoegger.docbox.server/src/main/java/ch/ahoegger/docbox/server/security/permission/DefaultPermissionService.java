package ch.ahoegger.docbox.server.security.permission;

import java.sql.Connection;
import java.util.Map;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.DefaultPermissionTable;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DefaultPermissionTableRecord;
import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.backup.IBackupService;

/**
 * <h3>{@link DefaultPermissionService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public class DefaultPermissionService {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultPermissionService.class);

  public Map<String, Integer> getDefaultPermissions() {

    DefaultPermissionTable t = DefaultPermissionTable.DEFAULT_PERMISSION_TABLE;
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(t.USERNAME, t.PERMISSION)
        .from(t)
        .fetch()
        .stream()
        .collect(Collectors.toMap(rec -> rec.get(t.USERNAME), rec -> rec.get(t.PERMISSION)));
  }

  /**
   * @param username
   * @param permission
   */
  public void updateDefaultPermission(String username, Integer permission) {
    deleteByUsername(username);
    if (permission != null) {
      createDefaultPermission(username, permission);
    }
  }

  /**
   * @param value
   * @param value2
   */
  public void createDefaultPermission(String username, Integer permission) {
    Assertions.assertNotNull(username);
    if (DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeInsert(toRecord(username, permission)) == 1) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
    }

  }

  public boolean deleteByUsername(String username) {
    Assertions.assertNotNull(username);
    DefaultPermissionTable t = DefaultPermissionTable.DEFAULT_PERMISSION_TABLE;
    DefaultPermissionTableRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(t, t.USERNAME.eq(username));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", username);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", username);
      return false;
    }

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return true;
  }

  public int insertRow(Connection connection, String username, int permission) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(toRecord(username, permission));
  }

  protected DefaultPermissionTableRecord toRecord(String username, int permission) {
    return new DefaultPermissionTableRecord()
        .with(DefaultPermissionTable.DEFAULT_PERMISSION_TABLE.PERMISSION, permission)
        .with(DefaultPermissionTable.DEFAULT_PERMISSION_TABLE.USERNAME, username);
  }

}
