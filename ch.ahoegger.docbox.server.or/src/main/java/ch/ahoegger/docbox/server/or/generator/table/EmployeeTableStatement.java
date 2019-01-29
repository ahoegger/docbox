package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.Employee;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IEmployeeTable;
import ch.ahoegger.docbox.server.or.generator.IJooqTable;

/**
 * <h3>{@link EmployeeTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployeeTableStatement implements ITableStatement, IJooqTable, IEmployeeTable {
  private static final Logger LOG = LoggerFactory.getLogger(EmployeeTableStatement.class);

  @Override
  public Table<?> getJooqTable() {
    return Employee.EMPLOYEE;
  }

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (")
        .append(EMPLOYEE_NR).append(" BIGINT NOT NULL, ")
        .append(EMPLOYER_NR).append(" BIGINT NOT NULL, ")
        .append(ADDRESS_NR).append(" BIGINT NOT NULL, ")
        .append(FIRST_NAME).append(" VARCHAR(").append(FIRST_NAME_LENGTH).append(") NOT NULL, ")
        .append(LAST_NAME).append(" VARCHAR(").append(LAST_NAME_LENGTH).append(") NOT NULL, ")
        .append(AHV_NUMBER).append(" VARCHAR(").append(AHV_NUMBER_LENGTH).append("), ")
        .append(BIRTHDAY).append(" DATE, ")
        .append(ACCOUNT_NUMBER).append(" VARCHAR(").append(ACCOUNT_NUMBER_LENGTH).append("), ")
        .append(TAX_TYPE).append(" BIGINT NOT NULL, ")
        .append(REDUCED_LUNCH).append(" BOOLEAN NOT NULL, ")
        .append(HOURLY_WAGE).append(" DECIMAL(5, 2), ")
        .append(SOCIAL_INSURANCE_RATE).append(" DECIMAL(5, 3), ")
        .append(SOURCE_TAX_RATE).append(" DECIMAL(5, 3), ")
        .append(VACATION_EXTRA_RATE).append(" DECIMAL(5, 3), ")
        .append(PENSIONS_FUND_MONTHLY).append(" DECIMAL(9, 2), ")
        .append("PRIMARY KEY (").append(EMPLOYEE_NR).append(")")
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
