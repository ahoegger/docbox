package ch.ahoegger.docbox.server.or.generator.table;

import java.sql.Connection;
import java.sql.SQLException;

import org.ch.ahoegger.docbox.server.or.app.tables.Document;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.IDocumentTable;

/**
 * <h3>{@link DocumentTableStatement}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentTableStatement implements ITableStatement, IDocumentTable {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentTableStatement.class);

  @Override
  public String getCreateTable() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
    statementBuilder.append(DOCUMENT_NR).append(" BIGINT NOT NULL, ");
    statementBuilder.append(ABSTRACT).append(" VARCHAR(").append(ABSTRACT_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(DOCUMENT_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(INSERT_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(VALID_DATE).append(" DATE, ");
    statementBuilder.append(DOCUMENT_URL).append(" VARCHAR(").append(DOCUMENT_URL_LENGTH).append("), ");
    statementBuilder.append(ORIGINAL_STORAGE).append(" VARCHAR(").append(ORIGINAL_STORAGE_LENGTH).append("), ");
    statementBuilder.append(CONVERSATION_NR).append(" BIGINT, ");
    statementBuilder.append(PARSE_OCR).append(" BOOLEAN NOT NULL, ");
    statementBuilder.append(OCR_LANGUAGE).append(" VARCHAR(").append(OCR_LANGUAGE_LENGTH).append("), ");
    statementBuilder.append("PRIMARY KEY (").append(DOCUMENT_NR).append(")");
    statementBuilder.append(" )");
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
    DSL.using(connection, SQLDialect.DERBY).delete(Document.DOCUMENT)
        .execute();

  }

  @Override
  public void dropTable(Connection connection) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DSL.using(connection, SQLDialect.DERBY).dropTable(Document.DOCUMENT)
        .execute();
  }

}
