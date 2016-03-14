package ch.ahoegger.docbox.server.database.initialization;

import java.util.Date;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.IDocboxSqlService;
import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.document.IDocumentTable;

/**
 * <h3>{@link DocumentTableTask}</h3>
 *
 * @author aho
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
    statementBuilder.append("PRIMARY KEY (").append(DOCUMENT_NR).append(")");
    statementBuilder.append(" )");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(IDocboxSqlService sqlService) {
    LOG.info("SQL-DEV create Table: DOCUMENT");
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(IDocboxSqlService sqlService) {
    createDocumentRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT, "A sample document", new Date(), new Date(), new Date(), "2016/2016_03_08_124640.pdf", null);
    createDocumentRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT + 1, "Bobs document", new Date(), new Date(), new Date(), "2016/2016_03_08_124640.pdf", null);
    createDocumentRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT + 2, "Muliple partner document", new Date(), new Date(), new Date(), "2016/2016_03_08_124640.pdf", null);
  }

  public void createDocumentRow(IDocboxSqlService sqlService, long documentId, String abstractText, Date documentDate, Date capturedDate, Date validDate, String documentUrl, String originalStorage) {

    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("INSERT INTO ").append(TABLE_NAME);
    sqlBuilder.append(" (").append(SqlFramentBuilder.columns(DOCUMENT_NR, ABSTRACT, DOCUMENT_DATE, INSERT_DATE, VALID_DATE, DOCUMENT_URL, ORIGINAL_STORAGE));
    sqlBuilder.append(") VALUES ( ");
    sqlBuilder.append(":documentId, :abstract, :documentDate, :insertDate, :validDate, :documentUrl, :originalStorage");
    sqlBuilder.append(")");

    sqlService.insert(sqlBuilder.toString(),
        new NVPair("documentId", documentId),
        new NVPair("abstract", abstractText),
        new NVPair("documentDate", documentDate),
        new NVPair("insertDate", capturedDate),
        new NVPair("validDate", validDate),
        new NVPair("documentUrl", documentUrl),
        new NVPair("originalStorage", originalStorage));
  }
}
