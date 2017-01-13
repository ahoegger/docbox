package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPartner;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.DocumentPartnerTableStatement;

/**
 * <h3>{@link DocumentPartnerTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentPartnerTableTask extends DocumentPartnerTableStatement implements ITableTask {

  private static final Logger LOG = LoggerFactory.getLogger(DocumentPartnerTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    DocumentPartner t = DocumentPartner.DOCUMENT_PARTNER;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    DocumentPartner t = DocumentPartner.DOCUMENT_PARTNER;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(t);
    query.execute();
  }

  public void createDocumentPartnerRow(ISqlService sqlService, BigDecimal documentId, BigDecimal partnerId) {
    DocumentPartner t = DocumentPartner.DOCUMENT_PARTNER;
    DSL.using(sqlService.getConnection(), SQLDialect.DERBY)
        .newRecord(t)
        .with(t.DOCUMENT_NR, documentId)
        .with(t.PARTNER_NR, partnerId)
        .insert();
  }
}
