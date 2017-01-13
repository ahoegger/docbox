package ch.ahoegger.docbox.server.category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.junit.Assert;
import org.junit.Test;

import ch.ahoegger.docbox.server.database.dev.initialization.CategoryTableTask;
import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.category.CategorySearchFormData;
import ch.ahoegger.docbox.shared.category.ICategoryService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link CategoryServiceSearchTest}</h3>
 *
 * @author aho
 */
public class CategoryServiceSearchTest extends AbstractTestWithDatabase {

  private static final BigDecimal id01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal id02 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();
  private static final BigDecimal id03 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  public void setupDb() throws Exception {
    super.setupDb();
    ISqlService sqlService = BEANS.get(ISqlService.class);

    BEANS.get(CategoryTableTask.class).createCategoryRow(sqlService, id01, "cat01", "desc01",
        LocalDateUtility.toDate(LocalDate.now()), null);
    BEANS.get(CategoryTableTask.class).createCategoryRow(sqlService, id02, "cat02", "desc02",
        LocalDateUtility.toDate(LocalDate.now()), LocalDateUtility.toDate(LocalDate.now()));
    BEANS.get(CategoryTableTask.class).createCategoryRow(sqlService, id03, "cat03", "desc03",
        LocalDateUtility.toDate(LocalDate.now().minusDays(2)),
        LocalDateUtility.toDate(LocalDate.now().minusDays(1)));

  }

  @Test
  public void testSearchAll() {
    CategorySearchFormData fd = new CategorySearchFormData();
    Assert.assertEquals(CollectionUtility.arrayList(id01, id02, id03),
        Arrays.stream(BEANS.get(ICategoryService.class).getTableData(fd).getRows())
            .map(row -> row.getCategoryId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testSearchByName() {
    CategorySearchFormData fd = new CategorySearchFormData();
    fd.getName().setValue("1");
    Assert.assertEquals(CollectionUtility.arrayList(id01),
        Arrays.stream(BEANS.get(ICategoryService.class).getTableData(fd).getRows())
            .map(row -> row.getCategoryId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testSearchByActive() {
    CategorySearchFormData fd = new CategorySearchFormData();
    fd.getActiveBox().setValue(TriState.TRUE);
    Assert.assertEquals(CollectionUtility.arrayList(id01, id02),
        Arrays.stream(BEANS.get(ICategoryService.class).getTableData(fd).getRows())
            .map(row -> row.getCategoryId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testSearchByInactive() {
    CategorySearchFormData fd = new CategorySearchFormData();
    fd.getActiveBox().setValue(TriState.FALSE);
    Assert.assertEquals(CollectionUtility.arrayList(id03),
        Arrays.stream(BEANS.get(ICategoryService.class).getTableData(fd).getRows())
            .map(row -> row.getCategoryId())
            .sorted()
            .collect(Collectors.toList()));
  }

  @Test
  public void testSearchByActiveAndName() {
    CategorySearchFormData fd = new CategorySearchFormData();
    fd.getActiveBox().setValue(TriState.TRUE);
    fd.getName().setValue("2");
    Assert.assertEquals(CollectionUtility.arrayList(id02),
        Arrays.stream(BEANS.get(ICategoryService.class).getTableData(fd).getRows())
            .map(row -> row.getCategoryId())
            .sorted()
            .collect(Collectors.toList()));
  }
}
