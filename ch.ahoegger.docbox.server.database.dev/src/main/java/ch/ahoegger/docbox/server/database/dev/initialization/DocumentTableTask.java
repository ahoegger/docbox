package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.Document;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.DocumentTableStatement;

/**
 * <h3>{@link DocumentTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentTableTask extends DocumentTableStatement implements ITableTask {
  private static final Logger LOG = LoggerFactory.getLogger(DocumentTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: DOCUMENT");
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    Document t = Document.DOCUMENT;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    Document t = Document.DOCUMENT;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(t);
    query.execute();
  }

  public void insert(ISqlService sqlService, BigDecimal documentId, String abstractText, Date documentDate,
      Date capturedDate, Date validDate, String docPath, String originalStorage, BigDecimal conversationId, boolean parseOcr, String ocrLanguage) {
    Document t = Document.DOCUMENT;
    DSL.using(sqlService.getConnection(), SQLDialect.DERBY)
        .newRecord(t)
        .with(t.ABSTRACT, abstractText)
        .with(t.CONVERSATION_NR, conversationId)
        .with(t.DOCUMENT_DATE, documentDate)
        .with(t.DOCUMENT_NR, documentId)
        .with(t.DOCUMENT_URL, docPath)
        .with(t.INSERT_DATE, capturedDate)
        .with(t.OCR_LANGUAGE, ocrLanguage)
        .with(t.ORIGINAL_STORAGE, originalStorage)
        .with(t.PARSE_OCR, parseOcr)
        .with(t.VALID_DATE, validDate)
        .insert();

  }

}
