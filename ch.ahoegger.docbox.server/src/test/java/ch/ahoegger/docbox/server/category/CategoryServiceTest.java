package ch.ahoegger.docbox.server.category;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.shared.category.CategoryFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link HelloWorldFormServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class CategoryServiceTest extends AbstractTestWithDatabase {

  private static final BigDecimal m_categoryId01 = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @Override
  protected void execSetupDb(Connection connection) throws Exception {
    LocalDate today = LocalDate.now();
    BEANS.get(CategoryService.class).insertRow(connection, m_categoryId01, "Sample category", "A desc", LocalDateUtility.toDate(today), null);
  }

  @Test
  public void testCreate() {
    CategoryFormData fd1 = new CategoryFormData();
    fd1.getName().setValue("SampleCategory");
    fd1.getDescription().setValue("desc");
    Calendar cal = Calendar.getInstance();
    cal.set(2014, 5, 1);
    DateUtility.truncCalendar(cal);
    fd1.getStartDate().setValue(cal.getTime());
    cal.set(2020, 02, 24);
    DateUtility.truncCalendar(cal);
    fd1.getEndDate().setValue(cal.getTime());
    fd1 = BEANS.get(CategoryService.class).create(fd1);

    CategoryFormData fd2 = new CategoryFormData();
    fd2.setCategoryId(fd1.getCategoryId());
    fd2 = BEANS.get(CategoryService.class).load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }

  @Test
  public void testUpdate() {
    CategoryFormData fd1 = new CategoryFormData();
    fd1.setCategoryId(m_categoryId01);
    fd1 = BEANS.get(CategoryService.class).load(fd1);

    fd1.getName().setValue("Modified name");
    fd1.getDescription().setValue("Mod description");
    fd1.getStartDate().setValue(LocalDateUtility.toDate(LocalDate.now().minusDays(20)));
    fd1.getStartDate().setValue(LocalDateUtility.toDate(LocalDate.now().plusDays(10)));

    BEANS.get(CategoryService.class).store(fd1);

    CategoryFormData fd2 = new CategoryFormData();
    fd2.setCategoryId(fd1.getCategoryId());
    fd2 = BEANS.get(CategoryService.class).load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
  }
}
