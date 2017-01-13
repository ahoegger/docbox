package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentCategory;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentCategoryRecord;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.DocumentCategoryTableStatement;

/**
 * <h3>{@link DocumentCategoryTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentCategoryTableTask extends DocumentCategoryTableStatement implements ITableTask {

  private static final Logger LOG = LoggerFactory.getLogger(DocumentCategoryTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    DocumentCategory t = DocumentCategory.DOCUMENT_CATEGORY;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DocumentCategory t = DocumentCategory.DOCUMENT_CATEGORY;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(t);
    query.execute();
  }

  public void createDocumentCategoryRow(ISqlService sqlService, BigDecimal documentId, BigDecimal categoryId) {

    DocumentCategoryRecord newRecord = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).newRecord(DocumentCategory.DOCUMENT_CATEGORY);
    newRecord.setDocumentNr(documentId);
    newRecord.setCategoryNr(categoryId);
    newRecord.insert();
  }
}
