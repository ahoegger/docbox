package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.administration.user.IUserRoleTable;
import ch.ahoegger.docbox.server.database.IDocboxSqlService;
import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;

/**
 * <h3>{@link UserRoleTableTask}</h3>
 *
 * @author aho
 */
public class UserRoleTableTask implements ITableTask, IUserRoleTable {
  private static final Logger LOG = LoggerFactory.getLogger(UserRoleTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(ROLE_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(USERNAME).append(" VARCHAR(").append(IUserTable.USERNAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(SqlFramentBuilder.columns(ROLE_NR, USERNAME)).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(IDocboxSqlService sqlService) {
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(IDocboxSqlService sqlService) {
    createUserRole(sqlService, IDevSequenceNumbers.SEQ_START_ROLE, "admin");
  }

  private void createUserRole(IDocboxSqlService sqlService, Long roleId, String username) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(ROLE_NR, USERNAME));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":roleId, :username");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("roleId", roleId),
        new NVPair("username", username));
  }
}
