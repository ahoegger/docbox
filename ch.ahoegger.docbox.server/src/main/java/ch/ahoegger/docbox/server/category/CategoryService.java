package ch.ahoegger.docbox.server.category;

import java.math.BigDecimal;
import java.util.Date;

import org.ch.ahoegger.docbox.server.or.app.tables.Category;
import org.ch.ahoegger.docbox.server.or.app.tables.records.CategoryRecord;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.SQL;
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
    CategoryRecord category = DSL.using(SQL.getConnection(), SQLDialect.DERBY).newRecord(Category.CATEGORY);
    category.setCategoryNr(formData.getCategoryId());
    category.setName(formData.getName().getValue());
    category.setDescription(formData.getDescription().getValue());
    if (formData.getStartDate().getValue() != null) {
      category.setStartDate(new java.sql.Date(formData.getStartDate().getValue().getTime()));
    }
    if (formData.getEndDate().getValue() != null) {
      category.setEndDate(new java.sql.Date(formData.getEndDate().getValue().getTime()));
    }
    category.store();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

    return formData;
  }

  @Override
  public CategoryFormData load(CategoryFormData formData) {
    Category category = Category.CATEGORY.as("cat");

    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(category.NAME, category.DESCRIPTION, category.START_DATE, category.END_DATE)
        .from(category)
        .where(category.CATEGORY_NR.eq(formData.getCategoryId()))
        .fetch()
        .stream()
        .map(rec -> {
          CategoryFormData res = (CategoryFormData) formData.deepCopy();
          res.getName().setValue(rec.get(category.NAME));
          res.getDescription().setValue(rec.get(category.DESCRIPTION));
          res.getStartDate().setValue(rec.get(category.START_DATE));
          res.getEndDate().setValue(rec.get(category.END_DATE));
          return res;
        }).findFirst().orElse(null);

  }

  @Override
  public CategoryFormData store(CategoryFormData formData) {
    Category category = Category.CATEGORY.as("cat");

    DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .update(category)
        .set(category.NAME, formData.getName().getValue())
        .set(category.DESCRIPTION, formData.getDescription().getValue())
        .set(category.START_DATE, formData.getStartDate().getValue())
        .set(category.END_DATE, formData.getEndDate().getValue())
        .where(category.CATEGORY_NR.eq(formData.getCategoryId())).execute();

    // notify backup needed
    BEANS.get(IBackupService.class).notifyModification();

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
}
