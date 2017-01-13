package ch.ahoegger.docbox.server.database.dev.initialization;

import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.UserTableStatement;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link UserTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class UserTableTask extends UserTableStatement implements ITableTask {

  private static final Logger LOG = LoggerFactory.getLogger(UserTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    DocboxUser t = DocboxUser.DOCBOX_USER;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DROP TABLE ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  public void insertUser(ISqlService sqlService, String name, String firstname, String username, String password,
      boolean active, boolean administrator) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(NAME, FIRSTNAME, USERNAME, PASSWORD, ACTIVE, ADMINISTRATOR));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":name, :firstname, :username, :password, :active, :administrator");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("name", name),
        new NVPair("firstname", firstname),
        new NVPair("username", username),
        new NVPair("password", password),
        new NVPair("active", active),
        new NVPair("administrator", administrator));
  }
}
