package ch.ahoegger.docbox.server.database.initialization;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.server.document.store.DevDocumentStoreService;
import ch.ahoegger.docbox.server.document.store.DocumentStoreService;
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
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: DOCUMENT");
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {0}", TABLE_NAME);
    try {
      createDocumentRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT, "A sample document", new Date(), new Date(), new Date(), "2016_03_08_124640.pdf", null);
      createDocumentRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT + 1, "Bobs document", new Date(), new Date(), new Date(), "2016_03_08_124640.pdf", null);
      createDocumentRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT + 2, "Muliple partner document", new Date(), new Date(), new Date(), "2016_03_08_124640.pdf", null);
    }
    catch (IOException e) {
      LOG.error("Could not add dev documents to data store.", e);
    }
  }

  public void createDocumentRow(ISqlService sqlService, long documentId, String abstractText, Date documentDate, Date capturedDate, Date validDate, String devDocumentName, String originalStorage) throws IOException {
    String docPath = addDevDocument(devDocumentName, capturedDate, documentId);
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
        new NVPair("documentUrl", docPath),
        new NVPair("originalStorage", originalStorage));
  }

  private String addDevDocument(String fileName, Date insertDate, Long documentId) throws IOException {
    URL resource = DevDocumentStoreService.class.getClassLoader().getResource("devDocuments/" + fileName);
    BinaryResource br = new BinaryResource(fileName, "pdf", IOUtility.getContent(resource.openStream()), System.currentTimeMillis());
    return BEANS.get(DocumentStoreService.class).store(br, insertDate, documentId);
  }
}
