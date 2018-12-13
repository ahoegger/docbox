package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IStatementTable;
import ch.ahoegger.docbox.server.or.generator.IJooqTable;

/**
 * <h3>{@link StatementTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class StatementTableStatement implements ITableStatement, IJooqTable, IStatementTable {
  private static final Logger LOG = LoggerFactory.getLogger(StatementTableStatement.class);

  @Override
  public Table<?> getJooqTable() {
    return Statement.STATEMENT;
  }

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (")
        .append(STATEMENT_NR).append(" BIGINT NOT NULL, ")
        .append(DOCUMENT_NR).append(" BIGINT, ")
        .append(TAX_TYPE).append(" BIGINT, ")
        .append(STATEMENT_DATE).append(" DATE, ")
        .append(ACCOUNT_NUMBER).append(" VARCHAR(").append(ACCOUNT_NUMBER_LENGTH).append("), ")
        .append(HOURLY_WAGE).append(" DECIMAL(5, 2), ")
        .append(SOCIAL_INSURANCE_RATE).append(" DECIMAL(5, 3), ")
        .append(SOURCE_TAX_RATE).append(" DECIMAL(5, 3), ")
        .append(VACATION_EXTRA_RATE).append(" DECIMAL(5, 3), ")
        .append(WORKING_HOURS).append(" DECIMAL(9, 2), ")
        .append(WAGE).append(" DECIMAL(9, 2), ")
        .append(BRUTTO_WAGE).append(" DECIMAL(9, 2), ")
        .append(NETTO_WAGE).append(" DECIMAL(9, 2), ")
        .append(NETTO_WAGE_PAYOUT).append(" DECIMAL(9, 2), ")
        .append(SOURCE_TAX).append(" DECIMAL(9, 2), ")
        .append(SOCIAL_INSURANCE_TAX).append(" DECIMAL(9, 2), ")
        .append(VACATION_EXTRA).append(" DECIMAL(9, 2), ")
        .append(EXPENSES).append(" DECIMAL(9, 2), ")
        .append("PRIMARY KEY (").append(STATEMENT_NR).append(")")
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
    DSL.using(connection, SQLDialect.DERBY).delete(getJooqTable())
        .execute();

  }

  @Override
  public void dropTable(Connection connection) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DSL.using(connection, SQLDialect.DERBY).dropTable(getJooqTable())
        .execute();
  }

}
