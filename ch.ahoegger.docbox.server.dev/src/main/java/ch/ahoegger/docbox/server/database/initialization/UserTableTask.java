package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.administration.user.IUserTable;
import ch.ahoegger.docbox.server.administration.user.UserService;
import ch.ahoegger.docbox.server.database.IDocboxSqlService;
import ch.ahoegger.docbox.server.database.SqlFramentBuilder;

/**
 * <h3>{@link UserTableTask}</h3>
 *
 * @author aho
 */
public class UserTableTask implements ITableTask, IUserTable {

  private static final Logger LOG = LoggerFactory.getLogger(UserTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
    statementBuilder.append(USER_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(240) NOT NULL, ");
    statementBuilder.append(FIRSTNAME).append(" VARCHAR(240) NOT NULL, ");
    statementBuilder.append(USERNAME).append(" VARCHAR(240) NOT NULL, ");
    statementBuilder.append(PASSWORD).append(" VARCHAR(480) NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(USER_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(IDocboxSqlService sqlService) {
    LOG.info("SQL-DEV create Table: USER");
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(IDocboxSqlService sqlService) {
    createUser(sqlService, "Cuttis", "Bolion", "admin", "manager");
  }

  private static void createUser(IDocboxSqlService sqlService, String name, String firstname, String username, String password) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(USER_NR, NAME, FIRSTNAME, USERNAME, PASSWORD));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":userId, :name, :firstname, :username, :password");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("userId", sqlService.getSequenceNextval()),
        new NVPair("name", name),
        new NVPair("firstname", firstname),
        new NVPair("username", username),
        new NVPair("password", new String(BEANS.get(UserService.class).createPasswordHash(password.toCharArray()))));
  }
}
