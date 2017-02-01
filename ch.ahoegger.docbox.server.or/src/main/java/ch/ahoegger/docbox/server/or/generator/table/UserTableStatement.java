package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IUserTable;

/**
 * <h3>{@link UserTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class UserTableStatement implements ITableStatement, IUserTable {
  private static final Logger LOG = LoggerFactory.getLogger(UserTableStatement.class);

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

  @Override
  public void createTable(Connection connection) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    try {
      connection.createStatement().execute(getCreateTable());
    }
    catch (SQLException e) {
      throw new ProcessingException("Could not create table '" + TABLE_NAME + "'.", e);
    }
  }

  @Override
  public void deleteTable(Connection connection) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    DSL.using(connection, SQLDialect.DERBY).delete(DocboxUser.DOCBOX_USER)
        .execute();

  }

  @Override
  public void dropTable(Connection connection) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DSL.using(connection, SQLDialect.DERBY).dropTable(DocboxUser.DOCBOX_USER)
        .execute();
  }

}
