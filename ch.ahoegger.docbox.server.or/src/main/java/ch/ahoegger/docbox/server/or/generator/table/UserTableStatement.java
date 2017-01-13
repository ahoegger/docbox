package ch.ahoegger.docbox.server.or.generator.table;

import ch.ahoegger.docbox.or.definition.table.IUserTable;

/**
 * <h3>{@link UserTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class UserTableStatement implements ITableStatement, IUserTable {

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
    statementBuilder.append(USERNAME).append(" VARCHAR(").append(USERNAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(FIRSTNAME).append(" VARCHAR(").append(FIRSTNAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(PASSWORD).append(" VARCHAR(").append(PASSWORD_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(ACTIVE).append(" BOOLEAN NOT NULL, ");
    statementBuilder.append(ADMINISTRATOR).append(" BOOLEAN NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(USERNAME).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

}
