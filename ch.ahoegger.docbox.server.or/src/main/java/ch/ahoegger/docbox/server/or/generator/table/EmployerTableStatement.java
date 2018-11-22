package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.Employer;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IEmployerTable;

/**
 * <h3>{@link EmployerTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployerTableStatement implements ITableStatement, IEmployerTable {
  private static final Logger LOG = LoggerFactory.getLogger(EmployerTableStatement.class);

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (")
        .append(EMPLOYER_NR).append(" BIGINT NOT NULL, ")
        .append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append(") NOT NULL, ")
        .append(ADDRESS_NR).append(" BIGINT NOT NULL, ")
        .append(EMAIL).append(" VARCHAR(").append(EMAIL_LENGTH).append("), ")
        .append(PHONE).append(" VARCHAR(").append(PHONE_LENGTH).append("), ")
        .append("PRIMARY KEY (").append(EMPLOYER_NR).append(")")
        .append(")");
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
    DSL.using(connection, SQLDialect.DERBY).delete(Employer.EMPLOYER)
        .execute();

  }

  @Override
  public void dropTable(Connection connection) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DSL.using(connection, SQLDialect.DERBY).dropTable(Employer.EMPLOYER)
        .execute();
  }

}
