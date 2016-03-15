package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;
import ch.ahoegger.docbox.shared.security.permission.IDefaultPermissionTable;

/**
 * <h3>{@link DefaultPermissionTableTask}</h3>
 *
 * @author aho
 */
public class DefaultPermissionTableTask implements ITableTask, IDefaultPermissionTable {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultPermissionTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(USERNAME).append(" VARCHAR(").append(IUserTable.USERNAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(PERMISSION).append(" SMALLINT NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(SqlFramentBuilder.columns(USERNAME)).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {0}", TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {0}", TABLE_NAME);
    createDefaultPermissionRow(sqlService, "admin", IDocumentPermissionTable.PERMISSION_WRITE);
    createDefaultPermissionRow(sqlService, "bob", IDocumentPermissionTable.PERMISSION_READ);
  }

  private void createDefaultPermissionRow(ISqlService sqlService, String username, int permission) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(USERNAME, PERMISSION));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":username, :permission");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("username", username),
        new NVPair("permission", permission));
  }
}
