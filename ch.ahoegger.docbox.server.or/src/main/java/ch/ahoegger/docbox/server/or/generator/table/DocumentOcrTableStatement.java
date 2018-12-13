package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentOcr;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IDocumentOcrTable;
import ch.ahoegger.docbox.server.or.generator.IJooqTable;

/**
 * <h3>{@link DocumentOcrTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentOcrTableStatement implements ITableStatement, IJooqTable, IDocumentOcrTable {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentOcrTableStatement.class);

  @Override
  public Table<?> getJooqTable() {
    return DocumentOcr.DOCUMENT_OCR;
  }

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(DOCUMENT_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(TEXT).append(" CLOB(").append(TEXT_LENGHT).append(") , ");
    statementBuilder.append(OCR_SCANNED).append(" BOOLEAN NOT NULL, ");
    statementBuilder.append(PARSE_COUNT).append(" INT, ");
    statementBuilder.append(FAILED_REASON).append(" VARCHAR(").append(FAILED_REASON_LENGTH).append("), ");
    statementBuilder.append("PRIMARY KEY (").append(ITableStatement.columns(DOCUMENT_NR)).append(")");
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
