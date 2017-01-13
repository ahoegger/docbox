package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.TaxGroupTableStatement;

/**
 * <h3>{@link TaxGroupTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class TaxGroupTableTask extends TaxGroupTableStatement implements ITableTask {
  private static final Logger LOG = LoggerFactory.getLogger(TaxGroupTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {
    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);
    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    TaxGroup t = TaxGroup.TAX_GROUP;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(t);
    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    TaxGroup t = TaxGroup.TAX_GROUP;
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(t);
    query.execute();
  }

  public void createRow(ISqlService sqlService, BigDecimal taxGroupId, String name, Date startDate, Date endDate) {

    TaxGroup t = TaxGroup.TAX_GROUP;
    DSL.using(sqlService.getConnection(), SQLDialect.DERBY)
        .newRecord(t)
        .with(t.END_DATE, endDate)
        .with(t.NAME, name)
        .with(t.START_DATE, startDate)
        .with(t.TAX_GROUP_NR, taxGroupId)
        .insert();

  }

}
