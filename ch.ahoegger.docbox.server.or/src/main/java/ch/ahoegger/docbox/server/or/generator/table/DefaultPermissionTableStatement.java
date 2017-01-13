package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.IDefaultPermissionTable;

/**
 * <h3>{@link DefaultPermissionTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class DefaultPermissionTableStatement implements ITableStatement, IDefaultPermissionTable {

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(USERNAME).append(" VARCHAR(").append(UserTableStatement.USERNAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(PERMISSION).append(" INT NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(USERNAME).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

}
