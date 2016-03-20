package ch.ahoegger.docbox.server.database.initialization;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.administration.user.IUserTable;
import ch.ahoegger.docbox.shared.document.IDocumentPermissionTable;

/**
 * <h3>{@link DocumentPermissionTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentPermissionTableTask implements ITableTask, IDocumentPermissionTable {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentPermissionTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
    statementBuilder.append(USERNAME).append(" VARCHAR(").append(IUserTable.USERNAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(DOCUMENT_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(PERMISSION).append(" SMALLINT NOT NULL, ");
    statementBuilder.append("PRIMARY KEY (").append(SqlFramentBuilder.columns(IUserTable.USERNAME, DOCUMENT_NR)).append(")");
    statementBuilder.append(" )");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(ISqlService sqlService) {
    LOG.info("SQL-DEV create rows for: {}", TABLE_NAME);
    createDocumentPermissionRow(sqlService, "admin", IDevSequenceNumbers.SEQ_START_DOCUMENT, PERMISSION_WRITE);
    createDocumentPermissionRow(sqlService, "admin", IDevSequenceNumbers.SEQ_START_DOCUMENT + 1, PERMISSION_WRITE);
    createDocumentPermissionRow(sqlService, "admin", IDevSequenceNumbers.SEQ_START_DOCUMENT + 2, PERMISSION_WRITE);
    createDocumentPermissionRow(sqlService, "cuttis", IDevSequenceNumbers.SEQ_START_DOCUMENT + 1, PERMISSION_READ);
    createDocumentPermissionRow(sqlService, "bob", IDevSequenceNumbers.SEQ_START_DOCUMENT + 1, PERMISSION_WRITE);
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DROP TABLE ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  private void createDocumentPermissionRow(ISqlService sqlService, String userId, Long documentId, Integer permission) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(IUserTable.USERNAME, DOCUMENT_NR, PERMISSION));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":userId, :documentId, :permission");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("userId", userId),
        new NVPair("documentId", documentId),
        new NVPair("permission", permission));
  }

}
