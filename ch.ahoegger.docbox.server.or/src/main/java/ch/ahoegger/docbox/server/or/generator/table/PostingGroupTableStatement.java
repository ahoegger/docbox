package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.PostingGroup;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IPostingGroupTable;

/**
 * <h3>{@link PostingGroupTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class PostingGroupTableStatement implements ITableStatement, IPostingGroupTable {
  private static final Logger LOG = LoggerFactory.getLogger(PostingGroupTableStatement.class);

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(POSTING_GROUP_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(PARTNER_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(TAX_GROUP_NR).append(" BIGINT, ");
    statementBuilder.append(DOCUMENT_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append("), ");
    statementBuilder.append(START_DATE).append(" DATE, ");
    statementBuilder.append(END_DATE).append(" DATE, ");
    statementBuilder.append(STATEMENT_DATE).append(" DATE, ");
    statementBuilder.append(WORKING_HOURS).append(" DECIMAL(6, 2), ");
    statementBuilder.append(BRUTTO_WAGE).append(" DECIMAL(6, 2), ");
    statementBuilder.append(NETTO_WAGE).append(" DECIMAL(6, 2), ");
    statementBuilder.append(SOURCE_TAX).append(" DECIMAL(6, 2), ");
    statementBuilder.append(SOCIAL_SECURITY_TAX).append(" DECIMAL(6, 2), ");
    statementBuilder.append(VACATION_EXTRA).append(" DECIMAL(6, 2), ");
    statementBuilder.append("PRIMARY KEY (").append(POSTING_GROUP_NR).append(")");
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
    DSL.using(connection, SQLDialect.DERBY).delete(PostingGroup.POSTING_GROUP)
        .execute();

  }

  @Override
  public void dropTable(Connection connection) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DSL.using(connection, SQLDialect.DERBY).dropTable(PostingGroup.POSTING_GROUP)
        .execute();
  }

}
