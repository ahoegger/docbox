package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.Address;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IAddressTable;

/**
 * <h3>{@link AddressTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class AddressTableStatement implements ITableStatement, IAddressTable {
  private static final Logger LOG = LoggerFactory.getLogger(AddressTableStatement.class);

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (")
        .append(ADDRESS_NR).append(" BIGINT NOT NULL, ")
        .append(LINE_1).append(" VARCHAR(").append(ADDRESS_LINE_LENGTH).append("), ")
        .append(LINE_2).append(" VARCHAR(").append(ADDRESS_LINE_LENGTH).append("), ")
        .append(PLZ).append(" VARCHAR(").append(PLZ_LENGTH).append("), ")
        .append(CITY).append(" VARCHAR(").append(CITY_LENGTH).append("), ");

    statementBuilder.append("PRIMARY KEY (").append(ADDRESS_NR).append(")");
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
    DSL.using(connection, SQLDialect.DERBY).delete(Address.ADDRESS)
        .execute();

  }

  @Override
  public void dropTable(Connection connection) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DSL.using(connection, SQLDialect.DERBY).dropTable(Address.ADDRESS)
        .execute();
  }

}
