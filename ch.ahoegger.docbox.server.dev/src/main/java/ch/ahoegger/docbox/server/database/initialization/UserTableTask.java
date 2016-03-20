package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;

/**
 * <h3>{@link UserTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class UserTableTask implements ITableTask, IUserTable {

  private static final Logger LOG = LoggerFactory.getLogger(UserTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
    statementBuilder.append(USERNAME).append(" VARCHAR(").append(USERNAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(FIRSTNAME).append(" VARCHAR(").append(FIRSTNAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(PASSWORD).append(" VARCHAR(").append(PASSWORD_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(ACTIVE).append(" BOOLEAN NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(USERNAME).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", TABLE_NAME);
    createUser(sqlService, "Cuttis", "Bolion", "cuttis", "pwd", true);
    createUser(sqlService, "Bob", "Miller", "bob", "pwd", true);
    createUser(sqlService, "Admin", "Manager", "admin", "manager", true);
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DROP TABLE ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  public void createUser(ISqlService sqlService, String name, String firstname, String username, String password, boolean active) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(NAME, FIRSTNAME, USERNAME, PASSWORD, ACTIVE));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":name, :firstname, :username, :password, :active");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("name", name),
        new NVPair("firstname", firstname),
        new NVPair("username", username),
        new NVPair("password", new String(BEANS.get(UserService.class).createPasswordHash(password.toCharArray()))),
        new NVPair("active", active));
  }
}
