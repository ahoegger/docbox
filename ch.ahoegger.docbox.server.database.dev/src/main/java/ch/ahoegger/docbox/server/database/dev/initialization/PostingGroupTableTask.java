package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.PostingGroup;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.PostingGroupTableStatement;

/**
 * <h3>{@link PostingGroupTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class PostingGroupTableTask extends PostingGroupTableStatement implements ITableTask {
  private static final Logger LOG = LoggerFactory.getLogger(PostingGroupTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    PostingGroup t = PostingGroup.POSTING_GROUP;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    PostingGroup t = PostingGroup.POSTING_GROUP;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(t);
    query.execute();
  }

  public void createRow(ISqlService sqlService, BigDecimal postingGroupId, BigDecimal partnerId, BigDecimal taxGroupId, BigDecimal documentId, String name,
      Date statementDate, BigDecimal workingHours, BigDecimal bruttoWage, BigDecimal nettoWage, BigDecimal sourceTax, BigDecimal socialSecurityTax, BigDecimal vacationExtra) {

    PostingGroup t = PostingGroup.POSTING_GROUP;
    DSL.using(sqlService.getConnection(), SQLDialect.DERBY)
        .newRecord(t)
        .with(t.BRUTTO_WAGE, bruttoWage)
        .with(t.DOCUMENT_NR, documentId)
        .with(t.NAME, name)
        .with(t.NETTO_WAGE, nettoWage)
        .with(t.PARTNER_NR, partnerId)
        .with(t.POSTING_GROUP_NR, postingGroupId)
        .with(t.SOCIAL_SECURITY_TAX, socialSecurityTax)
        .with(t.SOURCE_TAX, sourceTax)
        .with(t.STATEMENT_DATE, statementDate)
        .with(t.TAX_GROUP_NR, taxGroupId)
        .with(t.VACATION_EXTRA, vacationExtra)
        .with(t.WORKING_HOURS, workingHours)
        .insert();
  }

}
