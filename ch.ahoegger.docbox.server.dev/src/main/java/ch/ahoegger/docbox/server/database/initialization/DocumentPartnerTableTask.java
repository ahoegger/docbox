package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.IDocboxSqlService;
import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.document.IDocumentPartnerTable;

/**
 * <h3>{@link DocumentPartnerTableTask}</h3>
 *
 * @author aho
 */
public class DocumentPartnerTableTask implements ITableTask, IDocumentPartnerTable {

  private static final Logger LOG = LoggerFactory.getLogger(DocumentPartnerTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(DOCUMENT_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(PARTNER_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(SqlFramentBuilder.columns(DOCUMENT_NR, PARTNER_NR)).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(IDocboxSqlService sqlService) {
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(IDocboxSqlService sqlService) {
    createDocumentPartnerRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT, IDevSequenceNumbers.SEQ_START_PARTNER);
    createDocumentPartnerRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT + 2, IDevSequenceNumbers.SEQ_START_PARTNER);
    createDocumentPartnerRow(sqlService, IDevSequenceNumbers.SEQ_START_DOCUMENT + 2, IDevSequenceNumbers.SEQ_START_PARTNER + 1);
  }

  private void createDocumentPartnerRow(IDocboxSqlService sqlService, Long documentId, Long partnerId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(DOCUMENT_NR, PARTNER_NR));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":documentId, :partnerId");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("documentId", documentId),
        new NVPair("partnerId", partnerId));
  }
}
