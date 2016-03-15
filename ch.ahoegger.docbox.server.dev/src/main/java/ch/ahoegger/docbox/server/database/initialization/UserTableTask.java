package ch.ahoegger.docbox.server.database.initialization;

import java.util.Date;

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
 * @author aho
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
    statementBuilder.append(INSERT_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(VALID_DATE).append(" DATE, ");
    statementBuilder.append("PRIMARY KEY (").append(USERNAME).append(")");
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
    createUser(sqlService, "Cuttis", "Bolion", "cuttis", "pwd", new Date(), null);
    createUser(sqlService, "Bob", "Miller", "bob", "pwd", new Date(), null);
    createUser(sqlService, "Admin", "Manager", "admin", "manager", new Date(), null);
  }

  private void createUser(ISqlService sqlService, String name, String firstname, String username, String password, Date inserDate, Date validDate) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(NAME, FIRSTNAME, USERNAME, PASSWORD, INSERT_DATE, VALID_DATE));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":name, :firstname, :username, :password, :inserDate, :validDate");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("name", name),
        new NVPair("firstname", firstname),
        new NVPair("username", username),
        new NVPair("password", new String(BEANS.get(UserService.class).createPasswordHash(password.toCharArray()))),
        new NVPair("inserDate", inserDate),
        new NVPair("validDate", validDate));
  }
}
