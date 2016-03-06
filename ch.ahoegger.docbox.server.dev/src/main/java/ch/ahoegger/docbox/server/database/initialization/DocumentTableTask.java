package ch.ahoegger.docbox.server.database.initialization;

import java.util.Date;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.IDocboxSqlService;
import ch.ahoegger.docbox.server.document.IDocumentTable;

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
    statementBuilder.append(ABSTRACT).append(" VARCHAR(3000) NOT NULL, ");
    statementBuilder.append(PARTNER_NR).append(" DECIMAL, ");
    statementBuilder.append(DOCUMENT_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(INSERT_DATE).append(" DATE NOT NULL, ");
    statementBuilder.append(VALID_DATE).append(" DATE, ");
    statementBuilder.append(DOCUMENT_URL).append(" VARCHAR(1200), ");
    statementBuilder.append(ORIGINAL_STORAGE).append(" VARCHAR(1200), ");
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
    createDocumentRow(sqlService, Long.valueOf(0l), "A sample document", Long.valueOf(100l), new Date(), new Date(), new Date(), null, null);
  }

  public void createDocumentRow(IDocboxSqlService sqlService, long documentId, String abstractText, Long companyId, Date documentDate, Date capturedDate, Date validDate, String documentUrl, String originalStorage) {

    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("INSERT INTO ").append(TABLE_NAME);
    sqlBuilder.append(" (");
    sqlBuilder.append(DOCUMENT_NR).append(", ");
    sqlBuilder.append(ABSTRACT).append(", ");
    sqlBuilder.append(PARTNER_NR).append(", ");
    sqlBuilder.append(DOCUMENT_DATE).append(", ");
    sqlBuilder.append(INSERT_DATE).append(", ");
    sqlBuilder.append(VALID_DATE).append(", ");
    sqlBuilder.append(DOCUMENT_URL).append(", ");
    sqlBuilder.append(ORIGINAL_STORAGE);
    sqlBuilder.append(") VALUES ( ");
    sqlBuilder.append(":documentId, :abstract, :companyId, :documentDate, :insertDate, :validDate, :documentUrl, :originalStorage");
    sqlBuilder.append(")");

    sqlService.insert(sqlBuilder.toString(),
        new NVPair("documentId", documentId),
        new NVPair("abstract", abstractText),
        new NVPair("companyId", companyId),
        new NVPair("documentDate", documentDate),
        new NVPair("insertDate", capturedDate),
        new NVPair("validDate", validDate),
        new NVPair("documentUrl", documentUrl),
        new NVPair("originalStorage", originalStorage));
  }
}
