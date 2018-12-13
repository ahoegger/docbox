package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IEmployerTaxGroupTable;
import ch.ahoegger.docbox.server.or.generator.IJooqTable;

/**
 * <h3>{@link EmployerTaxGroupTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployerTaxGroupTableStatement implements ITableStatement, IJooqTable, IEmployerTaxGroupTable {
  private static final Logger LOG = LoggerFactory.getLogger(EmployerTaxGroupTableStatement.class);

  @Override
  public Table<?> getJooqTable() {
    return EmployerTaxGroup.EMPLOYER_TAX_GROUP;
  }

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(EMPLOYER_TAX_GROUP_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(EMPLOYER_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(TAX_GROUP_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(STATEMENT_NR).append(" BIGINT, ");
    statementBuilder.append("PRIMARY KEY (").append(EMPLOYER_TAX_GROUP_NR).append("), ");
    statementBuilder.append("UNIQUE (").append(ITableStatement.columns(EMPLOYER_NR, TAX_GROUP_NR)).append(")");
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
