package ch.ahoegger.docbox.server.category;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.server.document.DocumentCategoryService;
import ch.ahoegger.docbox.shared.ISequenceTable;
import ch.ahoegger.docbox.shared.category.CategoryFormData;
import ch.ahoegger.docbox.shared.category.ICategoryService;
import ch.ahoegger.docbox.shared.category.ICategoryTable;

/**
 * <h3>{@link CategoryService}</h3>
 *
 * @author Andreas Hoegger
 */
public class CategoryService implements ICategoryService, ICategoryTable {

  @Override
  public CategoryFormData prepareCreate(CategoryFormData formData) {
    formData.getStartDate().setValue(new Date());
    return formData;
  }

  @Override
  public CategoryFormData create(CategoryFormData formData) {
    formData.setCategoryId(new BigDecimal(SQL.getSequenceNextval(ISequenceTable.TABLE_NAME)));
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("INSERT INTO ").append(TABLE_NAME);
    statementBuilder.append(" (").append(SqlFramentBuilder.columns(CATEGORY_NR, NAME, DESCRIPTION, START_DATE, END_DATE)).append(")");
    statementBuilder.append(" VALUES (:categoryId, :name, :description, :startDate, :endDate )");
    SQL.insert(statementBuilder.toString(), formData);
    return formData;
  }

  @Override
  public CategoryFormData load(CategoryFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(SqlFramentBuilder.columns(NAME, DESCRIPTION, START_DATE, END_DATE)));
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" WHERE ").append(CATEGORY_NR).append(" = :categoryId");
    statementBuilder.append(" INTO :name, :description, :startDate, :endDate");
    SQL.selectInto(statementBuilder.toString(), formData);
    return formData;
  }

  @Override
  public CategoryFormData store(CategoryFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("UPDATE ").append(TABLE_NAME).append(" SET ");
    statementBuilder.append(NAME).append("= :name, ");
    statementBuilder.append(DESCRIPTION).append("= :description, ");
    statementBuilder.append(START_DATE).append("= :startDate, ");
    statementBuilder.append(END_DATE).append("= :endDate ");
    statementBuilder.append(" WHERE ").append(CATEGORY_NR).append(" = :categoryId");
    SQL.update(statementBuilder.toString(), formData);
    return formData;
  }

  @Override
  public void delete(Long categoryId) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("DELETE FROM ").append(TABLE_NAME).append(" WHERE ").append(CATEGORY_NR).append(" = :categoryId");
    SQL.delete(statementBuilder.toString(), new NVPair("categoryId", categoryId));

    // delete document category connection
    BEANS.get(DocumentCategoryService.class).delete(categoryId);
  }
}
