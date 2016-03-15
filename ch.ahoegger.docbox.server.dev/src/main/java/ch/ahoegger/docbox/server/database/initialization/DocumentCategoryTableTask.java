package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.document.IDocumentCategoryTable;

/**
 * <h3>{@link DocumentCategoryTableTask}</h3>
 *
 * @author aho
 */
public class DocumentCategoryTableTask implements ITableTask, IDocumentCategoryTable {

  private static final Logger LOG = LoggerFactory.getLogger(DocumentCategoryTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(DOCUMENT_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(CATEGORY_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(SqlFramentBuilder.columns(DOCUMENT_NR, CATEGORY_NR)).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {0}", TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {0}", TABLE_NAME);
    createDocumentCategoryRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT, IDevSequenceNumbers.SEQ_START_CATEGORY);
    createDocumentCategoryRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT + 2, IDevSequenceNumbers.SEQ_START_CATEGORY);
    createDocumentCategoryRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT + 2, IDevSequenceNumbers.SEQ_START_CATEGORY + 1);
  }

  private void createDocumentCategoryRow(ISqlService sqlService, Long documentId, Long categoryId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(DOCUMENT_NR, CATEGORY_NR));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":documentId, :categoryId");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("documentId", documentId),
        new NVPair("categoryId", categoryId));
  }
}
