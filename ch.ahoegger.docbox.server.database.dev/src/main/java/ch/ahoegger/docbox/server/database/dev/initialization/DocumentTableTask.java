package ch.ahoegger.docbox.server.database.dev.initialization;

import java.io.IOException;
import java.util.Date;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.document.IDocumentTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link DocumentTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentTableTask implements ITableTask, IDocumentTable {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
    statementBuilder.append(DOCUMENT_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(ABSTRACT).append(" VARCHAR(").append(ABSTRACT_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(DOCUMENT_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(INSERT_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(VALID_DATE).append(" DATE, ");
    statementBuilder.append(DOCUMENT_URL).append(" VARCHAR(").append(DOCUMENT_URL_LENGTH).append("), ");
    statementBuilder.append(ORIGINAL_STORAGE).append(" VARCHAR(").append(ORIGINAL_STORAGE_LENGTH).append("), ");
    statementBuilder.append(CONVERSATION_NR).append(" DECIMAL, ");
    statementBuilder.append("PRIMARY KEY (").append(DOCUMENT_NR).append(")");
    statementBuilder.append(" )");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: DOCUMENT");
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DROP TABLE ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  public void createDocumentRow(ISqlService sqlService, long documentId, String abstractText, Date documentDate,
      Date capturedDate, Date validDate, String docPath, String originalStorage, Long conversationId)
      throws IOException {

    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("INSERT INTO ").append(TABLE_NAME);
    sqlBuilder.append(" (").append(SqlFramentBuilder.columns(DOCUMENT_NR, ABSTRACT, DOCUMENT_DATE, INSERT_DATE,
        VALID_DATE, DOCUMENT_URL, ORIGINAL_STORAGE, CONVERSATION_NR));
    sqlBuilder.append(") VALUES ( ");
    sqlBuilder.append(
        ":documentId, :abstract, :documentDate, :insertDate, :validDate, :documentUrl, :originalStorage, :conversationId");
    sqlBuilder.append(")");

    sqlService.insert(sqlBuilder.toString(),
        new NVPair("documentId", documentId),
        new NVPair("abstract", abstractText),
        new NVPair("documentDate", documentDate),
        new NVPair("insertDate", capturedDate),
        new NVPair("validDate", validDate),
        new NVPair("documentUrl", docPath),
        new NVPair("originalStorage", originalStorage),
        new NVPair("conversationId", conversationId));
  }

}
