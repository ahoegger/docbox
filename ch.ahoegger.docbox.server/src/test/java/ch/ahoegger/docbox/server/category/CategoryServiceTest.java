package ch.ahoegger.docbox.server.category;

import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Test;

import ch.ahoegger.docbox.server.test.util.AbstractTestWithDatabase;
import ch.ahoegger.docbox.server.test.util.DocboxAssert;
import ch.ahoegger.docbox.shared.category.CategoryFormData;

/**
 * <h3>{@link HelloWorldFormServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
public class CategoryServiceTest extends AbstractTestWithDatabase {

  @Test
  public void testMessageContainsSubjectName() {

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
}
