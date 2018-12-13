package ch.ahoegger.docbox.server.category;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.server.test.util.IdGenerateService;
import ch.ahoegger.docbox.server.test.util.TestBackupService;
import ch.ahoegger.docbox.server.test.util.TestDataGenerator;
import ch.ahoegger.docbox.shared.category.CategoryFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link HelloWorldFormServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class CategoryServiceTest extends AbstractTestWithDatabase {

  private static CategoryService service;
  private static final BigDecimal id_category = BEANS.get(IdGenerateService.class).getNextIdBigDecimal();

  @BeforeClass
  public static void initService() {
    service = BEANS.get(CategoryService.class);
  }

  @Override
  protected void execSetupDb(Connection connection, TestDataGenerator testDataGenerator) throws Exception {
    LocalDate today = LocalDate.now();
    service.insertRow(connection, id_category, "Sample category", "A desc", LocalDateUtility.toDate(today), null);
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
    fd1 = service.create(fd1);

    CategoryFormData fd2 = new CategoryFormData();
    fd2.setCategoryId(fd1.getCategoryId());
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
    Assert.assertTrue(BEANS.get(TestBackupService.class).isBackupNeeded());
  }

  @Test
  public void testUpdate() {
    CategoryFormData fd1 = new CategoryFormData();
    fd1.setCategoryId(id_category);
    fd1 = service.load(fd1);

    fd1.getName().setValue("Modified name");
    fd1.getDescription().setValue("Mod description");
    fd1.getStartDate().setValue(LocalDateUtility.toDate(LocalDate.now().minusDays(20)));
    fd1.getStartDate().setValue(LocalDateUtility.toDate(LocalDate.now().plusDays(10)));

    service.store(fd1);

    CategoryFormData fd2 = new CategoryFormData();
    fd2.setCategoryId(fd1.getCategoryId());
    fd2 = service.load(fd2);

    DocboxAssert.assertEquals(fd1, fd2);
    Assert.assertTrue(BEANS.get(TestBackupService.class).isBackupNeeded());
  }
}
