package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.ocr.IDocumentOcrTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link DocumentOcrTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentOcrTableTask implements ITableTask, IDocumentOcrTable {

  private static final Logger LOG = LoggerFactory.getLogger(DocumentOcrTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(DOCUMENT_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(TEXT).append(" CLOB(").append(TEXT_LENGHT).append(") , ");
    statementBuilder.append(OCR_SCANNED).append(" BOOLEAN NOT NULL, ");
    statementBuilder.append(PARSE_FAILED).append(" BOOLEAN NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(SqlFramentBuilder.columns(DOCUMENT_NR)).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
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

  public void createDocumentOcrRow(ISqlService sqlService, BigDecimal documentId, String text, boolean parsed, boolean parseFailed) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(DOCUMENT_NR, TEXT, OCR_SCANNED, PARSE_FAILED));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":documentId, :text, :parsed, :parseFailed");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("documentId", documentId),
        new NVPair("text", text),
        new NVPair("parsed", parsed),
        new NVPair("parseFailed", parseFailed));
  }
}
