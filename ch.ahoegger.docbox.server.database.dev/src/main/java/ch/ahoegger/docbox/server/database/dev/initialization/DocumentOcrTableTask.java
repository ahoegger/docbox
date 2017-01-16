package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentOcr;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.DocumentOcrTableStatement;

/**
 * <h3>{@link DocumentOcrTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentOcrTableTask extends DocumentOcrTableStatement implements ITableTask {

  private static final Logger LOG = LoggerFactory.getLogger(DocumentOcrTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    DocumentOcr t = DocumentOcr.DOCUMENT_OCR;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DocumentOcr t = DocumentOcr.DOCUMENT_OCR;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(t);
    query.execute();
  }

  public void createDocumentOcrRow(ISqlService sqlService, BigDecimal documentId, String text, boolean parsed, int parseCount, String parseFailedReason) {
    DocumentOcr t = DocumentOcr.DOCUMENT_OCR;
    DSL.using(sqlService.getConnection(), SQLDialect.DERBY)
        .newRecord(t)
        .with(t.DOCUMENT_NR, documentId)
        .with(t.OCR_SCANNED, parsed)
        .with(t.PARSE_COUNT, parseCount)
        .with(t.FAILED_REASON, parseFailedReason)
        .with(t.TEXT, text)
        .insert();
  }
}
