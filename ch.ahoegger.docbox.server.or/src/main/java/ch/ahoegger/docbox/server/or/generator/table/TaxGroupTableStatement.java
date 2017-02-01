package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ITaxGroupTable;

/**
 * <h3>{@link TaxGroupTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class TaxGroupTableStatement implements ITableStatement, ITaxGroupTable {
  private static final Logger LOG = LoggerFactory.getLogger(TaxGroupTableStatement.class);

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(TAX_GROUP_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(START_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(END_DATE).append(" DATE, ");
    statementBuilder.append("PRIMARY KEY (").append(TAX_GROUP_NR).append(")");
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
    DSL.using(connection, SQLDialect.DERBY).delete(TaxGroup.TAX_GROUP)
        .execute();

  }

  @Override
  public void dropTable(Connection connection) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DSL.using(connection, SQLDialect.DERBY).dropTable(TaxGroup.TAX_GROUP)
        .execute();
  }

}
