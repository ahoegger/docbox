package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.Entity;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IEntityTable;
import ch.ahoegger.docbox.server.or.generator.IJooqTable;

/**
 * <h3>{@link EntityTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class EntityTableStatement implements ITableStatement, IJooqTable, IEntityTable {
  private static final Logger LOG = LoggerFactory.getLogger(EntityTableStatement.class);

  @Override
  public Table<?> getJooqTable() {
    return Entity.ENTITY;
  }

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(ENTITY_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(PAYSLIP_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(ENTITY_TYPE).append(" BIGINT NOT NULL, ");
    statementBuilder.append(ENTITY_DATE).append(" DATE, ");
    statementBuilder.append(WORKING_HOURS).append(" DECIMAL(4, 2), ");
    statementBuilder.append(EXPENSE_AMOUNT).append(" DECIMAL(8, 2), ");
    statementBuilder.append(DESCRIPTION).append(" VARCHAR(").append(DESCRIPTION_LENGTH).append("), ");
    statementBuilder.append("PRIMARY KEY (").append(ENTITY_NR).append(")");
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
