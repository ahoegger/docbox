package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IEmployeeTaxGroupTable;
import ch.ahoegger.docbox.server.or.generator.IJooqTable;

/**
 * <h3>{@link EmployeeTaxGroupTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployeeTaxGroupTableStatement implements ITableStatement, IJooqTable, IEmployeeTaxGroupTable {
  private static final Logger LOG = LoggerFactory.getLogger(EmployeeTaxGroupTableStatement.class);

  @Override
  public Table<?> getJooqTable() {
    return EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
  }

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(EMPLOYEE_TAX_GROUP_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(EMPLOYEE_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(EMPLOYER_TAX_GROUP_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(STATEMENT_NR).append(" BIGINT, ");
    statementBuilder.append(START_DATE).append(" DATE, ");
    statementBuilder.append(END_DATE).append(" DATE, ");
    statementBuilder.append("PRIMARY KEY (").append(EMPLOYEE_TAX_GROUP_NR).append("), ");
    statementBuilder.append("UNIQUE (").append(ITableStatement.columns(EMPLOYEE_NR, EMPLOYER_TAX_GROUP_NR)).append(")");
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
