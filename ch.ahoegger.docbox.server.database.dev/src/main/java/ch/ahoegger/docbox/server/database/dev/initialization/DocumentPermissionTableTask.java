package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPermission;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.DocumentPermissionTableStatement;

/**
 * <h3>{@link DocumentPermissionTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentPermissionTableTask extends DocumentPermissionTableStatement implements ITableTask {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentPermissionTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    DocumentPermission t = DocumentPermission.DOCUMENT_PERMISSION;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DocumentPermission t = DocumentPermission.DOCUMENT_PERMISSION;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(t);
    query.execute();
  }

  public void createDocumentPermissionRow(ISqlService sqlService, String userId, BigDecimal documentId, int permission) {
    DocumentPermission t = DocumentPermission.DOCUMENT_PERMISSION;
    DSL.using(sqlService.getConnection(), SQLDialect.DERBY)
        .newRecord(t)
        .with(t.DOCUMENT_NR, documentId)
        .with(t.PERMISSION, permission)
        .with(t.USERNAME, userId)
        .insert();
  }

}
