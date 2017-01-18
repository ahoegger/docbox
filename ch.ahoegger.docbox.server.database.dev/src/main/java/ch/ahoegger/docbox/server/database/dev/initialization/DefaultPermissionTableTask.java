package ch.ahoegger.docbox.server.database.dev.initialization;

import org.ch.ahoegger.docbox.server.or.app.tables.DefaultPermissionTable;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.DefaultPermissionTableStatement;

/**
 * <h3>{@link DefaultPermissionTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class DefaultPermissionTableTask extends DefaultPermissionTableStatement implements ITableTask {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultPermissionTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    DefaultPermissionTable t = DefaultPermissionTable.DEFAULT_PERMISSION_TABLE;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DefaultPermissionTable t = DefaultPermissionTable.DEFAULT_PERMISSION_TABLE;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(t);
    query.execute();
  }

  public void insert(ISqlService sqlService, String username, int permission) {
    DefaultPermissionTable t = DefaultPermissionTable.DEFAULT_PERMISSION_TABLE;
    DSL.using(sqlService.getConnection(), SQLDialect.DERBY)
        .newRecord(t)
        .with(t.PERMISSION, permission)
        .with(t.USERNAME, username)
        .insert();
  }
}
