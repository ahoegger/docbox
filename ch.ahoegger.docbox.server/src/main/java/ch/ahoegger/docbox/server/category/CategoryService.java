package ch.ahoegger.docbox.server.category;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.Category;
import org.ch.ahoegger.docbox.server.or.app.tables.records.CategoryRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.document.DocumentCategoryService;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.category.CategoryFormData;
import ch.ahoegger.docbox.shared.category.CategorySearchFormData;
import ch.ahoegger.docbox.shared.category.CategoryTableData;
import ch.ahoegger.docbox.shared.category.CategoryTableData.CategoryTableRowData;
import ch.ahoegger.docbox.shared.category.ICategoryService;

/**
 * <h3>{@link CategoryService}</h3>
 *
 * @author Andreas Hoegger
 */
public class CategoryService implements ICategoryService {
  private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

  @Override
  public CategoryTableData getTableData(CategorySearchFormData formData) {

    Category category = Category.CATEGORY.as("cat");

    Condition condition = DSL.trueCondition();
    // name
    if (StringUtility.hasText(formData.getName().getValue())) {
      condition = condition.and(category.NAME.lower().contains(formData.getName().getValue().toLowerCase()));
    }
    // active
    if (formData.getActiveBox().getValue() != null) {
      switch (formData.getActiveBox().getValue()) {
        case TRUE:
          condition = condition.and(
              category.END_DATE.ge(new Date())
                  .or(category.END_DATE.isNull()));
          break;
        case FALSE:
          condition = condition.and(
              category.END_DATE.lessThan(new Date()));
          break;
      }
    }

    CategoryTableData tableData = new CategoryTableData();
    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(category.CATEGORY_NR, category.NAME, category.START_DATE, category.END_DATE)
        .from(category)
        .where(condition)
        .orderBy(category.NAME)
        .fetch().forEach(rec -> {
          CategoryTableRowData row = tableData.addRow();
          row.setCategoryId(rec.get(category.CATEGORY_NR));
          row.setName(rec.get(category.NAME));
          row.setStartDate(rec.get(category.START_DATE));
          row.setEndDate(rec.get(category.END_DATE));
        });
    return tableData;
  }

  @Override
  public CategoryFormData prepareCreate(CategoryFormData formData) {
    formData.getStartDate().setValue(new Date());
    return formData;
  }

  @Override
  public CategoryFormData create(CategoryFormData formData) {
    formData.setCategoryId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    if (insertRow(SQL.getConnection(), formData.getCategoryId(), formData.getName().getValue(), formData.getDescription().getValue(), formData.getStartDate().getValue(), formData.getEndDate().getValue()) > 0) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
      return formData;
    }
    return null;
  }

  @Override
  public CategoryFormData load(CategoryFormData formData) {
    Category table = Category.CATEGORY.as("cat");
    return toFormData(DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(table, table.CATEGORY_NR.eq(formData.getCategoryId())));
  }

  @Override
  public CategoryFormData store(CategoryFormData formData) {
    CategoryRecord rec = toRecord(formData);

    if (DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .executeUpdate(rec) > 0) {
      // notify backup needed
      BEANS.get(IBackupService.class).notifyModification();
    }

    return formData;
  }

  @Override
  public boolean delete(BigDecimal categoryId) {
    Category category = Category.CATEGORY.as("cat");

    CategoryRecord rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .fetchOne(category, category.CATEGORY_NR.eq(categoryId));
    if (rec == null) {
      LOG.warn("Try to delete not existing record with id '{}'!", categoryId);
      return false;
    }
    if (rec.delete() != 1) {
      LOG.error("Could not delete record with id '{}'!", categoryId);
      return false;
    }

    BEANS.get(DocumentCategoryService.class).deleteByCategoryId(categoryId);

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();
    return true;
  }

  @RemoteServiceAccessDenied
  public int insertRow(Connection connection, BigDecimal categoryId, String name, String description,
      Date startDate, Date endDate) {
    return DSL.using(connection, SQLDialect.DERBY)
        .executeInsert(toRecord(categoryId, name, description, startDate, endDate));
  }

  protected CategoryRecord toRecord(CategoryFormData formData) {
    return toRecord(formData.getCategoryId(), formData.getName().getValue(), formData.getDescription().getValue(), formData.getStartDate().getValue(), formData.getEndDate().getValue());
  }

  protected CategoryRecord toRecord(BigDecimal categoryId, String name, String description,
      Date startDate, Date endDate) {
    return new CategoryRecord()
        .with(Category.CATEGORY.CATEGORY_NR, categoryId)
        .with(Category.CATEGORY.DESCRIPTION, description)
        .with(Category.CATEGORY.END_DATE, endDate)
        .with(Category.CATEGORY.NAME, name)
        .with(Category.CATEGORY.START_DATE, startDate);
  }

  protected CategoryFormData toFormData(CategoryRecord rec) {
    if (rec == null) {
      return null;
    }
    CategoryFormData fd = new CategoryFormData();
    fd.setCategoryId(rec.getCategoryNr());
    fd.getName().setValue(rec.getName());
    fd.getDescription().setValue(rec.getDescription());
    fd.getStartDate().setValue(rec.getStartDate());
    fd.getEndDate().setValue(rec.getEndDate());
    return fd;
  }
}
