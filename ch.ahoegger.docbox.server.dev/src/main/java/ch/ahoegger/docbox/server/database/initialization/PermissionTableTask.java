package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.IDocboxSqlService;
import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;
import ch.ahoegger.docbox.shared.security.permission.IPermissionTable;

/**
 * <h3>{@link PermissionTableTask}</h3>
 *
 * @author aho
 */
public class PermissionTableTask implements ITableTask, IPermissionTable {
  private static final Logger LOG = LoggerFactory.getLogger(PermissionTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
    statementBuilder.append(USERNAME).append(" VARCHAR(").append(IUserTable.USERNAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(ENTITY_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(PERMISSION).append(" SMALLINT NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(SqlFramentBuilder.columns(IUserTable.USERNAME, ENTITY_NR)).append(")");
    statementBuilder.append(" )");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(IDocboxSqlService sqlService) {
    LOG.info("SQL-DEV create Table: " + TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(IDocboxSqlService sqlService) {
    createPermission(sqlService, "admin", IDevSequenceNumbers.SEQ_START_DOCUMENT, PERMISSION_WRITE);
    createPermission(sqlService, "admin", IDevSequenceNumbers.SEQ_START_DOCUMENT + 1, PERMISSION_WRITE);
    createPermission(sqlService, "admin", IDevSequenceNumbers.SEQ_START_DOCUMENT + 2, PERMISSION_WRITE);
    createPermission(sqlService, "cuttis", IDevSequenceNumbers.SEQ_START_DOCUMENT + 1, PERMISSION_READ);
    createPermission(sqlService, "bob", IDevSequenceNumbers.SEQ_START_DOCUMENT + 1, PERMISSION_WRITE);
  }

  private void createPermission(IDocboxSqlService sqlService, String userId, Long entityId, Integer permission) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(IUserTable.USERNAME, ENTITY_NR, PERMISSION));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":userId, :entityId, :permission");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("userId", userId),
        new NVPair("entityId", entityId),
        new NVPair("permission", permission));
  }

}
