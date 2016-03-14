package ch.ahoegger.docbox.server.database.initialization;

import java.util.Date;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.server.database.IDocboxSqlService;
import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.category.ICategoryTable;

/**
 * <h3>{@link CategoryTableTask}</h3>
 *
 * @author aho
 */
public class CategoryTableTask implements ITableTask, ICategoryTable {
  private static final Logger LOG = LoggerFactory.getLogger(CategoryTableTask.class);

  @Override
  public String getCreateStatement() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
    statementBuilder.append(CATEGORY_NR).append(" DECIMAL NOT NULL, ");
    statementBuilder.append(NAME).append(" VARCHAR(").append(NAME_LENGTH).append(") NOT NULL, ");
    statementBuilder.append(DESCRIPTION).append(" VARCHAR(").append(DESCRIPTION_LENGTH).append("), ");
    statementBuilder.append(START_DATE).append(" DATE, ");
    statementBuilder.append(END_DATE).append(" DATE, ");
    statementBuilder.append("PRIMARY KEY (").append(CATEGORY_NR).append(")");
    statementBuilder.append(")");
    return statementBuilder.toString();
  }

  @Override
  public void createTable(IDocboxSqlService sqlService) {
    LOG.info("SQL-DEV create Table: " + TABLE_NAME);
    sqlService.insert(getCreateStatement());
  }

  @Override
  public void createRows(IDocboxSqlService sqlService) {
    createCategoryRow(sqlService, IDevSequenceNumbers.SEQ_START_CATEGORY, "Work", "anything work related.", new Date(), null);
    createCategoryRow(sqlService, IDevSequenceNumbers.SEQ_START_CATEGORY + 1, "Household", "some window cleaning stuff.", new Date(), null);
  }

  public void createCategoryRow(IDocboxSqlService sqlService, long categoryId, String name, String description, Date startDate, Date endDate) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME).append(" (");
    statementBuilder.append(SqlFramentBuilder.columns(CATEGORY_NR, NAME, DESCRIPTION, START_DATE, END_DATE));
    statementBuilder.append(") VALUES (");
    statementBuilder.append(":categoryId, :name, :description, :startDate, :endDate");
    statementBuilder.append(")");
    sqlService.insert(statementBuilder.toString(),
        new NVPair("categoryId", categoryId),
        new NVPair("name", name),
        new NVPair("description", description),
        new NVPair("startDate", startDate),
        new NVPair("endDate", endDate));
  }

}
