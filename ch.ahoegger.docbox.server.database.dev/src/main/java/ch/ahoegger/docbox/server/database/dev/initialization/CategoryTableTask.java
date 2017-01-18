package ch.ahoegger.docbox.server.database.dev.initialization;

import java.math.BigDecimal;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.Category;
import org.ch.ahoegger.docbox.server.or.app.tables.records.CategoryRecord;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.or.generator.table.CategoryTableStatement;

/**
 * <h3>{@link CategoryTableTask}</h3>
 *
 * @author Andreas Hoegger
 */
public class CategoryTableTask extends CategoryTableStatement implements ITableTask {
  private static final Logger LOG = LoggerFactory.getLogger(CategoryTableTask.class);

  @Override
  public void createTable(ISqlService sqlService) {

    LOG.info("SQL-DEV create Table: {}", TABLE_NAME);

    sqlService.insert(getCreateTable());
  }

  @Override
  public void deleteTable(ISqlService sqlService) {
    LOG.info("SQL-DEV delete table: {}", TABLE_NAME);
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).delete(Category.CATEGORY);

    query.execute();
  }

  @Override
  public void dropTable(ISqlService sqlService) {
    LOG.info("SQL-DEV drop table: {}", TABLE_NAME);
    Query query = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).dropTable(Category.CATEGORY);
    query.execute();
  }

  public void insert(ISqlService sqlService, BigDecimal categoryId, String name, String description,
      Date startDate, Date endDate) {
    CategoryRecord newCatetory = DSL.using(sqlService.getConnection(), SQLDialect.DERBY).newRecord(Category.CATEGORY);
    newCatetory.setCategoryNr(categoryId);
    newCatetory.setName(name);
    newCatetory.setDescription(description);
    newCatetory.setStartDate(startDate);
    newCatetory.setEndDate(endDate);
    newCatetory.insert();
  }

}
