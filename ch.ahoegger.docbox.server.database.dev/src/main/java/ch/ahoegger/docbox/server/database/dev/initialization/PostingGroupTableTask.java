package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.PostingGroup;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.PostingGroupTableStatement;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

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
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DROP TABLE ").append(TABLE_NAME);
    sqlService.insert(statementBuilder.toString());
  }

  public void createRow(ISqlService sqlService, BigDecimal postingGroupId, BigDecimal partnerId, BigDecimal documentId, String name, Date statementDate, BigDecimal workingHours, BigDecimal bruttoWage, BigDecimal nettoWage,
      BigDecimal sourceTax,
      BigDecimal socialSecurityTax,
      BigDecimal vacationExtra) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(POSTING_GROUP_NR, PARTNER_NR, DOCUMENT_NR, NAME, STATEMENT_DATE, WORKING_HOURS, BRUTTO_WAGE, NETTO_WAGE, SOURCE_TAX, SOCIAL_SECURITY_TAX, VACATION_EXTRA));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":postingGroupId, :partnerId, :documentId, :name, :statementDate, :workingHours, :bruttoWage, :nettoWage, :sourceTax, :socialSecurityTax, :vacationExtra");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(), new NVPair("postingGroupId", postingGroupId), new NVPair("partnerId", partnerId), new NVPair("documentId", documentId), new NVPair("name", name), new NVPair("statementDate", statementDate),
        new NVPair("workingHours", workingHours),
        new NVPair("bruttoWage", bruttoWage),
        new NVPair("nettoWage", nettoWage),
        new NVPair("sourceTax", sourceTax),
        new NVPair("socialSecurityTax", socialSecurityTax),
        new NVPair("vacationExtra", vacationExtra));
  }

}
