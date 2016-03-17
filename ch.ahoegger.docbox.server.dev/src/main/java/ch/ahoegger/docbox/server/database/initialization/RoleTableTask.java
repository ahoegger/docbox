package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.server.security.role.IRoleTable;

/**
 * <h3>{@link RoleTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class RoleTableTask implements ITableTask, IRoleTable {
  private static final Logger LOG = LoggerFactory.getLogger(RoleTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(ROLE_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(240) NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(ROLE_NR).append(")");
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
    createRole(sqlService, IDevSequenceNumbers.SEQ_START_ROLE, "admin");
  }

  private void createRole(ISqlService sqlService, Long roleId, String roleName) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(ROLE_NR, NAME));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":roleId, :name");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("roleId", roleId),
        new NVPair("name", roleName));
  }

}
